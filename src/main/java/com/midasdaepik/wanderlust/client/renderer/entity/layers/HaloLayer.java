package com.midasdaepik.wanderlust.client.renderer.entity.layers;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HaloLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo.png");
	private static final ResourceLocation TEXTURE_LOCATION_FLUGEL = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_flugel.png");
	private static final ResourceLocation TEXTURE_LOCATION_HOSHI = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_hoshi.png");
	private static final ResourceLocation TEXTURE_LOCATION_HOSHI_TERROR = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_hoshi_terror.png");
	private static final ResourceLocation TEXTURE_LOCATION_KOKONA = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_kokona.png");
	private static final ResourceLocation TEXTURE_LOCATION_SNOW = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_snow.png");
	private static final ResourceLocation TEXTURE_LOCATION_SQUARE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_square.png");
	private static final ResourceLocation TEXTURE_LOCATION_SQUARE_DARK = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_square_dark.png");
	private static final ResourceLocation TEXTURE_LOCATION_CHERUB = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_cherub.png");
	private static final ResourceLocation TEXTURE_LOCATION_DARK = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/halo_dark.png");

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "halo"), "main");
	private final HaloModel<T> haloModel;

	public HaloLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
		super(renderer);
		this.haloModel = new HaloModel<>(modelSet.bakeLayer(LAYER_LOCATION));
	}

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack pItemStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
		if (pItemStack.getOrDefault(WLDataComponents.COSMETIC_TYPE, "").equals("halo")) {
			int pMaterial = pItemStack.getOrDefault(WLDataComponents.COSMETIC_MATERIAL, -1);
			if (pMaterial >= 0) {
				switch (pMaterial) {
					case 0 -> {
						poseStack.pushPose();

						float yRot = Mth.lerp(partialTicks, livingEntity.yRotO, livingEntity.getYRot()) - Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
						float xRot = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
						poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
						poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

						poseStack.translate(0, -0.1, -0.175);

						poseStack.mulPose(Axis.XP.rotationDegrees(-15));

						poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * 2));

						this.getParentModel().copyPropertiesTo(this.haloModel);
						this.haloModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
						VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}

					case 1 -> {
						poseStack.pushPose();

						float yRot = Mth.lerp(partialTicks, livingEntity.yRotO, livingEntity.getYRot()) - Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
						float xRot = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
						poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
						poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

						poseStack.translate(-0.05, -0.1, 0.05);

						poseStack.mulPose(Axis.YP.rotationDegrees(-45));
						poseStack.mulPose(Axis.XP.rotationDegrees(45));

						poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks));

						this.getParentModel().copyPropertiesTo(this.haloModel);
						this.haloModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
						VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION_FLUGEL));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION_FLUGEL));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}

					case 2 -> {
						poseStack.pushPose();

						float yRot = Mth.lerp(partialTicks, livingEntity.yRotO, livingEntity.getYRot()) - Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
						float xRot = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
						poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
						poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

						boolean pWarm = !livingEntity.level().dimensionType().ultraWarm();
						ResourceLocation pLocation = pWarm ? TEXTURE_LOCATION_HOSHI : TEXTURE_LOCATION_HOSHI_TERROR;

						if (pWarm) {
							poseStack.translate(0, -0.1, -0.05);

							poseStack.mulPose(Axis.XP.rotationDegrees(-35));
						} else {
							poseStack.translate(0, -0.6, -0.3);

							poseStack.mulPose(Axis.XP.rotationDegrees(-75));
							poseStack.mulPose(Axis.YP.rotationDegrees(180));
						}

						this.getParentModel().copyPropertiesTo(this.haloModel);
						this.haloModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
						VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(pLocation));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(pLocation));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}

					case 3 -> {
						poseStack.pushPose();

						float yRot = Mth.lerp(partialTicks, livingEntity.yRotO, livingEntity.getYRot()) - Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
						float xRot = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
						poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
						poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

						poseStack.translate(0, Math.sin(ageInTicks / 60 * Math.PI) * 0.05 - 0.05, 0);

						poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * 2));

						this.getParentModel().copyPropertiesTo(this.haloModel);
						this.haloModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
						VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION_KOKONA));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION_KOKONA));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}

					case 4 -> {
						poseStack.pushPose();

						float yRot = Mth.lerp(partialTicks, livingEntity.yRotO, livingEntity.getYRot()) - Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
						float xRot = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
						poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
						poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

						poseStack.translate(0, -0.15, -0.25);

						poseStack.mulPose(Axis.XP.rotationDegrees(-30));

						poseStack.mulPose(Axis.YP.rotationDegrees(-45));

						this.getParentModel().copyPropertiesTo(this.haloModel);
						this.haloModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
						VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION_SNOW));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION_SNOW));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}

					case 5 -> {
						poseStack.pushPose();

						float yRot = Mth.lerp(partialTicks, livingEntity.yRotO, livingEntity.getYRot()) - Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
						float xRot = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
						poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
						poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

						poseStack.translate(0, -0.45, -0.2);

						poseStack.mulPose(Axis.YP.rotationDegrees(-30));
						poseStack.mulPose(Axis.XP.rotationDegrees(-75));

						poseStack.mulPose(Axis.YP.rotationDegrees(185));

						ResourceLocation pLocation = livingEntity.level().getRawBrightness(livingEntity.getOnPos().above(), 0) > 1 ? TEXTURE_LOCATION_SQUARE : TEXTURE_LOCATION_SQUARE_DARK;

						this.getParentModel().copyPropertiesTo(this.haloModel);
						this.haloModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
						VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(pLocation));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(pLocation));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}

					case 6 -> {
						poseStack.pushPose();

						float yRot = Mth.lerp(partialTicks, livingEntity.yRotO, livingEntity.getYRot()) - Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
						float xRot = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
						poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
						poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

						poseStack.translate(0, -0.1, -0.15);

						poseStack.mulPose(Axis.XP.rotationDegrees(-20));

						poseStack.mulPose(Axis.YP.rotationDegrees(180));

						this.getParentModel().copyPropertiesTo(this.haloModel);
						this.haloModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
						VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION_CHERUB));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION_CHERUB));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}

					case 7 -> {
						poseStack.pushPose();

						float yRot = Mth.lerp(partialTicks, livingEntity.yRotO, livingEntity.getYRot()) - Mth.lerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
						float xRot = Mth.lerp(partialTicks, livingEntity.xRotO, livingEntity.getXRot());
						poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
						poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

						poseStack.translate(0, 0.05, 0);

						poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks));

						this.getParentModel().copyPropertiesTo(this.haloModel);
						this.haloModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
						VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION_DARK));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION_DARK));
						this.haloModel.renderToBuffer(poseStack, vertexconsumer, 16711680, OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}
				}
			}
		}
	}
}