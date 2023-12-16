package glowingbanners.attachment;

import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.neoforged.neoforge.attachment.AttachmentHolder;

import javax.annotation.Nullable;
import java.util.Collection;

public class BannerGlowItemCapability implements IBannerGlowData {
    private final ItemStack provider;

    public BannerGlowItemCapability(ItemStack provider) {
        this.provider = provider;
    }

    @Override
    public boolean shouldAllGlow() {
        return this.getAttachmentsBlockEntityTag().getBoolean("all_glow");
    }

    @Override
    public void setAllGlow(boolean value) {
        this.getAttachmentsBlockEntityTag().putBoolean("all_glow", value);
    }

    @Override
    public boolean isLayerGlowing(int layerIndex) {
        ListTag listTag = this.getAttachmentsBlockEntityTag().getList("glowing_layers", ListTag.TAG_INT);
        return listTag.contains(IntTag.valueOf(layerIndex));
    }

    @Override
    public void addGlowToLayer(int layerIndex) {
        ListTag listTag = this.getAttachmentsBlockEntityTag().getList("glowing_layers", ListTag.TAG_INT).copy();
        listTag.add(IntTag.valueOf(layerIndex));
        this.getAttachmentsBlockEntityTag().put("glowing_layers", listTag);
    }

    @Override
    public void removeGlowFromLayer(int layerIndex) {
        ListTag listTag = this.getAttachmentsBlockEntityTag().getList("glowing_layers", ListTag.TAG_INT).copy();
        listTag.remove(IntTag.valueOf(layerIndex));
        this.getAttachmentsBlockEntityTag().put("glowing_layers", listTag);
    }

    @Override
    public void clearGlowingLayers() {
        this.getAttachmentsBlockEntityTag().remove("glowing_layers");
    }

    @Override
    public Collection<Integer> getGlowingLayers() {
        return this.getAttachmentsBlockEntityTag().getList("glowing_layers", ListTag.TAG_INT).stream().map(tag -> ((IntTag)tag).getAsInt()).toList();
    }

    @Override
    public void setGlowingLayers(Collection<Integer> value) {
        this.clearGlowingLayers();
        for (int val : value.stream().sorted(Integer::compareTo).toList()) {
            this.addGlowToLayer(val);
        }
    }

    private CompoundTag getAttachmentsBlockEntityTag() {
        if (BlockItem.getBlockEntityData(this.provider) == null || !BlockItem.getBlockEntityData(this.provider).contains(AttachmentHolder.ATTACHMENTS_NBT_KEY)) {
            CompoundTag ccaTag = new CompoundTag();
            this.provider.getOrCreateTagElement("BlockEntityTag").put(AttachmentHolder.ATTACHMENTS_NBT_KEY, ccaTag);
        }
        if (!BlockItem.getBlockEntityData(this.provider).getCompound(AttachmentHolder.ATTACHMENTS_NBT_KEY).contains(IBannerGlowData.ID.toString())) {
            BlockItem.getBlockEntityData(this.provider).getCompound(AttachmentHolder.ATTACHMENTS_NBT_KEY).put(IBannerGlowData.ID.toString(), new CompoundTag());
        }
        return BlockItem.getBlockEntityData(this.provider).getCompound(AttachmentHolder.ATTACHMENTS_NBT_KEY).getCompound(IBannerGlowData.ID.toString());
    }

    @Override
    public void sync(@Nullable BannerBlockEntity blockEntity) {
        // Not necessary to implement
    }
}
