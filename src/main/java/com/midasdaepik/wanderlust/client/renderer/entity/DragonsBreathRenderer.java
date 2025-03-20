package com.midasdaepik.wanderlust.client.renderer.entity;

import com.midasdaepik.wanderlust.entity.DragonsBreath;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DragonsBreathRenderer extends EntityRenderer<DragonsBreath> {
	public DragonsBreathRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(DragonsBreath entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(DragonsBreath pEntity) {
		return null;
	}
}
