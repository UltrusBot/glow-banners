package me.ultrusmods.glowingbanners.client;

import me.ultrusmods.glowingbanners.GlowBannersModFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;

public class GlowBannersModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLoginConnectionEvents.DISCONNECT.register((handler, client) -> GlowBannersModFabric.clearItemCapCache());
    }
}
