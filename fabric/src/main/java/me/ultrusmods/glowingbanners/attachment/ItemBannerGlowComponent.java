package me.ultrusmods.glowingbanners.attachment;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ItemBannerGlowComponent extends ItemComponent implements IBannerGlowData {
    protected boolean initialized = false;
    private final SortedSet<Integer> glowingLayers = new TreeSet<>(Integer::compare);
    public ItemBannerGlowComponent(ItemStack stack) {
        super(stack, GlowBannersComponents.BANNER_GLOW);
    }

    @Override
    public boolean shouldAllGlow() {
        return this.getBoolean("ShouldAllGlow");
    }

    @Override
    public void setAllGlow(boolean value) {
        this.putBoolean("ShouldAllGlow", value);
    }

    @Override
    public boolean isLayerGlowing(int layerIndex) {
        if (!initialized) {
            this.glowingLayers.addAll(this.getList("GlowingLayers", ListTag.TAG_INT).stream().map(tag -> ((IntTag)tag).getAsInt()).toList());
            this.initialized = true;
        }
        return glowingLayers.contains(layerIndex);
    }

    @Override
    public void addGlowToLayer(int layerIndex) {
        ListTag list = this.getList("GlowingLayers", ListTag.TAG_INT);
        list.add(IntTag.valueOf(layerIndex));
        list.sort(Comparator.comparingInt(o -> ((IntTag) o).getAsInt()));
        this.putList("GlowingLayers", list);
    }

    @Override
    public void removeLastGlowFromLayer() {
        ListTag list = this.getList("GlowingLayers", ListTag.TAG_INT);
        list.remove(list.size() - 1);
        list.sort(Comparator.comparingInt(o -> ((IntTag) o).getAsInt()));
        this.putList("GlowingLayers", list);
    }

    @Override
    public void clearGlowingLayers() {
        this.remove("GlowingLayers");
    }

    @Override
    public Collection<Integer> getGlowingLayers() {
        if (!initialized) {
            this.glowingLayers.addAll(this.getList("GlowingLayers", ListTag.TAG_INT).stream().map(tag -> ((IntTag)tag).getAsInt()).toList());
            this.initialized = true;
        }
        return glowingLayers;
    }

    @Override
    public void setGlowingLayers(Collection<Integer> value) {
        for (int i : value) {
            addGlowToLayer(i);
        }
    }

    @Override
    public void serialize(CompoundTag tag) {
        // Not required.
    }

    @Override
    public void deserialize(CompoundTag tag) {
        // Not required.
    }

    @Override
    public void sync(BannerBlockEntity blockEntity) {
        // Not necessary.
    }

    @Override
    public void onTagInvalidated() {
        super.onTagInvalidated(); // Must call super!
        this.initialized = false;
    }
}
