package me.ultrusmods.glowingbanners.platform;

import me.ultrusmods.glowingbanners.GlowBannersMod;

import java.util.ServiceLoader;

public class ServiceUtil {
    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        GlowBannersMod.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
