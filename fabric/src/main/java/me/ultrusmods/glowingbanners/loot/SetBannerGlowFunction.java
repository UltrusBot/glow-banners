package me.ultrusmods.glowingbanners.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import me.ultrusmods.glowingbanners.registry.GlowBannersAttachmentTypes;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class SetBannerGlowFunction extends LootItemConditionalFunction {
    public static final ResourceLocation ID = GlowBannersMod.asResource("set_banner_glow");

    private static final MapCodec<SetBannerGlowFunction> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            commonFields(instance)
                    .apply(instance, SetBannerGlowFunction::new)
    );
    public static final LootItemFunctionType<SetBannerGlowFunction> TYPE = new LootItemFunctionType<>(CODEC);


    protected SetBannerGlowFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext lootContext) {
        BlockEntity blockEntity = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BannerBlockEntity bannerBlockEntity) {
            BannerGlowComponent prevData = bannerBlockEntity.getAttached(GlowBannersAttachmentTypes.BANNER_GLOW);
            if (prevData != null)
                stack.applyComponents(DataComponentPatch.builder().set(GlowBannersDataComponents.BANNER_GLOW, prevData).build());
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return TYPE;
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetBannerGlowFunction.Builder> {

        protected SetBannerGlowFunction.Builder getThis() {
            return this;
        }

        public LootItemFunction build() {
            return new SetBannerGlowFunction(this.getConditions());
        }
    }

}
