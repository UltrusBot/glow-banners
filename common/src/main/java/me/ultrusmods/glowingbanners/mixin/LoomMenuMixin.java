package me.ultrusmods.glowingbanners.mixin;

import com.google.common.collect.ImmutableList;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(LoomMenu.class)
public abstract class LoomMenuMixin extends AbstractContainerMenu {
    @Shadow @Final
    Slot dyeSlot;

    @Shadow @Final
    Slot bannerSlot;

    @Shadow @Final private Slot resultSlot;

    @Shadow private List<Holder<BannerPattern>> selectablePatterns;

    @Shadow @Final
    DataSlot selectedBannerPatternIndex;

    protected LoomMenuMixin(@Nullable MenuType<?> $$0, int $$1) {
        super($$0, $$1);
    }

    @Inject(method = "slotsChanged", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void glowBanners$addOrRemoveGlowToBannerInLoom(Container container, CallbackInfo ci, ItemStack banner, ItemStack dye) {
        boolean hasGlowInkSac = dye.is(Items.GLOW_INK_SAC);
        boolean hasInkSac = dye.is(Items.INK_SAC);
        if (hasGlowInkSac || hasInkSac) {
            this.selectablePatterns = ImmutableList.of();
            this.selectedBannerPatternIndex.set(-1);
            ItemStack result = this.bannerSlot.getItem().copy();
            int lastLayer = BannerBlockEntity.getItemPatterns(result) == null ? 0 : BannerBlockEntity.getItemPatterns(result).size();

            boolean isOriginalLastLayerGlowing = IGlowBannersPlatformHelper.INSTANCE.getData(this.bannerSlot.getItem()).shouldAllGlow() || IGlowBannersPlatformHelper.INSTANCE.getData(this.bannerSlot.getItem()).isLayerGlowing(lastLayer);
            if (hasGlowInkSac && isOriginalLastLayerGlowing || hasInkSac && !isOriginalLastLayerGlowing) {
                ci.cancel();
                return;
            }

            IBannerGlowData resultData = IGlowBannersPlatformHelper.INSTANCE.getData(result);
            if (hasGlowInkSac)
                resultData.addGlowToLayer(lastLayer);
            else
                resultData.removeGlowFromLayer(lastLayer);

            this.resultSlot.set(result);

            this.broadcastChanges();
            ci.cancel();
        }
    }

    @Inject(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void glowBanners$allowQuickMovingOfSacs(Player player, int slotIndex, CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack, Slot slot, ItemStack itemStack2) {
        if (itemStack2.is(Items.GLOW_INK_SAC) || itemStack2.is(Items.INK_SAC)) {
            if (!this.moveItemStackTo(itemStack2, this.dyeSlot.index, this.dyeSlot.index + 1, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
}
