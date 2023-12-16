package glowingbanners.platform;

import com.google.auto.service.AutoService;
import glowingbanners.GlowBannersModNeoForge;
import me.ultrusmods.glowingbanners.attachment.IBannerGlowData;
import me.ultrusmods.glowingbanners.platform.services.IGlowBannersPlatformHelper;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

@AutoService(IGlowBannersPlatformHelper.class)
public class NeoForgeGlowBannersPlatformHelper implements IGlowBannersPlatformHelper {
    public IBannerGlowData getData(BannerBlockEntity blockEntity) {
        return blockEntity.getData(GlowBannersModNeoForge.BANNER_GLOW_BLOCK);
    }

    public IBannerGlowData getData(ItemStack stack) {
        return stack.getCapability(GlowBannersModNeoForge.BANNER_GLOW_ITEM);
    }
}
