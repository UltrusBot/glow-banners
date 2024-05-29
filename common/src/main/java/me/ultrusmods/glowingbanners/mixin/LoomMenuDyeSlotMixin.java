package me.ultrusmods.glowingbanners.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/world/inventory/LoomMenu$4")
public class LoomMenuDyeSlotMixin {
    @ModifyReturnValue(
            method = "mayPlace",
            at = @At("RETURN")
    )
    private boolean glowBanners$allowGlowInkSacs(boolean original, ItemStack stack) {
        return original || stack.is(Items.GLOW_INK_SAC) || stack.is(Items.INK_SAC);
    }
}
