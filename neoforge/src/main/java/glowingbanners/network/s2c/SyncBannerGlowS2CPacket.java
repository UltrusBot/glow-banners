package glowingbanners.network.s2c;

import glowingbanners.GlowBannersModNeoForge;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.NetworkEvent;

public record SyncBannerGlowS2CPacket(BlockPos pos, CompoundTag tag) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos());
        buf.writeNbt(this.tag());
    }

    public static SyncBannerGlowS2CPacket decode(FriendlyByteBuf buf) {
        return new SyncBannerGlowS2CPacket(buf.readBlockPos(), buf.readNbt());
    }

    public void handle(NetworkEvent.Context context) {
        Minecraft.getInstance().execute(() -> {
            BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(this.pos());
            blockEntity.getData(GlowBannersModNeoForge.BANNER_GLOW_BLOCK).deserializeNBT(this.tag());
        });
        context.setPacketHandled(true);
    }

}
