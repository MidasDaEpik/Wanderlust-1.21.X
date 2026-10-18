package com.midasdaepik.wanderlust.particle.slash;

import com.midasdaepik.wanderlust.particle.type.OrientedParticleOptions;
import com.midasdaepik.wanderlust.registries.WLParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class CritOptions extends OrientedParticleOptions {
    public static final MapCodec<CritOptions> CODEC = RecordCodecBuilder.mapCodec(
            p_341566_ -> p_341566_.group(
                            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(p_970401_ -> p_970401_.color),
                            Codec.FLOAT.fieldOf("scale").forGetter(p_487622_ -> p_487622_.scale),
                            PITCH.fieldOf("pitch").forGetter(OrientedParticleOptions::getPitch),
                            YAW.fieldOf("yaw").forGetter(OrientedParticleOptions::getYaw)
                    )
                    .apply(p_341566_, CritOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CritOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            p_119803_ -> p_119803_.color,
            ByteBufCodecs.FLOAT,
            p_115522_ -> p_115522_.scale,
            ByteBufCodecs.FLOAT,
            CritOptions::getPitch,
            ByteBufCodecs.FLOAT,
            CritOptions::getYaw,
            CritOptions::new
    );

    private final Vector3f color;
    private final float scale;

    public CritOptions(Vector3f color, float scale, float pitch, float yaw) {
        super(pitch, yaw);
        this.color = color;
        this.scale = scale;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }

    @Override
    public ParticleType<CritOptions> getType() {
        return WLParticles.CRIT.get();
    }
}
