package me.ultrusmods.glowingbanners.mixin;

import me.ultrusmods.glowingbanners.GlowBannersMod;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin extends BlockEntity {
    @Shadow protected abstract void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider);

    public BannerBlockEntityMixin(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    @Inject(method = "applyImplicitComponents", at = @At("TAIL"))
    private void glowBanners$applyGlowComponent(DataComponentInput input, CallbackInfo ci) {
        BannerGlowComponent component = input.get(GlowBannersDataComponents.BANNER_GLOW);
        if (component == null || component.isEmpty()) {
            return;
        }
        GlowBannersMod.getHelper().setData(this, component);
    }

    @Inject(method = "collectImplicitComponents", at = @At("TAIL"))
    private void glowBanners$applyGlowComponent(DataComponentMap.Builder builder, CallbackInfo ci) {
        BannerGlowComponent attachment = GlowBannersMod.getHelper().getData(this);
        if (attachment == null || attachment.isEmpty())
            return;
        builder.set(GlowBannersDataComponents.BANNER_GLOW, attachment);
    }
}
