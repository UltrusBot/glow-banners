package me.ultrusmods.glowingbanners.registry;

import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class GlowBannersDataComponents {
    public static final DataComponentType<BannerGlowComponent> BANNER_GLOW =
            DataComponentType.<BannerGlowComponent>builder()
                    .persistent(BannerGlowComponent.CODEC)
                    .networkSynchronized(BannerGlowComponent.STREAM_CODEC)
                    .build();

    public static void registerAll(RegistrationCallback<DataComponentType<?>> callback) {
        callback.register(BuiltInRegistries.DATA_COMPONENT_TYPE, BannerGlowComponent.ID, BANNER_GLOW);
    }
}
