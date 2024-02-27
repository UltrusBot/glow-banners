package me.ultrusmods.glowingbanners.platform.services;

import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.ServiceUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

public interface IGlowBannersPlatformHelper {
    IGlowBannersPlatformHelper INSTANCE = ServiceUtil.load(IGlowBannersPlatformHelper.class);

    IBannerGlowData getData(BannerBlockEntity blockEntity);
    IBannerGlowData getData(ItemStack stack);
}
