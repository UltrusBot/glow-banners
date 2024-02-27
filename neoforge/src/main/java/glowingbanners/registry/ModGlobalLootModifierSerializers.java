package glowingbanners.registry;

import com.mojang.serialization.Codec;
import glowingbanners.loot.GlowBannerLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModGlobalLootModifierSerializers {
    public static void registerAll(RegistrationCallback<Codec<? extends IGlobalLootModifier>> callback) {
        callback.register(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, GlowBannerLootModifier.ID, () -> GlowBannerLootModifier.CODEC);
    }
}
