package com.midasdaepik.wanderlust.client.renderer.entity.layers;

import com.midasdaepik.wanderlust.Wanderlust;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.DRAGON_WINGS_STATUS;

@OnlyIn(Dist.CLIENT)
public class DragonWingsLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "dragon_wings"), "main");
	private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/dragon_wings.png");
	private final DragonWingsModel<T> dragonWingsModel;

	public DragonWingsLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
		super(renderer);
		this.dragonWingsModel = new DragonWingsModel<>(modelSet.bakeLayer(LAYER_LOCATION));
	}

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (livingEntity.getData(DRAGON_WINGS_STATUS)) {
			poseStack.pushPose();
			this.getParentModel().copyPropertiesTo(this.dragonWingsModel);
			this.dragonWingsModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(TEXTURE_LOCATION));
			this.dragonWingsModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
	}
}