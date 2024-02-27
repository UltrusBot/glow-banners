package glowingbanners.network;

import glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class GlowBannersNetworkHandler {

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            GlowBannersMod.asResource("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int i = 0;
        INSTANCE.registerMessage(i++, SyncBannerGlowS2CPacket.class, SyncBannerGlowS2CPacket::encode, SyncBannerGlowS2CPacket::decode, SyncBannerGlowS2CPacket::handle);
    }

}
