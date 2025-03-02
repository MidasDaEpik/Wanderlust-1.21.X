package com.midasdaepik.wanderlust.renderer.entity;

import com.midasdaepik.wanderlust.entity.DragonsRageBreath;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DragonsRageBreathRenderer extends EntityRenderer<DragonsRageBreath> {
	public DragonsRageBreathRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(DragonsRageBreath entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(DragonsRageBreath pEntity) {
		return null;
	}
}
