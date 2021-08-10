package io.github.ultrusbot.glowingbanners.mixin;

import io.github.ultrusbot.glowingbanners.GlowingBannerInterface;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBannerBlock.class)
public abstract class GlowingBannerBlockMixin extends BlockWithEntity {
    protected GlowingBannerBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getStackInHand(hand);
        boolean hasGlowInkSac = heldStack.isOf(Items.GLOW_INK_SAC);

        if (world.isClient()) {
            return (hasGlowInkSac && player.getAbilities().allowModifyWorld) ? ActionResult.SUCCESS : ActionResult.CONSUME;
        } else {
            if (!hasGlowInkSac) {
                return ActionResult.PASS;
            }
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BannerBlockEntity) {
                if (((GlowingBannerInterface)blockEntity).isGlowing()) {
                    return ActionResult.PASS;
                }
                world.playSound(null, pos, SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (player instanceof ServerPlayerEntity) {
                    Criteria.ITEM_USED_ON_BLOCK.test((ServerPlayerEntity)player, pos, heldStack);
                }
                ((GlowingBannerInterface)blockEntity).setGlowing(true);
                if (!player.isCreative()) {
                    heldStack.decrement(1);
                }
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        }
    }
}
