package com.midasdaepik.wanderlust.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class OrientedCircle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float start_scale;
    private final float end_scale;
    private final float pitch;
    private final float yaw;

    protected OrientedCircle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSpriteSet, double pXSpeed, double pYSpeed, double pZSpeed, OrientedCircleOptions pOrientedCircleOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.setSpriteFromAge(pSpriteSet);
        this.sprites = pSpriteSet;

        this.start_scale = pOrientedCircleOptions.getStartScale();
        this.end_scale = pOrientedCircleOptions.getEndScale();

        this.quadSize = this.start_scale;
        this.lifetime = pOrientedCircleOptions.getLifetime();
        this.friction = 0f;

        this.rCol = pOrientedCircleOptions.getColor().x;
        this.gCol = pOrientedCircleOptions.getColor().y;
        this.bCol = pOrientedCircleOptions.getColor().z;

        this.pitch = pOrientedCircleOptions.getPitch();
        this.yaw = pOrientedCircleOptions.getYaw();
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        this.alpha = 1.0F - Mth.clamp(((float)this.age + pPartialTicks) / (float)this.lifetime, 0.0F, 1.0F);

        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch);
        this.renderRotatedQuad(pBuffer, pRenderInfo, quaternionf, pPartialTicks);
        quaternionf.rotationY((float) (this.yaw - Math.PI)).rotateX(this.pitch);
        this.renderRotatedQuad(pBuffer, pRenderInfo, quaternionf, pPartialTicks);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
            this.quadSize = this.start_scale * (this.lifetime - this.age) / this.lifetime + this.end_scale * this.age / this.lifetime;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float partialTick) {
        float f = ((float)this.age + partialTick) / (float)this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public static class Provider implements ParticleProvider<OrientedCircleOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(OrientedCircleOptions pHorizontalCircleOptions, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new OrientedCircle(pLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed, pHorizontalCircleOptions);
        }
    }
}
