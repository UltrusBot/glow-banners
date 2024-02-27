package glowingbanners.client;

import glowingbanners.GlowBannersModNeoForge;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@Mod.EventBusSubscriber(modid = GlowBannersMod.MOD_ID, value = Dist.CLIENT)
public class GlowBannersClientEvents {
    @SubscribeEvent
    public static void onClientLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
        GlowBannersModNeoForge.clearItemCapCache();
    }
}
