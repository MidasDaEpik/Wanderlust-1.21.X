package com.midasdaepik.wanderlust.particle.slash;

import com.midasdaepik.wanderlust.registries.WLParticles;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3f;

public class SlowSweepOptions extends SweepOptions {
    public static final MapCodec<SlowSweepOptions> CODEC = RecordCodecBuilder.mapCodec(
            p_341566_ -> p_341566_.group(
                            COLOR.fieldOf("color").forGetter(SweepOptions::getColor),
                            SCALE.fieldOf("scale").forGetter(SweepOptions::getScale),
                            FLIPPED.fieldOf("flipped").forGetter(SweepOptions::getFlipped),
                            PITCH.fieldOf("pitch").forGetter(SweepOptions::getPitch),
                            YAW.fieldOf("yaw").forGetter(SweepOptions::getYaw)
                    )
                    .apply(p_341566_, SlowSweepOptions::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SlowSweepOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            SlowSweepOptions::getColor,
            ByteBufCodecs.FLOAT,
            SlowSweepOptions::getScale,
            ByteBufCodecs.BOOL,
            SlowSweepOptions::getFlipped,
            ByteBufCodecs.FLOAT,
            SlowSweepOptions::getPitch,
            ByteBufCodecs.FLOAT,
            SlowSweepOptions::getYaw,
            SlowSweepOptions::new
    );

    public SlowSweepOptions(Vector3f color, float scale, boolean flipped, float pitch, float yaw) {
        super(color, scale, flipped, pitch, yaw);
    }

    @Override
    public ParticleType<SlowSweepOptions> getType() {
        return WLParticles.SLOW_SWEEP.get();
    }
}
