package com.midasdaepik.wanderlust.particle.slash;

import com.midasdaepik.wanderlust.registries.WLParticles;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3f;

public class FastSweepOptions extends SweepOptions {
    public static final MapCodec<FastSweepOptions> CODEC = RecordCodecBuilder.mapCodec(
            p_341566_ -> p_341566_.group(
                            COLOR.fieldOf("color").forGetter(SweepOptions::getColor),
                            SCALE.fieldOf("scale").forGetter(SweepOptions::getScale),
                            FLIPPED.fieldOf("flipped").forGetter(SweepOptions::getFlipped),
                            PITCH.fieldOf("pitch").forGetter(SweepOptions::getPitch),
                            YAW.fieldOf("yaw").forGetter(SweepOptions::getYaw)
                    )
                    .apply(p_341566_, FastSweepOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FastSweepOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            FastSweepOptions::getColor,
            ByteBufCodecs.FLOAT,
            FastSweepOptions::getScale,
            ByteBufCodecs.BOOL,
            FastSweepOptions::getFlipped,
            ByteBufCodecs.FLOAT,
            FastSweepOptions::getPitch,
            ByteBufCodecs.FLOAT,
            FastSweepOptions::getYaw,
            FastSweepOptions::new
    );

    public FastSweepOptions(Vector3f color, float scale, boolean flipped, float pitch, float yaw) {
        super(color, scale, flipped, pitch, yaw);
    }

    @Override
    public ParticleType<FastSweepOptions> getType() {
        return WLParticles.FAST_SWEEP.get();
    }
}
