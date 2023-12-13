package me.ultrusmods.glowingbanners.mixin;


import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BannerItem.class)
public class BannerItemMixin {

    @Inject(method = "appendBannerTooltip", at = @At("HEAD"))
    private static void addGlowingTooltip(ItemStack stack, List<Text> tooltip, CallbackInfo ci) {
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbtFromStack(stack);
        if (nbtCompound != null && nbtCompound.getBoolean("isGlowing")) {
            tooltip.add(Text.translatable("glowbanners.block.glowing").formatted(Formatting.AQUA));
        }
    }

}
