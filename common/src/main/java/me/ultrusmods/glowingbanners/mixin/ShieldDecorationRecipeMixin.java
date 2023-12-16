package me.ultrusmods.glowingbanners.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {
    @ModifyReturnValue(
            method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "RETURN", ordinal = 1)
    )
    private ItemStack glowBanners$assembleGlowingOnShield(ItemStack original, @Local(ordinal = 0) ItemStack bannerStack) {
        IBannerGlowData bannerData = IGlowBannersPlatformHelper.INSTANCE.getData(bannerStack);
        if (bannerData != null) {
            IGlowBannersPlatformHelper.INSTANCE.getData(original).setFromOther(bannerData);
        }
        return original;
    }
}
