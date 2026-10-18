package com.midasdaepik.wanderlust.particle.slash;

import com.midasdaepik.wanderlust.registries.WLParticles;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3f;

public class MediumSweepOptions extends SweepOptions {
    public static final MapCodec<MediumSweepOptions> CODEC = RecordCodecBuilder.mapCodec(
            p_341566_ -> p_341566_.group(
                            COLOR.fieldOf("color").forGetter(SweepOptions::getColor),
                            SCALE.fieldOf("scale").forGetter(SweepOptions::getScale),
                            FLIPPED.fieldOf("flipped").forGetter(SweepOptions::getFlipped),
                            PITCH.fieldOf("pitch").forGetter(SweepOptions::getPitch),
                            YAW.fieldOf("yaw").forGetter(SweepOptions::getYaw)
                    )
                    .apply(p_341566_, MediumSweepOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MediumSweepOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            MediumSweepOptions::getColor,
            ByteBufCodecs.FLOAT,
            MediumSweepOptions::getScale,
            ByteBufCodecs.BOOL,
            MediumSweepOptions::getFlipped,
            ByteBufCodecs.FLOAT,
            MediumSweepOptions::getPitch,
            ByteBufCodecs.FLOAT,
            MediumSweepOptions::getYaw,
            MediumSweepOptions::new
    );

    public MediumSweepOptions(Vector3f color, float scale, boolean flipped, float pitch, float yaw) {
        super(color, scale, flipped, pitch, yaw);
    }

    @Override
    public ParticleType<MediumSweepOptions> getType() {
        return WLParticles.MEDIUM_SWEEP.get();
    }
}
