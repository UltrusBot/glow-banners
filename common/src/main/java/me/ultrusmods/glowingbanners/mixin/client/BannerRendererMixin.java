package me.ultrusmods.glowingbanners.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerRenderer.class)
public class BannerRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;)V")
    )
    public void glowBanners$storeContext(BannerBlockEntity bannerBlockEntity, float f, PoseStack poseStack, MultiBufferSource bufferSource, int i, int j, CallbackInfo ci) {
        GlowBannersMod.BANNER_RENDERER_CONTEXT = GlowBannersMod.getHelper().getData(bannerBlockEntity);
    }

    @Inject(
            method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;)V", shift = At.Shift.AFTER)
    )
    public void glowBanners$unstoreContext(BannerBlockEntity bannerBlockEntity, float f, PoseStack poseStack, MultiBufferSource bufferSource, int i, int j, CallbackInfo ci) {
        GlowBannersMod.BANNER_RENDERER_CONTEXT = null;
    }

    @ModifyArg(
            method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderPatternLayer(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;Lnet/minecraft/world/item/DyeColor;)V", ordinal = 0),
            index = 2
    )
    private static int glowBanners$changeLightForBase(int light) {
        if (GlowBannersMod.BANNER_RENDERER_CONTEXT != null && (GlowBannersMod.BANNER_RENDERER_CONTEXT.shouldAllGlow() || GlowBannersMod.BANNER_RENDERER_CONTEXT.isLayerGlowing(0))) {
            return 15728880;
        }
        return light;
    }

    @ModifyArg(
            method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderPatternLayer(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;Lnet/minecraft/world/item/DyeColor;)V", ordinal = 1),
            index = 2
    )
    private static int glowBanners$changeLightForLayer(int light, @Local(ordinal = 2) int index) {
        if (GlowBannersMod.BANNER_RENDERER_CONTEXT != null && (GlowBannersMod.BANNER_RENDERER_CONTEXT.shouldAllGlow() || GlowBannersMod.BANNER_RENDERER_CONTEXT.isLayerGlowing(index + 1))) {
            return 15728880;
        }
        return light;
    }
}
