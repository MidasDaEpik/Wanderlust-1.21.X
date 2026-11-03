package com.midasdaepik.wanderlust.particle.slash;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class RoundaboutSweep extends Sweep {
    protected RoundaboutSweep(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSpriteSet, double pXSpeed, double pYSpeed, double pZSpeed, RoundaboutSweepOptions pRoundaboutSweepOptions) {
        super(pLevel, pX, pY, pZ, pSpriteSet, pXSpeed, pYSpeed, pZSpeed, pRoundaboutSweepOptions);
        this.lifetime = 7;
    }

    public static class Provider implements ParticleProvider<RoundaboutSweepOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(RoundaboutSweepOptions pRoundaboutSweepOptions, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new RoundaboutSweep(pLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed, pRoundaboutSweepOptions);
        }
    }
}
