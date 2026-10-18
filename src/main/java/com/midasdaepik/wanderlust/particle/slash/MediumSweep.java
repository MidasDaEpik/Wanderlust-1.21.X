package com.midasdaepik.wanderlust.particle.slash;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class MediumSweep extends Sweep {
    protected MediumSweep(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSpriteSet, double pXSpeed, double pYSpeed, double pZSpeed, MediumSweepOptions pMediumSweepOptions) {
        super(pLevel, pX, pY, pZ, pSpriteSet, pXSpeed, pYSpeed, pZSpeed, pMediumSweepOptions);
        this.lifetime = 5;
    }

    public static class Provider implements ParticleProvider<MediumSweepOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(MediumSweepOptions pMediumSweepOptions, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new MediumSweep(pLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed, pMediumSweepOptions);
        }
    }
}