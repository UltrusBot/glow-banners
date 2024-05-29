package me.ultrusmods.glowingbanners.component;

import com.google.common.base.Objects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.ultrusmods.glowingbanners.GlowBannersMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class BannerGlowComponent {
    public static final BannerGlowComponent EMPTY = new BannerGlowComponent();
    public static final ResourceLocation ID = GlowBannersMod.asResource("banner_glow");
    public static final Codec<BannerGlowComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.BOOL.optionalFieldOf("all_glow", false).forGetter(BannerGlowComponent::shouldAllGlow),
            Codec.list(Codec.INT).optionalFieldOf("glowing_layers", List.of()).xmap(Collections::unmodifiableCollection, List::copyOf).forGetter(BannerGlowComponent::getGlowingLayers)
    ).apply(inst, BannerGlowComponent::new));
    public static final StreamCodec<FriendlyByteBuf, BannerGlowComponent> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, BannerGlowComponent::shouldAllGlow, ByteBufCodecs.INT.apply(ByteBufCodecs.list()).map(Collections::unmodifiableCollection, List::copyOf), BannerGlowComponent::getGlowingLayers, BannerGlowComponent::new);

    private boolean allGlow;
    private final SortedSet<Integer> glowingLayers = new TreeSet<>(Integer::compare);

    public BannerGlowComponent() {
        this(false, List.of());
    }

    public BannerGlowComponent(boolean allGlow, Collection<Integer> glowingLayers) {
        this.allGlow = allGlow;
        this.glowingLayers.addAll(glowingLayers);
    }

    public boolean shouldAllGlow() {
        return this.allGlow;
    }

    public void setAllGlow(boolean value) {
        this.allGlow = value;
    }

    public boolean isLayerGlowing(int layerIndex) {
        return glowingLayers.contains(layerIndex);
    }

    public void addGlowToLayer(int layerIndex) {
        this.glowingLayers.add(layerIndex);
    }

    public void removeGlowFromLayer(int layerIndex) {
        this.glowingLayers.remove(layerIndex);
    }

    public void clearGlowingLayers() {
        this.glowingLayers.clear();
    }

    public Collection<Integer> getGlowingLayers() {
        return Collections.unmodifiableCollection(glowingLayers);
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BannerGlowComponent other))
            return false;
        return other.allGlow == allGlow && other.glowingLayers.equals(glowingLayers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(allGlow, glowingLayers);
    }
}
