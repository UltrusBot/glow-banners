package glowingbanners.attachment;

import glowingbanners.network.GlowBannersNetworkHandler;
import glowingbanners.network.s2c.SyncBannerGlowS2CPacket;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class BannerGlowAttachment implements IBannerGlowData, INBTSerializable<CompoundTag> {
    private boolean allGlow = false;
    private final SortedSet<Integer> glowingLayers = new TreeSet<>(Integer::compare);

    public BannerGlowAttachment() {
    }

    @Override
    public boolean shouldAllGlow() {
        return allGlow;
    }

    @Override
    public void setAllGlow(boolean value) {
        this.allGlow = value;
    }

    @Override
    public boolean isLayerGlowing(int layerIndex) {
        return glowingLayers.contains(layerIndex);
    }

    @Override
    public void addGlowToLayer(int layerIndex) {
        this.glowingLayers.add(layerIndex);
    }

    @Override
    public void removeGlowFromLayer(int layerIndex) {
        this.glowingLayers.remove(layerIndex);
    }

    @Override
    public void clearGlowingLayers() {
        this.glowingLayers.clear();
    }

    @Override
    public Collection<Integer> getGlowingLayers() {
        return glowingLayers;
    }

    @Override
    public void setGlowingLayers(Collection<Integer> value) {
        for (int i : value) {
            addGlowToLayer(i);
        }
    }

    @Override
    public void sync(BannerBlockEntity blockEntity) {
        GlowBannersNetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> blockEntity.getLevel().getChunkAt(blockEntity.getBlockPos())), new SyncBannerGlowS2CPacket(blockEntity.getBlockPos(), this.serializeNBT()));
        blockEntity.invalidateCapabilities();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        serialize(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        deserialize(tag);
    }
}
