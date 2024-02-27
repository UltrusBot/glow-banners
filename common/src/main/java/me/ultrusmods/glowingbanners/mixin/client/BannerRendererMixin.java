package me.ultrusmods.glowingbanners.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BannerRenderer.class)
public class BannerRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLjava/util/List;)V")
    )
    public void glowBanners$changeLight(BannerBlockEntity bannerBlockEntity, float f, PoseStack poseStack, MultiBufferSource bufferSource, int i, int j, CallbackInfo ci) {
        GlowBannersMod.BANNER_RENDERER_CONTEXT = IGlowBannersPlatformHelper.INSTANCE.getData(bannerBlockEntity);
    }

    @Unique
    private static int glowBanners$capturedLayerIndex;

    @Inject(
            method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLjava/util/List;Z)V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void glowBanners$captureIndex(PoseStack stack, MultiBufferSource bufferSource, int light, int overlay, ModelPart part, Material material, boolean bl, List<Pair<Holder<BannerPattern>, DyeColor>> patterns, boolean glint, CallbackInfo ci, int index) {
        glowBanners$capturedLayerIndex = index;
    }


    @ModifyArg(
            method = { "method_43789", "lambda$renderPatterns$1" },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"),
            index = 2
    )
    private static int glowBanners$changeLightForLayer(int light) {
        if (GlowBannersMod.BANNER_RENDERER_CONTEXT != null && (GlowBannersMod.BANNER_RENDERER_CONTEXT.shouldAllGlow() || GlowBannersMod.BANNER_RENDERER_CONTEXT.isLayerGlowing(glowBanners$capturedLayerIndex))) {
            return 15728880;
        }
        return light;
    }

    @Inject(
            method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLjava/util/List;)V",
            at = @At("TAIL")
    )
    private static void glowBanners$resetBannerRendererContext(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ModelPart modelPart, Material material, boolean bl, List<Pair<Holder<BannerPattern>, DyeColor>> patterns, CallbackInfo ci) {
        GlowBannersMod.BANNER_RENDERER_CONTEXT = null;
    }
}
