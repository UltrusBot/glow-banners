package me.ultrusmods.glowingbanners.platform;

import com.google.auto.service.AutoService;
import me.ultrusmods.glowingbanners.GlowBannersModFabric;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

@AutoService(IGlowBannersPlatformHelper.class)
public class FabricGlowBannersPlatformHelper implements IGlowBannersPlatformHelper {
    public IBannerGlowData getData(BannerBlockEntity blockEntity) {
        return GlowBannersModFabric.BANNER_GLOW.get(blockEntity);
    }

    public IBannerGlowData getData(ItemStack stack) {
        return GlowBannersModFabric.BANNER_GLOW_ITEM.find(stack, null);
    }
}
