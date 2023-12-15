package me.ultrusmods.glowingbanners.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BannerItem.class)
public class BannerItemMixin {

    @Unique
    private static IBannerGlowData glowBanners$cappturedGlowData;
    @Unique
    private static int glowBanners$capturedIndex;

    @Inject(method = "appendHoverTextFromBannerBlockEntityTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/ListTag;getCompound(I)Lnet/minecraft/nbt/CompoundTag;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void glowBanners$changeChatFormatting(ItemStack stack, List<Component> components, CallbackInfo ci, CompoundTag blockEntityDataTag, ListTag patternList, int index) {
        glowBanners$cappturedGlowData = IGlowBannersPlatformHelper.INSTANCE.getData(stack);
        glowBanners$capturedIndex = index + 1;
    }

    @ModifyArg(method = { "method_43707", "lambda$appendHoverTextFromBannerBlockEntityTag$1" }, at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private static <E> E glowBanners$changeChatFormatting(E original, @Share("glowData") LocalRef<IBannerGlowData> dataRef) {
        if (glowBanners$cappturedGlowData.shouldAllGlow() || glowBanners$cappturedGlowData.isLayerGlowing(glowBanners$capturedIndex))
            return (E)Component.translatable("glowbanners.block.glowing_banner", original).withStyle(ChatFormatting.GRAY);

        return original;
    }
}
