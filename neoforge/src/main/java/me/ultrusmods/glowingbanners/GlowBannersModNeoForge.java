package me.ultrusmods.glowingbanners;

import me.ultrusmods.glowingbanners.platform.NeoForgeGlowBannersPlatformHelper;
import me.ultrusmods.glowingbanners.registry.GlowBannersAttachmentTypes;
import me.ultrusmods.glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import me.ultrusmods.glowingbanners.registry.GlowBannersGlobalLootModifierSerializers;
import me.ultrusmods.glowingbanners.registry.GlowBannersLootConditionTypes;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;
import me.ultrusmods.glowingbanners.registry.RegistrationCallback;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

@Mod(GlowBannersMod.MOD_ID)
public class GlowBannersModNeoForge {
    public GlowBannersModNeoForge(IEventBus bus) {
        GlowBannersMod.setHelper(new NeoForgeGlowBannersPlatformHelper());
    }

    @EventBusSubscriber(modid = GlowBannersMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
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

    @EventBusSubscriber(modid = GlowBannersMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerPayload(RegisterPayloadHandlersEvent event) {
            event.registrar(GlowBannersMod.MOD_ID)
                    .versioned("1.0.0")
                    .playToClient(SyncBannerGlowS2CPacket.TYPE, SyncBannerGlowS2CPacket.STREAM_CODEC, (payload, context) -> context.enqueueWork(payload::handle));
        }

        @SubscribeEvent
        public static void registerContents(RegisterEvent event) {
            if (event.getRegistryKey() == NeoForgeRegistries.Keys.ATTACHMENT_TYPES)
                register(event, GlowBannersAttachmentTypes::registerAll);
            if (event.getRegistryKey() == NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS)
                register(event, GlowBannersGlobalLootModifierSerializers::registerAll);
            if (event.getRegistryKey() == Registries.LOOT_CONDITION_TYPE)
                register(event, GlowBannersLootConditionTypes::registerAll);
            if (event.getRegistryKey() == Registries.DATA_COMPONENT_TYPE)
                register(event, GlowBannersDataComponents::registerAll);
        }

        private static <T> void register(RegisterEvent event, Consumer<RegistrationCallback<T>> consumer) {
            consumer.accept((registry, id, value) -> event.register(registry.key(), id, () -> value));
        }
    }
}
