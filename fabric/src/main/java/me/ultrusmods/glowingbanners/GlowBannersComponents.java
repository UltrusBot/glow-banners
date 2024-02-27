package me.ultrusmods.glowingbanners;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import me.ultrusmods.glowingbanners.attachment.BannerGlowComponent;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

public class GlowBannersComponents implements BlockComponentInitializer {
    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(BannerBlockEntity.class, GlowBannersModFabric.BANNER_GLOW, BannerGlowComponent::new);
    }
}