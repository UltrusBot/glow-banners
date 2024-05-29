package me.ultrusmods.glowingbanners.registry;

import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import me.ultrusmods.glowingbanners.registry.RegistrationCallback;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GlowBannersAttachmentTypes {
    public static final AttachmentType<BannerGlowComponent> BANNER_GLOW = AttachmentType
            .builder(BannerGlowComponent::new)
            .serialize(BannerGlowComponent.CODEC)
            .build();

    public static void registerAll(RegistrationCallback<AttachmentType<?>> callback) {
        callback.register(NeoForgeRegistries.ATTACHMENT_TYPES, BannerGlowComponent.ID, BANNER_GLOW);
    }
}
