package glowingbanners;

import glowingbanners.attachment.BannerGlowAttachment;
import glowingbanners.loot.GlowBannerLootModifier;
import glowingbanners.loot.IsBannerBlockLootCondition;
import glowingbanners.network.GlowBannersNetworkHandler;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Supplier;

@Mod(GlowBannersMod.MOD_ID)
public class GlowBannersModNeoForge {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, GlowBannersMod.MOD_ID);

    public static final Supplier<AttachmentType<BannerGlowAttachment>> BANNER_GLOW_ITEM = ATTACHMENT_TYPES.register("banner_glow_item", () -> AttachmentType.serializable(BannerGlowAttachment::new).build());
    public static final Supplier<AttachmentType<BannerGlowAttachment>> BANNER_GLOW_BLOCK = ATTACHMENT_TYPES.register("banner_glow", () -> AttachmentType.serializable(BannerGlowAttachment::new).build());

    public GlowBannersModNeoForge(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }

    @Mod.EventBusSubscriber(modid = GlowBannersMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class NeoForgeBusEvents {
        @SubscribeEvent
        public static void registerContents(PlayerInteractEvent.RightClickBlock event) {
            InteractionResult result = GlowBannersMod.interactWithBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
            if (result != InteractionResult.PASS) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = GlowBannersMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            GlowBannersNetworkHandler.init();
        }

        @SubscribeEvent
        public static void registerContents(RegisterEvent event) {
            if (event.getRegistryKey() == NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS) {
                event.register(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, GlowBannersMod.asResource("set_banner_glow"), () -> GlowBannerLootModifier.CODEC);
            } else if (event.getRegistryKey() == Registries.LOOT_CONDITION_TYPE) {
                event.register(Registries.LOOT_CONDITION_TYPE, GlowBannersMod.asResource("is_banner_block"), () -> IsBannerBlockLootCondition.TYPE);
            }
        }
    }
}
