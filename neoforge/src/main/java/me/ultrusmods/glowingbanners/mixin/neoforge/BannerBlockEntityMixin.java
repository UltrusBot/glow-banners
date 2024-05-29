package me.ultrusmods.glowingbanners.mixin.neoforge;

import me.ultrusmods.glowingbanners.registry.GlowBannersAttachmentTypes;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin extends BlockEntity {
    public BannerBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void glowBanners$convertOldGlowingNbt(CompoundTag tag, HolderLookup.Provider provider, CallbackInfo ci) {
        if (!hasData(GlowBannersAttachmentTypes.BANNER_GLOW) && tag.contains("isGlowing", Tag.TAG_BYTE) && tag.getBoolean("isGlowing")) {
            tag.remove("isGlowing");
            setData(GlowBannersAttachmentTypes.BANNER_GLOW, new BannerGlowComponent(true, List.of()));
        }
    }
}
