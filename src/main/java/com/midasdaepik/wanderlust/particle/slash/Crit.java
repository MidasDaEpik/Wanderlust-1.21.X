package com.midasdaepik.wanderlust.particle.slash;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class Crit extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float pitch;
    private final float yaw;
    private final float offset;

    protected Crit(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSpriteSet, double pXSpeed, double pYSpeed, double pZSpeed, CritOptions pCritOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.setSpriteFromAge(pSpriteSet);
        this.sprites = pSpriteSet;

        this.quadSize = pCritOptions.getScale() * 2;
        this.lifetime = 6;
        this.friction = 0f;

        this.rCol = pCritOptions.getColor().x;
        this.gCol = pCritOptions.getColor().y;
        this.bCol = pCritOptions.getColor().z;
        this.alpha = 0.5f;

        this.pitch = pCritOptions.getPitch();
        this.yaw = pCritOptions.getYaw();

        this.offset = (float) ((Math.random() - 0.5) * 0.1);
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotationY((float) (this.yaw - Math.PI)).rotateX(this.pitch).rotateY((float) (Math.PI / 2 + offset * Math.PI));
        this.renderRotatedQuad(pBuffer, pRenderInfo, quaternionf, pPartialTicks, false);
        quaternionf.rotationY(this.yaw).rotateX(-this.pitch).rotateY((float) (Math.PI / 2 + offset * Math.PI));
        this.renderRotatedQuad(pBuffer, pRenderInfo, quaternionf, pPartialTicks, true);
    }

    public void renderRotatedQuad(VertexConsumer buffer, Camera camera, Quaternionf quaternion, float partialTicks, boolean flip) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        this.renderRotatedQuad(buffer, quaternion, f, f1, f2, partialTicks, flip);
    }

    public void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks, boolean flip) {
        float f = this.getQuadSize(partialTicks);
        float f1 = flip ? this.getU1() : this.getU0();
        float f2 = flip ? this.getU0() : this.getU1();
        float f3 = this.getV0();
        float f4 = this.getV1();
        int i = this.getLightColor(partialTicks);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, f1, f4, i);
    }

    public void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(packedLight);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float partialTick) {
        return LightTexture.pack(15, 15);
    }

    public static class Provider implements ParticleProvider<CritOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(CritOptions pCritOptions, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new Crit(pLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed, pCritOptions);
        }
    }
}
