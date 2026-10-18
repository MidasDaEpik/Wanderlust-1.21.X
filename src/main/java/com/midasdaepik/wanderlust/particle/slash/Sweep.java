package com.midasdaepik.wanderlust.particle.slash;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public abstract class Sweep extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final boolean flipped;
    private final float pitch;
    private final float yaw;
    private final float offset;

    protected Sweep(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSpriteSet, double pXSpeed, double pYSpeed, double pZSpeed, SweepOptions pSweepOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.setSpriteFromAge(pSpriteSet);
        this.sprites = pSpriteSet;

        this.quadSize = pSweepOptions.getScale() * 2 * (float) (1 + (Math.random() - 0.5) * 0.2);
        this.lifetime = 4;
        this.friction = 0f;

        this.rCol = pSweepOptions.getColor().x;
        this.gCol = pSweepOptions.getColor().y;
        this.bCol = pSweepOptions.getColor().z;
        this.alpha = 0.5f;

        this.flipped = pSweepOptions.getFlipped();
        this.pitch = pSweepOptions.getPitch();
        this.yaw = pSweepOptions.getYaw();

        this.offset = (float) ((Math.random() - 0.5) * 0.05);
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotationY((float) (this.yaw - Math.PI)).rotateX((float) (this.pitch - Math.PI / 2)).rotateY((float) (offset * Math.PI));
        this.renderRotatedQuad(pBuffer, pRenderInfo, quaternionf, pPartialTicks, flipped);
        quaternionf.rotationY(this.yaw).rotateX((float) (-this.pitch + Math.PI / 2)).rotateY((float) (offset * Math.PI));
        this.renderRotatedQuad(pBuffer, pRenderInfo, quaternionf, pPartialTicks, !flipped);
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
}
