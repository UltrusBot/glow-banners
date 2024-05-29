package me.ultrusmods.glowingbanners.platform;

import me.ultrusmods.glowingbanners.registry.GlowBannersAttachmentTypes;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import me.ultrusmods.glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NeoForgeGlowBannersPlatformHelper implements IGlowBannersPlatformHelper {

    @Nullable
    @Override
    public BannerGlowComponent getData(BlockEntity blockEntity) {
        return blockEntity.getExistingData(GlowBannersAttachmentTypes.BANNER_GLOW).orElse(null);
    }

    @Override
    public BannerGlowComponent getOrCreateData(BlockEntity blockEntity) {
        return blockEntity.getData(GlowBannersAttachmentTypes.BANNER_GLOW);
    }

    @Override
    public boolean hasData(BlockEntity blockEntity) {
        return blockEntity.hasData(GlowBannersAttachmentTypes.BANNER_GLOW);
    }

    @Override
    public BannerGlowComponent setData(BlockEntity blockEntity, BannerGlowComponent component) {
        return blockEntity.setData(GlowBannersAttachmentTypes.BANNER_GLOW, component);
    }

    @Override
    public BannerGlowComponent removeData(BlockEntity blockEntity) {
        return blockEntity.removeData(GlowBannersAttachmentTypes.BANNER_GLOW);
    }

    @Override
    public void syncBlockEntity(BlockEntity blockEntity) {
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) blockEntity.getLevel(), blockEntity.getLevel().getChunkAt(blockEntity.getBlockPos()).getPos(), new SyncBannerGlowS2CPacket(blockEntity.getBlockPos(), Optional.ofNullable(blockEntity.getData(GlowBannersAttachmentTypes.BANNER_GLOW))));
        blockEntity.invalidateCapabilities();
    }
}
