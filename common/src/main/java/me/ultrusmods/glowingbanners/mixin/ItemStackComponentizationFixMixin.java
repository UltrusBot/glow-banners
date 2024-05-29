package me.ultrusmods.glowingbanners.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin {
    @ModifyReturnValue(method = "fixBlockEntityTag", at = @At("RETURN"))
    private static <T> Dynamic<T> glowBanners$fixGlowBanners(Dynamic<T> original, ItemStackComponentizationFix.ItemStackData data, Dynamic<T> dynamic, String itemId) {
        if (itemId.equals("minecraft:banner") || itemId.equals("minecraft:shield")) {
            OptionalDynamic<T> isGlowingTag = dynamic.get("isGlowing");
            Optional<Boolean> isGlowing = isGlowingTag.asBoolean().result();
            isGlowing.ifPresent(aBoolean -> data.setComponent(BannerGlowComponent.ID.toString(), dynamic.emptyMap().set("all_glow", dynamic.createBoolean(aBoolean))));
            return original.remove("isGlowing");
        }
        return original;
    }
}
