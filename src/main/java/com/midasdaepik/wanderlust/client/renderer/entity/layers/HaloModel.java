package com.midasdaepik.wanderlust.client.renderer.entity.layers;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class HaloModel<T extends LivingEntity> extends AgeableListModel<T> {
	private final ModelPart halo;

	public HaloModel(ModelPart root) {
		this.halo = root.getChild("halo");
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition halo = partdefinition.addOrReplaceChild("halo", CubeListBuilder.create()
				.texOffs(-16, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(-16, 16).addBox(-8.0F, -9.7F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(-16, 32).addBox(-8.0F, -11.4F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition ring = halo.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -11.0F, -4.75F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition ring2_r1 = ring.addOrReplaceChild("ring2", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, -11.0F, -4.75F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition ring3_r1 = ring.addOrReplaceChild("ring3", CubeListBuilder.create().texOffs(0, 56).addBox(-2.0F, -11.0F, -4.75F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition ring4_r1 = ring.addOrReplaceChild("ring4", CubeListBuilder.create().texOffs(0, 60).addBox(-2.0F, -11.0F, -4.75F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));
		PartDefinition ring5_r1 = ring.addOrReplaceChild("ring5", CubeListBuilder.create().texOffs(8, 48).addBox(-2.0F, -11.0F, -4.75F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition ring6_r1 = ring.addOrReplaceChild("ring6", CubeListBuilder.create().texOffs(8, 52).addBox(-2.0F, -11.0F, -4.75F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));
		PartDefinition ring7_r1 = ring.addOrReplaceChild("ring7", CubeListBuilder.create().texOffs(8, 56).addBox(-2.0F, -11.0F, -4.75F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition ring8_r1 = ring.addOrReplaceChild("ring8", CubeListBuilder.create().texOffs(8, 60).addBox(-2.0F, -11.0F, -4.75F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 64);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.halo);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of();
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if (pEntity.isCrouching()) {
			this.halo.y = 0;
		} else {
			this.halo.y = -4;
		}
	}
}