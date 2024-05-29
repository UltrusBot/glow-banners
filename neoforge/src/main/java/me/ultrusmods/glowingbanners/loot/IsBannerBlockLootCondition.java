package me.ultrusmods.glowingbanners.loot;

import com.mojang.serialization.MapCodec;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class IsBannerBlockLootCondition implements LootItemCondition {
    public static final ResourceLocation ID = GlowBannersMod.asResource("is_banner_block");
    private static final IsBannerBlockLootCondition INSTANCE = new IsBannerBlockLootCondition();
    public static final MapCodec<IsBannerBlockLootCondition> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public LootItemConditionType getType() {
        return BuiltInRegistries.LOOT_CONDITION_TYPE.get(ID);
    }

    @Override
    public boolean test(LootContext lootContext) {
        return lootContext.getQueriedLootTableId().getPath().contains("banner") && lootContext.getQueriedLootTableId().toString().contains("minecraft:blocks/");
    }

    public static LootItemCondition.Builder isBannerBlock() {
        return () -> INSTANCE;
    }
}
