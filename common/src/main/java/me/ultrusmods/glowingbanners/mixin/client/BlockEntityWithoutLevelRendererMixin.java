package me.ultrusmods.glowingbanners.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
    @Inject(method = "renderByItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Z)V"))
    private void glowBanners$storeShieldContext(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, CallbackInfo ci) {
        GlowBannersMod.BANNER_RENDERER_CONTEXT = stack.get(GlowBannersDataComponents.BANNER_GLOW);
    }
}
