package me.ultrusmods.glowingbanners.attachment;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

public class GlowBannersComponents implements ItemComponentInitializer, BlockComponentInitializer {
    public static final ComponentKey<BannerGlowComponent> BANNER_GLOW =
            ComponentRegistry.getOrCreate(IBannerGlowData.ID, BannerGlowComponent.class);
    public static final ComponentKey<ItemBannerGlowComponent> BANNER_GLOW_ITEM =
            ComponentRegistry.getOrCreate(GlowBannersMod.asResource("banner_glow_item"), ItemBannerGlowComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(BannerBlockEntity.class, BANNER_GLOW, BannerGlowComponent::new);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(item -> (item instanceof BannerItem || item instanceof ShieldItem), BANNER_GLOW_ITEM, ItemBannerGlowComponent::new);
    }
}
