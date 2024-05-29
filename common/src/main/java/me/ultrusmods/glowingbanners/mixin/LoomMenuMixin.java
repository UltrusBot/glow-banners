package me.ultrusmods.glowingbanners.mixin;

import com.google.common.collect.ImmutableList;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            int lastLayer = result.get(DataComponents.BANNER_PATTERNS) == null ? 0 : result.get(DataComponents.BANNER_PATTERNS).layers().size();

            BannerGlowComponent component = result.getOrDefault(GlowBannersDataComponents.BANNER_GLOW, new BannerGlowComponent());
            boolean isOriginalLastLayerGlowing = component.shouldAllGlow() || component.isLayerGlowing(lastLayer);
            if (hasGlowInkSac && isOriginalLastLayerGlowing || hasInkSac && !isOriginalLastLayerGlowing) {
                ci.cancel();
                return;
            }

            if (hasGlowInkSac)
                component.addGlowToLayer(lastLayer);
            else if (component.shouldAllGlow()) {
                component.setAllGlow(false);
                component.clearGlowingLayers();
                for (int i = 0; i < Math.max(lastLayer - 1, 0); ++i) {
                    component.addGlowToLayer(i);
                }
            } else
                component.removeGlowFromLayer(lastLayer);

            if (!component.isEmpty())
                result.set(GlowBannersDataComponents.BANNER_GLOW, component);
            else
                result.remove(GlowBannersDataComponents.BANNER_GLOW);
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
