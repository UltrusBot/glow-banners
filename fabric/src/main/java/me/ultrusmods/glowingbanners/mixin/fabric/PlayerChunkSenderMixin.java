package me.ultrusmods.glowingbanners.mixin.fabric;

import me.ultrusmods.glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import me.ultrusmods.glowingbanners.registry.GlowBannersAttachmentTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerChunkSender.class)
public class PlayerChunkSenderMixin {
    @Inject(method = "sendChunk", at = @At("TAIL"))
    private static void glowBanners$sendBannerGlow(ServerGamePacketListenerImpl listener, ServerLevel level, LevelChunk chunk, CallbackInfo ci) {
        for (BlockEntity blockEntity : chunk.getBlockEntities().values().stream().filter(blockEntity -> blockEntity.hasAttached(GlowBannersAttachmentTypes.BANNER_GLOW)).toList()) {
            ServerPlayNetworking.send(listener.player, new SyncBannerGlowS2CPacket(blockEntity.getBlockPos(), Optional.ofNullable(blockEntity.getAttached(GlowBannersAttachmentTypes.BANNER_GLOW))));
        }
    }
}
