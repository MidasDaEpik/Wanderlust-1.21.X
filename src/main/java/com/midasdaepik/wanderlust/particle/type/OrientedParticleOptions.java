package com.midasdaepik.wanderlust.particle.type;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;

public abstract class OrientedParticleOptions implements ParticleOptions {
    protected static final Codec<Float> PITCH;
    protected static final Codec<Float> YAW;
    private final float pitch;
    private final float yaw;

    public OrientedParticleOptions(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    static {
        PITCH = Codec.FLOAT;
        YAW = Codec.FLOAT;
    }
}
