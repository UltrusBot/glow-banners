package me.ultrusmods.glowingbanners.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BannerItem.class)
public class BannerItemMixin {

    @ModifyArg(method = "appendHoverTextFromBannerBlockEntityTag", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private static <E> E glowBanners$changeChatFormatting(E original, @Local(argsOnly = true) ItemStack stack, @Local int index) {
        BannerGlowComponent data = stack.get(GlowBannersDataComponents.BANNER_GLOW);
        if (data != null && (data.shouldAllGlow() || data.isLayerGlowing(index + 1))) {
            return (E)Component.translatable("glowbanners.block.glowing_banner", original).withStyle(ChatFormatting.GRAY);
        }
        return original;
    }
}
