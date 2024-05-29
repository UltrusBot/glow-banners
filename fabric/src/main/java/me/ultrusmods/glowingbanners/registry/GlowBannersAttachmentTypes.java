package me.ultrusmods.glowingbanners.registry;

import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class GlowBannersAttachmentTypes {
    public static final AttachmentType<BannerGlowComponent> BANNER_GLOW = AttachmentRegistry.<BannerGlowComponent>builder()
            .persistent(BannerGlowComponent.CODEC)
            .initializer(BannerGlowComponent::new)
            .buildAndRegister(BannerGlowComponent.ID);

    public static void init() {}
}
