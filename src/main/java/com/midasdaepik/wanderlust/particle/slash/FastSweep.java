package com.midasdaepik.wanderlust.particle.slash;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class FastSweep extends Sweep {
    protected FastSweep(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSpriteSet, double pXSpeed, double pYSpeed, double pZSpeed, FastSweepOptions pFastSweepOptions) {
        super(pLevel, pX, pY, pZ, pSpriteSet, pXSpeed, pYSpeed, pZSpeed, pFastSweepOptions);
        this.lifetime = 5;
    }

    public static class Provider implements ParticleProvider<FastSweepOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(FastSweepOptions pFastSweepOptions, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new FastSweep(pLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed, pFastSweepOptions);
        }
    }
}
