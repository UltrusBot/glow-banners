package me.ultrusmods.glowingbanners;

import me.ultrusmods.glowingbanners.loot.SetBannerGlowFunction;
import me.ultrusmods.glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import me.ultrusmods.glowingbanners.platform.FabricGlowBannersPlatformHelper;
import me.ultrusmods.glowingbanners.registry.GlowBannersAttachmentTypes;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class GlowBannersModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GlowBannersMod.setHelper(new FabricGlowBannersPlatformHelper());
        GlowBannersAttachmentTypes.init();
        GlowBannersDataComponents.registerAll(Registry::register);
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, SetBannerGlowFunction.ID, SetBannerGlowFunction.TYPE);
        PayloadTypeRegistry.playS2C().register(SyncBannerGlowS2CPacket.TYPE, SyncBannerGlowS2CPacket.STREAM_CODEC);
        registerEvents();
    }

    private static void registerEvents() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
            if (key.location().getPath().contains("banner") && key.location().toString().contains("minecraft:blocks/")) {
                tableBuilder.unwrap().modifyPools(pool -> {
                    pool.apply(new SetBannerGlowFunction.Builder().build());
                });
            }
        });

        UseBlockCallback.EVENT.register(GlowBannersMod::interactWithBlock);
    }

}
