package me.ultrusmods.glowingbanners.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
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

    private static final Codec<SetBannerGlowFunction> CODEC = RecordCodecBuilder.create((instance) ->
            commonFields(instance)
                    .apply(instance, SetBannerGlowFunction::new)
    );
    public static final LootItemFunctionType TYPE = new LootItemFunctionType(CODEC);


    protected SetBannerGlowFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext lootContext) {
        BlockEntity blockEntity = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BannerBlockEntity bannerBlockEntity) {
            IBannerGlowData prevData = IGlowBannersPlatformHelper.INSTANCE.getData(bannerBlockEntity);
            IBannerGlowData glowData = IGlowBannersPlatformHelper.INSTANCE.getData(stack);
            glowData.setFromOther(prevData);
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
