package me.ultrusmods.glowingbanners;

import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.mixin.AbstractCauldronBlockAccessor;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlowBannersMod {
    public static final String MOD_ID = "glowingbanners";
    public static final String MOD_NAME = "Glow Banners";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static IBannerGlowData BANNER_RENDERER_CONTEXT;

    public void init() {
    }

    public static InteractionResult interactWithBlock(Player player, Level level, InteractionHand hand, BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity entity = level.getBlockEntity(pos);

        if (entity instanceof BannerBlockEntity) {
            return GlowBannersMod.addGlowingToBanner(player, level, hand, result);
        } else if (state.getBlock() instanceof LayeredCauldronBlock block && ((AbstractCauldronBlockAccessor)block).glowBanners$getInteractions().equals(CauldronInteraction.WATER)) {
            return GlowBannersMod.removeGlowFromBanner(player, level, hand, result);
        }

        return InteractionResult.PASS;
    }

    private static InteractionResult addGlowingToBanner(Player player, Level level, InteractionHand hand, BlockHitResult result) {
        ItemStack heldStack = player.getItemInHand(hand);
        boolean hasGlowInkSac = heldStack.is(Items.GLOW_INK_SAC);
        BlockPos pos = result.getBlockPos();

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BannerBlockEntity bannerBlockEntity) {
            IBannerGlowData data = IGlowBannersPlatformHelper.INSTANCE.getData(bannerBlockEntity);

            if (!data.shouldAllGlow())  {
                if (level.isClientSide()) {
                    return (hasGlowInkSac && player.getAbilities().mayBuild) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
                } else {
                    if (!hasGlowInkSac) {
                        return InteractionResult.PASS;
                    }
                    if (IGlowBannersPlatformHelper.INSTANCE.getData(bannerBlockEntity).shouldAllGlow()) {
                        return InteractionResult.PASS;
                    }
                    level.playSound(null, result.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, heldStack);
                    }
                    data.clearGlowingLayers();
                    data.setAllGlow(true);
                    data.sync(bannerBlockEntity);

                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;

    }

    private static InteractionResult removeGlowFromBanner(Player player, Level level, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = result.getBlockPos();

        IBannerGlowData data = IGlowBannersPlatformHelper.INSTANCE.getData(stack);
        if (stack.getItem() instanceof BannerItem && data != null) {
            ItemStack itemStack = stack.copy();
            IBannerGlowData copiedData = IGlowBannersPlatformHelper.INSTANCE.getData(itemStack);

            boolean updated = false;
            if (level.isClientSide()) {
                updated = copiedData.shouldAllGlow() || BannerBlockEntity.getItemPatterns(itemStack) != null && copiedData.isLayerGlowing(BannerBlockEntity.getItemPatterns(itemStack).size());

                if (updated)
                    return InteractionResult.SUCCESS;
            } else {
                itemStack.setCount(1);

                if (copiedData.shouldAllGlow()) {
                    copiedData.setAllGlow(false);
                    updated = true;
                } else if (BannerBlockEntity.getItemPatterns(itemStack) != null) {
                    int lastLayer = BannerBlockEntity.getItemPatterns(itemStack).size();
                    if (copiedData.isLayerGlowing(lastLayer)) {
                        copiedData.removeLastGlowFromLayer();
                        updated = true;
                    }
                }

                if (updated) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, itemStack);
                    } else if (player.getInventory().add(itemStack)) {
                        player.containerMenu.broadcastChanges();
                    } else {
                        player.drop(itemStack, false);
                    }

                    LayeredCauldronBlock.lowerFillLevel(level.getBlockState(pos), level, pos);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
