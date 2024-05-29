package me.ultrusmods.glowingbanners.client;

import me.ultrusmods.glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class GlowBannersModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncBannerGlowS2CPacket.TYPE, (packet, context) -> packet.handle());
    }
}
