package me.ultrusmods.glowingbanners.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

    @Unique
    private ItemStack glowingBannerCheckStack;

    @Inject(method = "render", at = @At("HEAD"))
    public void captureItemStack$GlowBanners(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        this.glowingBannerCheckStack = stack;
    }
    @ModifyArg(method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V"),
            index = 2)
    public int changeShieldPatternLight$GlowBanners(int lightArg) {
        if (glowingBannerCheckStack == null || !(glowingBannerCheckStack.getItem() instanceof ShieldItem) ) return lightArg;
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbtFromStack(glowingBannerCheckStack);
        if (nbtCompound != null && nbtCompound.getBoolean("isGlowing")) {
            return 15728880;
        }
        return lightArg;
    }
}
