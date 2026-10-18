package com.midasdaepik.wanderlust.particle.slash;

import com.midasdaepik.wanderlust.particle.type.OrientedParticleOptions;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public abstract class SweepOptions extends OrientedParticleOptions {
    protected static final Codec<Vector3f> COLOR;
    protected static final Codec<Float> SCALE;
    protected static final Codec<Boolean> FLIPPED;
    private final Vector3f color;
    private final float scale;
    private final boolean flipped;

    public SweepOptions(Vector3f color, float scale, boolean flipped, float pitch, float yaw) {
        super(pitch, yaw);
        this.color = color;
        this.scale = scale;
        this.flipped = flipped;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }

    public boolean getFlipped() {
        return this.flipped;
    }

    static {
        COLOR = ExtraCodecs.VECTOR3F;
        SCALE = Codec.FLOAT;
        FLIPPED = Codec.BOOL;
    }
}
