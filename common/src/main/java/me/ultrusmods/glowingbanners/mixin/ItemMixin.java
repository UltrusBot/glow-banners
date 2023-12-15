package me.ultrusmods.glowingbanners.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ItemMixin {
    @ModifyReturnValue(method = "getName", at = @At("RETURN"))
    private Component glowBanners$updateName(Component original, ItemStack stack) {
        if (stack.getItem() instanceof BannerItem && (IGlowBannersPlatformHelper.INSTANCE.getData(stack).shouldAllGlow() || IGlowBannersPlatformHelper.INSTANCE.getData(stack).isLayerGlowing(0)))
            return Component.translatable("glowbanners.block.glowing_banner", original);

        return original;
    }
}
