package me.ultrusmods.glowingbanners.platform;

import com.google.auto.service.AutoService;
import me.ultrusmods.glowingbanners.attachment.GlowBannersComponents;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

@AutoService(IGlowBannersPlatformHelper.class)
public class FabricGlowBannersPlatformHelper implements IGlowBannersPlatformHelper {
    public IBannerGlowData getData(BannerBlockEntity blockEntity) {
        return GlowBannersComponents.BANNER_GLOW.get(blockEntity);
    }

    public IBannerGlowData getData(ItemStack stack) {
        if (!GlowBannersComponents.BANNER_GLOW_ITEM.isProvidedBy(stack))
            return null;

        return GlowBannersComponents.BANNER_GLOW_ITEM.get(stack);
    }
}
