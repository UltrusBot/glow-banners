package me.ultrusmods.glowingbanners.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import me.ultrusmods.glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import me.ultrusmods.glowingbanners.registry.GlowBannersAttachmentTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(ChunkHolder.class)
public class ChunkHolderMixin {
    @Inject(method = "broadcastBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkHolder;broadcast(Ljava/util/List;Lnet/minecraft/network/protocol/Packet;)V", shift = At.Shift.AFTER))
    private void glowBanners$updateGlow(List<ServerPlayer> list, Level level, BlockPos blockPos, CallbackInfo ci, @Local BlockEntity blockEntity) {
        if (!blockEntity.hasAttached(GlowBannersAttachmentTypes.BANNER_GLOW))
            return;
        for (ServerPlayer player : list)
            ServerPlayNetworking.send(player, new SyncBannerGlowS2CPacket(blockPos, Optional.of(blockEntity.getAttached(GlowBannersAttachmentTypes.BANNER_GLOW))));
    }
}
