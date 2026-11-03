package com.midasdaepik.wanderlust.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MarkedEffect extends SimpleAnimatedParticle {
    protected MarkedEffect(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSpriteSet) {
        super(pLevel, pX, pY, pZ, pSpriteSet, 0.0F);
        this.setSpriteFromAge(pSpriteSet);

        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;

        this.quadSize = 0.2F;
        this.lifetime = 20;
    }

    @Override
    public void tick() {
        super.tick();
        setAlpha((float) Math.cos(((double) this.age / this.lifetime / 2d) * Math.PI));
        this.oRoll = this.roll;
        this.roll = (float) (this.age / 20f * Math.PI);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType pSimpleParticleType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new MarkedEffect(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprite);
        }
    }
}