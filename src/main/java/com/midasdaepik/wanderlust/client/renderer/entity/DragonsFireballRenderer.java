package com.midasdaepik.wanderlust.client.renderer.entity;

import com.midasdaepik.wanderlust.entity.DragonsFireball;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class DragonsFireballRenderer extends EntityRenderer<DragonsFireball> {
	private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/enderdragon/dragon_fireball.png");
	private static final RenderType RENDER_TYPE;

	public DragonsFireballRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	protected int getBlockLightLevel(DragonsFireball entity, BlockPos pos) {
		return 15;
	}

	@Override
	public void render(DragonsFireball entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		poseStack.pushPose();
		poseStack.scale(1.4F, 1.4F, 1.4F);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		PoseStack.Pose posestack$pose = poseStack.last();
		VertexConsumer vertexconsumer = bufferIn.getBuffer(RENDER_TYPE);
		vertex(vertexconsumer, posestack$pose, packedLightIn, 0.0F, 0, 0, 1);
		vertex(vertexconsumer, posestack$pose, packedLightIn, 1.0F, 0, 1, 1);
		vertex(vertexconsumer, posestack$pose, packedLightIn, 1.0F, 1, 1, 0);
		vertex(vertexconsumer, posestack$pose, packedLightIn, 0.0F, 1, 0, 0);
		poseStack.popPose();
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, int y, int u, int v) {
		consumer.addVertex(pose, x - 0.5F, (float)y - 0.25F, 0.0F).setColor(-1).setUv((float)u, (float)v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(DragonsFireball pEntity) {
		return TEXTURE_LOCATION;
	}

	static {
		RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);
	}
}
