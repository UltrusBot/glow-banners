package me.ultrusmods.glowingbanners.attachment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class BannerGlowApi implements IBannerGlowData {
    private final ItemStack provider;

    public BannerGlowApi(ItemStack provider) {
        this.provider = provider;
    }

    @Override
    public boolean shouldAllGlow() {
        return this.getCardinalComponentsBlockEntityTag().getBoolean("all_glow");
    }

    @Override
    public void setAllGlow(boolean value) {
        this.getCardinalComponentsBlockEntityTag().putBoolean("all_glow", value);
    }

    @Override
    public boolean isLayerGlowing(int layerIndex) {
        ListTag listTag = this.getCardinalComponentsBlockEntityTag().getList("glowing_layers", ListTag.TAG_INT);
        return listTag.contains(IntTag.valueOf(layerIndex));
    }

    @Override
    public void addGlowToLayer(int layerIndex) {
        ListTag listTag = this.getCardinalComponentsBlockEntityTag().getList("glowing_layers", ListTag.TAG_INT).copy();
        listTag.add(IntTag.valueOf(layerIndex));
        this.getCardinalComponentsBlockEntityTag().put("glowing_layers", listTag);
    }

    @Override
    public void removeGlowFromLayer(int layerIndex) {
        ListTag listTag = this.getCardinalComponentsBlockEntityTag().getList("glowing_layers", ListTag.TAG_INT).copy();
        listTag.remove(IntTag.valueOf(layerIndex));
        this.getCardinalComponentsBlockEntityTag().put("glowing_layers", listTag);
    }

    @Override
    public void clearGlowingLayers() {
        this.getCardinalComponentsBlockEntityTag().remove("glowing_layers");
    }

    @Override
    public Collection<Integer> getGlowingLayers() {
        return this.getCardinalComponentsBlockEntityTag().getList("glowing_layers", ListTag.TAG_INT).stream().map(tag -> ((IntTag)tag).getAsInt()).toList();
    }

    @Override
    public void setGlowingLayers(Collection<Integer> value) {
        this.clearGlowingLayers();
        for (int val : value.stream().sorted(Integer::compareTo).toList()) {
            this.addGlowToLayer(val);
        }
    }

    private CompoundTag getCardinalComponentsBlockEntityTag() {
        if (BlockItem.getBlockEntityData(this.provider) == null || !BlockItem.getBlockEntityData(this.provider).contains("cardinal_components")) {
            CompoundTag ccaTag = new CompoundTag();
            this.provider.getOrCreateTagElement("BlockEntityTag").put("cardinal_components", ccaTag);
        }
        if (!BlockItem.getBlockEntityData(this.provider).getCompound("cardinal_components").contains(IBannerGlowData.ID.toString())) {
            BlockItem.getBlockEntityData(this.provider).getCompound("cardinal_components").put(IBannerGlowData.ID.toString(), new CompoundTag());
        }
        return BlockItem.getBlockEntityData(this.provider).getCompound("cardinal_components").getCompound(IBannerGlowData.ID.toString());
    }

    @Override
    public void sync(@Nullable BannerBlockEntity blockEntity) {
        // No implementation.
    }
}
