package me.ultrusmods.glowingbanners.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {
    @ModifyReturnValue(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN", ordinal = 1))
    private ItemStack glowBanners$assembleWithGlow(ItemStack original, @Local(ordinal = 0) ItemStack bannerStack) {
        if (bannerStack.has(GlowBannersDataComponents.BANNER_GLOW))
            original.set(GlowBannersDataComponents.BANNER_GLOW, bannerStack.get(GlowBannersDataComponents.BANNER_GLOW));
        return original;
    }
}
