package me.ultrusmods.glowingbanners.registry;

import me.ultrusmods.glowingbanners.loot.IsBannerBlockLootCondition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class GlowBannersLootConditionTypes {
    public static void registerAll(RegistrationCallback<LootItemConditionType> callback) {
        callback.register(BuiltInRegistries.LOOT_CONDITION_TYPE, IsBannerBlockLootCondition.ID, new LootItemConditionType(IsBannerBlockLootCondition.CODEC));
    }
}
