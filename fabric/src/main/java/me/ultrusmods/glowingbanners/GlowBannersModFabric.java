package me.ultrusmods.glowingbanners;

import me.ultrusmods.glowingbanners.loot.SetBannerGlowFunction;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class GlowBannersModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, SetBannerGlowFunction.ID, SetBannerGlowFunction.TYPE);
        registerEvents();
    }

    private static void registerEvents() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.getPath().contains("banner") && id.toString().contains("minecraft:blocks/")) {
                tableBuilder.unwrap().modifyPools(pool -> {
                    pool.apply(new SetBannerGlowFunction.Builder().build());
                });
            }
        });

        UseBlockCallback.EVENT.register(GlowBannersMod::interactWithBlock);
    }
}
