package me.ultrusmods.glowingbanners.platform.services;

import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public interface IGlowBannersPlatformHelper {
    @Nullable
    BannerGlowComponent getData(BlockEntity blockEntity);

    BannerGlowComponent getOrCreateData(BlockEntity blockEntity);

    boolean hasData(BlockEntity blockEntity);

    BannerGlowComponent setData(BlockEntity blockEntity, BannerGlowComponent component);

    BannerGlowComponent removeData(BlockEntity blockEntity);

    void syncBlockEntity(BlockEntity blockEntity);
}
