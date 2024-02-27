package glowingbanners.registry;

import glowingbanners.loot.IsBannerBlockLootCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ModLootConditionTypes {
    public static void registerAll(RegistrationCallback<LootItemConditionType> callback) {
        callback.register(Registries.LOOT_CONDITION_TYPE, IsBannerBlockLootCondition.ID, () -> new LootItemConditionType(IsBannerBlockLootCondition.CODEC));
    }
}
