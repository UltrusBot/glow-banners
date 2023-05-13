package me.ultrusmods.glowingbanners;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;

public class GlowBannersMod implements ModInitializer {

    @Override
    public void onInitialize() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.getPath().contains("banner") && id.toString().contains("minecraft:blocks/")) {
                tableBuilder.unwrap().modifyPools(pool -> {
                    pool.apply(CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY).withOperation("isGlowing", "BlockEntityTag.isGlowing"));
                });
            }
        });
    }
}
