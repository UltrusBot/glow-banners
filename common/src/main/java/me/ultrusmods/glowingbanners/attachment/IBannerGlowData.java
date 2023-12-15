package me.ultrusmods.glowingbanners.attachment;

import me.ultrusmods.glowingbanners.GlowBannersMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

import java.util.Collection;

public interface IBannerGlowData {
    ResourceLocation ID = GlowBannersMod.asResource("banner_glow");

    boolean shouldAllGlow();
    void setAllGlow(boolean value);
    boolean isLayerGlowing(int layerIndex);
    void addGlowToLayer(int layerIndex);
    void removeGlowFromLayer(int layerIndex);
    void clearGlowingLayers();
    Collection<Integer> getGlowingLayers();
    void setGlowingLayers(Collection<Integer> value);
    default void setFromOther(IBannerGlowData other) {
        this.setAllGlow(other.shouldAllGlow());
        this.setGlowingLayers(other.getGlowingLayers());
    }

    default void serialize(CompoundTag tag) {
        tag.putBoolean("should_all_glow", this.shouldAllGlow());

        ListTag layersList = new ListTag();
        for (int layer : this.getGlowingLayers())
            layersList.add(IntTag.valueOf(layer));
        tag.put("glowing_layers", layersList);
    }
    default void deserialize(CompoundTag tag) {

        this.setAllGlow(tag.getBoolean("should_all_glow"));

        ListTag layers = tag.getList("glowing_layers", ListTag.TAG_INT);
        for (int i = 0; i < layers.size(); ++i) {
            this.addGlowToLayer(layers.getInt(i));
        }
    }
    void sync(BannerBlockEntity blockEntity);
}
