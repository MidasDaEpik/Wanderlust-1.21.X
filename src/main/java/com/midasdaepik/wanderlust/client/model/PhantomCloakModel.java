package com.midasdaepik.wanderlust.client.model;

import com.midasdaepik.wanderlust.Wanderlust;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PhantomCloakModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "phantom_cloak"), "main");
	public final ModelPart Body;

	public PhantomCloakModel(ModelPart root) {
		this.Body = root.getChild("Body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition CloakLeft_r1 = Body.addOrReplaceChild("CloakLeft_r1", CubeListBuilder.create().texOffs(0, 14).mirror().addBox(3.1289F, -22.9826F, -2.3034F, 8.0F, 16.0F, 8.0F, new CubeDeformation(2.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0948F, -0.079F, -0.0948F));

		PartDefinition CloakRight_r1 = Body.addOrReplaceChild("CloakRight_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-11.1289F, -22.9826F, -2.3034F, 8.0F, 16.0F, 8.0F, new CubeDeformation(2.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0948F, 0.079F, 0.0948F));

		PartDefinition CloakFlair_r1 = Body.addOrReplaceChild("CloakFlair_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -27.8322F, -1.2047F, 10.0F, 4.0F, 10.0F, new CubeDeformation(1.05F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		Body.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}

	@Override
	public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
	}
}