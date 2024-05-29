package me.ultrusmods.glowingbanners.network.s2c;

import com.mojang.datafixers.util.Pair;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public record SyncBannerGlowS2CPacket(BlockPos pos, Optional<BannerGlowComponent> attachment) implements CustomPacketPayload {
    public static final Type<SyncBannerGlowS2CPacket> TYPE = new Type<>(GlowBannersMod.asResource("sync_banner_glow"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBannerGlowS2CPacket> STREAM_CODEC = StreamCodec.of(SyncBannerGlowS2CPacket::write, SyncBannerGlowS2CPacket::new);

    public SyncBannerGlowS2CPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), !buf.readBoolean() ? Optional.empty() : BannerGlowComponent.CODEC.decode(NbtOps.INSTANCE, buf.readNbt()).result().map(Pair::getFirst));
    }

    public static void write(FriendlyByteBuf buf, SyncBannerGlowS2CPacket packet) {
        buf.writeBlockPos(packet.pos);
        buf.writeBoolean(packet.attachment.isPresent());
        packet.attachment.ifPresent(component -> buf.writeNbt(BannerGlowComponent.CODEC.encodeStart(NbtOps.INSTANCE, component).getOrThrow()));
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(this.pos());
            if ((blockEntity == null)) {
                return;
            }
            if (attachment.isEmpty())
                GlowBannersMod.getHelper().removeData(blockEntity);
            else
                GlowBannersMod.getHelper().setData(blockEntity, attachment.get());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
