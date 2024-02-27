package glowingbanners.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@FunctionalInterface
public interface RegistrationCallback<T> {
    void register(ResourceKey<? extends Registry<T>> registry, ResourceLocation id, Supplier<T> object);
}
