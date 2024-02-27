package me.ultrusmods.glowingbanners;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import me.ultrusmods.glowingbanners.attachment.BannerGlowComponent;
import me.ultrusmods.glowingbanners.attachment.BannerGlowApi;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.loot.SetBannerGlowFunction;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

import java.util.Map;
import java.util.WeakHashMap;

public class GlowBannersModFabric implements ModInitializer {
    public static final ComponentKey<BannerGlowComponent> BANNER_GLOW =
            ComponentRegistry.getOrCreate(IBannerGlowData.ID, BannerGlowComponent.class);

    public static final ItemApiLookup<IBannerGlowData, Void> BANNER_GLOW_ITEM =
            ItemApiLookup.get(GlowBannersMod.asResource("banner_glow"), IBannerGlowData.class, Void.class);

    private static final Map<ItemStack, BannerGlowApi> ITEM_API_CACHE = new WeakHashMap<>(128);

    public static void clearItemCapCache() {
        ITEM_API_CACHE.clear();
    }

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, SetBannerGlowFunction.ID, SetBannerGlowFunction.TYPE);
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BannerItem || item instanceof ShieldItem)
                BANNER_GLOW_ITEM.registerForItems((itemStack, context) -> ITEM_API_CACHE.computeIfAbsent(itemStack, stack -> {
                    if (stack.getItem() instanceof ShieldItem && BlockItem.getBlockEntityData(stack) == null)
                        return null;

                    CompoundTag bannerGlowTag = BlockItem.getBlockEntityData(stack);
                    BannerGlowApi api = new BannerGlowApi(stack);
                    if (bannerGlowTag != null && bannerGlowTag.contains("cardinal_components") && bannerGlowTag.getCompound("cardinal_components").contains(IBannerGlowData.ID.toString()))
                        api.deserialize(bannerGlowTag.getCompound("cardinal_components").getCompound(IBannerGlowData.ID.toString()));
                    return api;
                }), item);
        }
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
