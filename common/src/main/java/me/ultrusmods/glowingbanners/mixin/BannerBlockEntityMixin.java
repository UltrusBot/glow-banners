package me.ultrusmods.glowingbanners.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin {
    @Inject(method = "load", at = @At("TAIL"))
    private void glowBanners$convertGlowingNbt(CompoundTag tag, CallbackInfo ci) {
        if (tag.getBoolean("isGlowing")) {
            IBannerGlowData data = IGlowBannersPlatformHelper.INSTANCE.getData((BannerBlockEntity)(Object)this);
            data.setAllGlow(true);
            data.sync((BannerBlockEntity)(Object)this);
        }
    }

    @ModifyReturnValue(method = "getItem", at = @At("RETURN"))
    private ItemStack glowBanners$setGlowDataFromStack(ItemStack original) {
        IBannerGlowData prevData = IGlowBannersPlatformHelper.INSTANCE.getData((BannerBlockEntity)(Object)this);
        IBannerGlowData glowData = IGlowBannersPlatformHelper.INSTANCE.getData(original);
        glowData.setFromOther(prevData);
        return original;
    }
}
