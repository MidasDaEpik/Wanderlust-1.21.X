package com.midasdaepik.wanderlust.client.renderer.entity.layers;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class DragonWingsModel<T extends LivingEntity> extends AgeableListModel<T> {
	private final ModelPart leftWing;
	private final ModelPart rightWing;

	public DragonWingsModel(ModelPart root) {
		this.leftWing = root.getChild("left_wing");
		this.rightWing = root.getChild("right_wing");
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 2.0F));
		PartDefinition left_wing_r1 = left_wing.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -24.0F, 0.0F, 18.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.0873F));
		PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 2.0F));
		PartDefinition right_wing_r1 = right_wing.addOrReplaceChild("right_wing_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-18.0F, -24.0F, 0.0F, 18.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.0873F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.leftWing, this.rightWing);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		float pDiv = (float) Math.sin(pAgeInTicks / 30 * Math.PI);

		if (pEntity.isCrouching()) {
			this.leftWing.xRot = 0.4363f;
			this.leftWing.z = 12.0f;
			this.rightWing.xRot = 0.4363f;
			this.rightWing.z = 12.0f;

			this.leftWing.yRot = 0.0f;
			this.rightWing.yRot = 0.0f;
		} else {
			this.leftWing.xRot = 0.0f;
			this.leftWing.z = 2.0f;
			this.rightWing.xRot = 0.0f;
			this.rightWing.z = 2.0f;

			this.leftWing.yRot = pDiv * 0.1745f;
			this.rightWing.yRot = -this.leftWing.yRot;
		}
	}
}