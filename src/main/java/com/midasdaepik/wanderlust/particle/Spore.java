package com.midasdaepik.wanderlust.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Spore extends TextureSheetParticle {
    protected Spore(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSpriteSet) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.pickSprite(pSpriteSet);

        this.quadSize = 0.4F * (this.random.nextFloat() * 0.4F + 0.4F);
        this.lifetime = (int)((double) 16.0F / (Math.random() * 0.8 + 0.2));

        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }

    @Override
    public void tick() {
        super.tick();
        setAlpha((float) Math.cos(((double) this.age / this.lifetime / 2d) * Math.PI));
        this.oRoll = this.roll;
        this.roll = (float) (this.age / 40f * Math.PI);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType pSimpleParticleType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            RandomSource pRandomsource = pLevel.random;
            double dX = pRandomsource.nextGaussian() * (double) 1.0E-6F * 0.25;
            double dY = pRandomsource.nextGaussian() * (double) 1.0E-4F * 0.25;
            double dZ = pRandomsource.nextGaussian() * (double) 1.0E-6F * 0.25;
            Spore pSpore = new Spore(pLevel, pX, pY, pZ, dX, dY, dZ, this.sprite);
            pSpore.setColor((float) (0.9 + Math.random() * 0.1), (float) (0.4 + Math.random() * 0.3), (float) (0.4 + Math.random() * 0.1));
            return pSpore;
        }
    }
}