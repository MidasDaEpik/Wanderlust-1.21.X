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

public class ElderChestplateModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "elder_chestplate"), "main");
	public final ModelPart Body;
	public final ModelPart RightArm;
	public final ModelPart LeftArm;

	public ElderChestplateModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition BottomSpine_r1 = Body.addOrReplaceChild("BottomSpine_r1", CubeListBuilder.create().texOffs(56, 21).addBox(-1.0F, 2.25F, -12.25F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition MiddleSpine_r1 = Body.addOrReplaceChild("MiddleSpine_r1", CubeListBuilder.create().texOffs(56, 21).addBox(-1.0F, -8.5F, -18.5F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition TopSpine_r1 = Body.addOrReplaceChild("TopSpine_r1", CubeListBuilder.create().texOffs(56, 21).addBox(-1.0F, -22.5F, -14.5F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).texOffs(40, 8).addBox(-3.0F, 7.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition RightBackSpike_r1 = RightArm.addOrReplaceChild("RightBackSpike_r1", CubeListBuilder.create().texOffs(56, 21).addBox(9.2666F, -28.483F, -3.3162F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 22.0F, 0.0F, -0.3665F, 0.4014F, -0.8116F));

		PartDefinition RightFrontSpike_r1 = RightArm.addOrReplaceChild("RightFrontSpike_r1", CubeListBuilder.create().texOffs(56, 21).addBox(9.2165F, -28.4795F, 1.3012F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 22.0F, 0.0F, 0.3665F, -0.4014F, -0.8116F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition LeftGauntlet_r1 = LeftArm.addOrReplaceChild("LeftGauntlet_r1", CubeListBuilder.create().texOffs(40, 8).addBox(-8.0F, -14.75F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-5.0F, 22.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition LeftBackSpike_r1 = LeftArm.addOrReplaceChild("LeftBackSpike_r1", CubeListBuilder.create().texOffs(56, 21).addBox(9.2165F, -28.4795F, 1.3012F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 22.0F, 0.0F, -2.7751F, 0.4014F, -2.33F));

		PartDefinition LeftFrontSpike_r1 = LeftArm.addOrReplaceChild("LeftFrontSpike_r1", CubeListBuilder.create().texOffs(56, 21).addBox(9.2666F, -28.483F, -3.3162F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 22.0F, 0.0F, 2.7751F, -0.4014F, -2.33F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		Body.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
		RightArm.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
		LeftArm.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}

	@Override
	public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
	}
}