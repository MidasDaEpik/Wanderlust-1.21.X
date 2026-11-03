package com.midasdaepik.wanderlust.particle;

import com.midasdaepik.wanderlust.particle.type.ScaledOrientedParticleOptions;
import com.midasdaepik.wanderlust.registries.WLParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class CubeOptions extends ScaledOrientedParticleOptions {
    public static final MapCodec<CubeOptions> CODEC = RecordCodecBuilder.mapCodec(
            p_341566_ -> p_341566_.group(
                            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(p_970401_ -> p_970401_.color),
                            Codec.INT.fieldOf("lifetime").forGetter(p_487622_ -> p_487622_.lifetime),
                            START_SCALE.fieldOf("start_scale").forGetter(ScaledOrientedParticleOptions::getStartScale),
                            END_SCALE.fieldOf("end_scale").forGetter(ScaledOrientedParticleOptions::getEndScale),
                            PITCH.fieldOf("pitch").forGetter(ScaledOrientedParticleOptions::getPitch),
                            YAW.fieldOf("yaw").forGetter(ScaledOrientedParticleOptions::getYaw)
                    )
                    .apply(p_341566_, CubeOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CubeOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            p_119803_ -> p_119803_.color,
            ByteBufCodecs.INT,
            p_115522_ -> p_115522_.lifetime,
            ByteBufCodecs.FLOAT,
            CubeOptions::getStartScale,
            ByteBufCodecs.FLOAT,
            CubeOptions::getEndScale,
            ByteBufCodecs.FLOAT,
            CubeOptions::getPitch,
            ByteBufCodecs.FLOAT,
            CubeOptions::getYaw,
            CubeOptions::new
    );

    private final Vector3f color;
    private final int lifetime;

    public CubeOptions(Vector3f color, int lifetime, float start_scale, float end_scale, float pitch, float yaw) {
        super(start_scale, end_scale, pitch, yaw);
        this.color = color;
        this.lifetime = lifetime;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    @Override
    public ParticleType<CubeOptions> getType() {
        return WLParticles.CUBE.get();
    }
}
