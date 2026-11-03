package com.midasdaepik.wanderlust.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class Cube extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float start_scale;
    private final float end_scale;
    private final float pitch;
    private final float yaw;

    protected Cube(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSpriteSet, double pXSpeed, double pYSpeed, double pZSpeed, CubeOptions pCubeOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.setSpriteFromAge(pSpriteSet);
        this.sprites = pSpriteSet;

        this.start_scale = pCubeOptions.getStartScale();
        this.end_scale = pCubeOptions.getEndScale();

        this.quadSize = this.start_scale;
        this.lifetime = pCubeOptions.getLifetime();
        this.friction = 0f;

        this.rCol = pCubeOptions.getColor().x;
        this.gCol = pCubeOptions.getColor().y;
        this.bCol = pCubeOptions.getColor().z;
        this.alpha = 0.333f;

        this.pitch = pCubeOptions.getPitch();
        this.yaw = pCubeOptions.getYaw();
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Quaternionf quaternionf = new Quaternionf();

        float pYaw = this.yaw;
        float pPitch = this.pitch;
        Vec3 pVec3 = new Vec3(
                Math.sin(pYaw) * Math.cos(pPitch) * quadSize,
                Math.sin(pPitch) * this.quadSize,
                Math.cos(pYaw) * Math.cos(pPitch) * quadSize
        );
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) pVec3.x, (float) pVec3.y, (float) pVec3.z, pPartialTicks);
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch).rotateY((float) Math.PI);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) pVec3.x, (float) pVec3.y, (float) pVec3.z, pPartialTicks);

        quaternionf.rotationY(this.yaw).rotateX(-this.pitch);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) -pVec3.x, (float) -pVec3.y, (float) -pVec3.z, pPartialTicks);
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch).rotateY((float) Math.PI);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) -pVec3.x, (float) -pVec3.y, (float) -pVec3.z, pPartialTicks);

        pYaw = this.yaw;
        pPitch = this.pitch + (float) (Math.PI / 2);
        pVec3 = new Vec3(
                Math.sin(pYaw) * Math.cos(pPitch) * quadSize,
                Math.sin(pPitch) * this.quadSize,
                Math.cos(pYaw) * Math.cos(pPitch) * quadSize
        );
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch + (float) Math.PI / 2);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) pVec3.x, (float) pVec3.y, (float) pVec3.z, pPartialTicks);
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch + (float) Math.PI / 2).rotateY((float) Math.PI);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) pVec3.x, (float) pVec3.y, (float) pVec3.z, pPartialTicks);

        quaternionf.rotationY(this.yaw).rotateX(-this.pitch + (float) Math.PI / 2);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) -pVec3.x, (float) -pVec3.y, (float) -pVec3.z, pPartialTicks);
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch + (float) Math.PI / 2).rotateY((float) Math.PI);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) -pVec3.x, (float) -pVec3.y, (float) -pVec3.z, pPartialTicks);

        pYaw = this.yaw + (float) (Math.PI / 2);
        pPitch = 0;
        pVec3 = new Vec3(
                Math.sin(pYaw) * Math.cos(pPitch) * quadSize,
                Math.sin(pPitch) * this.quadSize,
                Math.cos(pYaw) * Math.cos(pPitch) * quadSize
        );
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch).rotateY((float) Math.PI / 2);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) pVec3.x, (float) pVec3.y, (float) pVec3.z, pPartialTicks);
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch).rotateY((float) -Math.PI / 2);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) pVec3.x, (float) pVec3.y, (float) pVec3.z, pPartialTicks);

        quaternionf.rotationY(this.yaw).rotateX(-this.pitch).rotateY((float) Math.PI / 2);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) -pVec3.x, (float) -pVec3.y, (float) -pVec3.z, pPartialTicks);
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch).rotateY((float) -Math.PI / 2);
        this.offsetRenderRotatedQuad(pBuffer, pRenderInfo, quaternionf, (float) -pVec3.x, (float) -pVec3.y, (float) -pVec3.z, pPartialTicks);
    }

    protected void offsetRenderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) + x - vec3.x());
        float f1 = (float)(Mth.lerp(partialTicks, this.yo, this.y) + y - vec3.y());
        float f2 = (float)(Mth.lerp(partialTicks, this.zo, this.z) + z - vec3.z());
        this.renderRotatedQuad(buffer, quaternion, f, f1, f2, partialTicks);
    }

    @Override
    public void tick() {
        setAlpha((float) Math.cos(((double) this.age / this.lifetime / 2d) * Math.PI) / 3);
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
            this.quadSize = (float) Math.sin((double) this.age / this.lifetime / 2 * Math.PI) * (this.end_scale - this.start_scale) + this.start_scale;
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

    public static class Provider implements ParticleProvider<CubeOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(CubeOptions pCubeOptions, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new Cube(pLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed, pCubeOptions);
        }
    }
}
