package glowingbanners.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class IsBannerBlockLootCondition implements LootItemCondition {
    private static final Codec<IsBannerBlockLootCondition> CODEC = Codec.unit(IsBannerBlockLootCondition::new);
    public static final LootItemConditionType TYPE = new LootItemConditionType(CODEC);

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return lootContext.getQueriedLootTableId().getPath().contains("banner") && lootContext.getQueriedLootTableId().toString().contains("minecraft:blocks/");
    }
}
