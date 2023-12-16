package glowingbanners;

import glowingbanners.attachment.BannerGlowAttachment;
import glowingbanners.attachment.BannerGlowItemCapability;
import glowingbanners.loot.GlowBannerLootModifier;
import glowingbanners.loot.IsBannerBlockLootCondition;
import glowingbanners.network.GlowBannersNetworkHandler;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

@Mod(GlowBannersMod.MOD_ID)
public class GlowBannersModNeoForge {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, GlowBannersMod.MOD_ID);
    public static final ItemCapability<IBannerGlowData, Void> BANNER_GLOW_ITEM = ItemCapability.createVoid(
            GlowBannersMod.asResource("banner_glow"),
            IBannerGlowData.class
    );
    private static final Map<ItemStack, BannerGlowItemCapability> ITEM_CAPABILITY_CACHE = new WeakHashMap<>(128);
    public static final Supplier<AttachmentType<BannerGlowAttachment>> BANNER_GLOW_BLOCK = ATTACHMENT_TYPES.register("banner_glow", () -> AttachmentType.serializable(BannerGlowAttachment::new).build());

    public GlowBannersModNeoForge(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }

    public static void clearItemCapCache() {
        ITEM_CAPABILITY_CACHE.clear();
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
                event.register(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, GlowBannerLootModifier.ID, () -> GlowBannerLootModifier.CODEC);
            } else if (event.getRegistryKey() == Registries.LOOT_CONDITION_TYPE) {
                event.register(Registries.LOOT_CONDITION_TYPE, IsBannerBlockLootCondition.ID, () -> new LootItemConditionType(IsBannerBlockLootCondition.CODEC));
            }
        }
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof BannerItem || item instanceof ShieldItem)
                    event.registerItem(
                            BANNER_GLOW_ITEM,
                            (itemStack, aVoid) -> ITEM_CAPABILITY_CACHE.computeIfAbsent(itemStack, stack -> {
                                if (stack.getItem() instanceof ShieldItem && BlockItem.getBlockEntityData(stack) == null)
                                    return null;

                                CompoundTag bannerGlowTag = BlockItem.getBlockEntityData(stack);
                                BannerGlowItemCapability api = new BannerGlowItemCapability(stack);
                                if (bannerGlowTag != null && bannerGlowTag.contains(AttachmentHolder.ATTACHMENTS_NBT_KEY) && bannerGlowTag.getCompound(AttachmentHolder.ATTACHMENTS_NBT_KEY).contains(IBannerGlowData.ID.toString()))
                                    api.deserialize(bannerGlowTag.getCompound(AttachmentHolder.ATTACHMENTS_NBT_KEY).getCompound(IBannerGlowData.ID.toString()));
                                return api;
                            }),
                            item);
            }
        }
    }
}
