package glowingbanners.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import glowingbanners.GlowBannersModNeoForge;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class GlowBannerLootModifier extends LootModifier {
    public static final Codec<GlowBannerLootModifier> CODEC = RecordCodecBuilder.create(inst ->
            LootModifier.codecStart(inst).apply(inst, GlowBannerLootModifier::new));

    protected GlowBannerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> objectArrayList, LootContext lootContext) {
        objectArrayList.stream().filter(itemStack -> itemStack.getItem() instanceof BannerItem).forEach(stack -> {
            BlockEntity blockEntity = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);
            if (blockEntity instanceof BannerBlockEntity) {
                IBannerGlowData blockEntityData = blockEntity.getData(GlowBannersModNeoForge.BANNER_GLOW_BLOCK);
                IBannerGlowData stackData = stack.getCapability(GlowBannersModNeoForge.BANNER_GLOW_ITEM);
                if (stackData != null)
                    stackData.setFromOther(blockEntityData);
            }
        });
        return objectArrayList;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
