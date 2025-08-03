package com.midasdaepik.wanderlust.particle;

import com.midasdaepik.wanderlust.particle.type.OrientedParticleOptions;
import com.midasdaepik.wanderlust.registries.WLParticles;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class PyroBarrierOptions extends OrientedParticleOptions {
    public static final MapCodec<PyroBarrierOptions> CODEC = RecordCodecBuilder.mapCodec(
            p_341566_ -> p_341566_.group(
                            PITCH.fieldOf("scale").forGetter(OrientedParticleOptions::getPitch),
                            YAW.fieldOf("scale").forGetter(OrientedParticleOptions::getYaw)
                    )
                    .apply(p_341566_, PyroBarrierOptions::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, PyroBarrierOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            PyroBarrierOptions::getPitch,
            ByteBufCodecs.FLOAT,
            PyroBarrierOptions::getYaw,
            PyroBarrierOptions::new
    );

    public PyroBarrierOptions(float pitch, float yaw) {
        super(pitch, yaw);
    }

    @Override
    public ParticleType<PyroBarrierOptions> getType() {
        return WLParticles.PYRO_BARRIER.get();
    }
}
