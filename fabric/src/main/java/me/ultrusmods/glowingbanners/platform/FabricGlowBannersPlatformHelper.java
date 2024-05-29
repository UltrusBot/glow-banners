package me.ultrusmods.glowingbanners.platform;

import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import me.ultrusmods.glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import me.ultrusmods.glowingbanners.registry.GlowBannersAttachmentTypes;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FabricGlowBannersPlatformHelper implements IGlowBannersPlatformHelper {
    @Nullable
    public BannerGlowComponent getData(BlockEntity blockEntity) {
        if (blockEntity == null) {
            return null;
        }
        return blockEntity.getAttached(GlowBannersAttachmentTypes.BANNER_GLOW);
    }

    @Override
    public BannerGlowComponent getOrCreateData(BlockEntity blockEntity) {
        return blockEntity.getAttachedOrCreate(GlowBannersAttachmentTypes.BANNER_GLOW);
    }

    @Override
    public boolean hasData(BlockEntity blockEntity) {
        return blockEntity.hasAttached(GlowBannersAttachmentTypes.BANNER_GLOW);
    }

    @Override
    public BannerGlowComponent setData(BlockEntity blockEntity, BannerGlowComponent component) {
        return blockEntity.setAttached(GlowBannersAttachmentTypes.BANNER_GLOW, component);
    }

    @Override
    public BannerGlowComponent removeData(BlockEntity blockEntity) {
        return blockEntity.removeAttached(GlowBannersAttachmentTypes.BANNER_GLOW);
    }

    public void syncBlockEntity(BlockEntity blockEntity) {
        for (ServerPlayer player : PlayerLookup.tracking(blockEntity)) {
            ServerPlayNetworking.send(player, new SyncBannerGlowS2CPacket(blockEntity.getBlockPos(), Optional.ofNullable(blockEntity.getAttached(GlowBannersAttachmentTypes.BANNER_GLOW))));
        }
    }
}
