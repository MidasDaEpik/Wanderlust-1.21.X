package com.midasdaepik.wanderlust.particle.slash;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class SlowSweep extends Sweep {
    protected SlowSweep(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSpriteSet, double pXSpeed, double pYSpeed, double pZSpeed, SlowSweepOptions pSlowSweepOptions) {
        super(pLevel, pX, pY, pZ, pSpriteSet, pXSpeed, pYSpeed, pZSpeed, pSlowSweepOptions);
        this.lifetime = 5;
    }

    public static class Provider implements ParticleProvider<SlowSweepOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SlowSweepOptions pSlowSweepOptions, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SlowSweep(pLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed, pSlowSweepOptions);
        }
    }
}
