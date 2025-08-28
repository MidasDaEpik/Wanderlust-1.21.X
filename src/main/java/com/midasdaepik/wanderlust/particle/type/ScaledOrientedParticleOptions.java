package com.midasdaepik.wanderlust.particle.type;

import com.mojang.serialization.Codec;

public abstract class ScaledOrientedParticleOptions extends OrientedParticleOptions {
    protected static final Codec<Float> START_SCALE;
    protected static final Codec<Float> END_SCALE;
    private final float start_scale;
    private final float end_scale;

    public ScaledOrientedParticleOptions(float start_scale, float end_scale, int lifetime, float pitch, float yaw) {
        super(pitch, yaw);
        this.start_scale = start_scale;
        this.end_scale = end_scale;
    }

    public float getStartScale() {
        return this.start_scale;
    }

    public float getEndScale() {
        return this.end_scale;
    }

    static {
        START_SCALE = Codec.FLOAT;
        END_SCALE = Codec.FLOAT;
    }
}
