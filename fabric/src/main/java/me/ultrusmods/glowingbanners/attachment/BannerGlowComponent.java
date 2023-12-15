package me.ultrusmods.glowingbanners.attachment;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class BannerGlowComponent implements IBannerGlowData, AutoSyncedComponent {
    private boolean allGlow = false;
    private final SortedSet<Integer> glowingLayers = new TreeSet<>(Integer::compare);
    private final BannerBlockEntity provider;

    public BannerGlowComponent(BannerBlockEntity provider) {
        this.provider = provider;
    }

    @Override
    public boolean shouldAllGlow() {
        return this.allGlow;
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
    public void removeLastGlowFromLayer() {
        this.glowingLayers.remove(this.glowingLayers.last());
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
        GlowBannersComponents.BANNER_GLOW.sync(blockEntity);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.deserialize(tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        this.serialize(tag);
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return PlayerLookup.tracking(provider).contains(player);
    }
}
