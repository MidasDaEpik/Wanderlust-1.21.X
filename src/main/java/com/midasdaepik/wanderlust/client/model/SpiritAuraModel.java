package com.midasdaepik.wanderlust.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

public class SpiritAuraModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart Root;
	private final ModelPart SpiritRing;
	private final ModelPart SpiritRingPart0;
	private final ModelPart SpiritRingPart1;
	private final ModelPart SpiritRingPart2;
	private final ModelPart SpiritRingPart3;
	public AnimationState rotate = new AnimationState();

	public SpiritAuraModel(ModelPart pRoot) {
		this.Root = pRoot;
		this.SpiritRing = pRoot.getChild("SpiritRing");
		this.SpiritRingPart0 = this.SpiritRing.getChild("SpiritRingPart0");
		this.SpiritRingPart1 = this.SpiritRing.getChild("SpiritRingPart1");
		this.SpiritRingPart2 = this.SpiritRing.getChild("SpiritRingPart2");
		this.SpiritRingPart3 = this.SpiritRing.getChild("SpiritRingPart3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition SpiritRing = partdefinition.addOrReplaceChild("SpiritRing", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition SpiritRingPart0 = SpiritRing.addOrReplaceChild("SpiritRingPart0", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, 2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 28).addBox(1.0F, 4.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offset(14.0F, -22.0F, -3.0F));

		PartDefinition SpiritRingPart1 = SpiritRing.addOrReplaceChild("SpiritRingPart1", CubeListBuilder.create(), PartPose.offset(-14.0F, -22.0F, 3.0F));

		PartDefinition SpiritRingPart1_r1 = SpiritRingPart1.addOrReplaceChild("SpiritRingPart1_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F))
		.texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition SpiritRingPart2 = SpiritRing.addOrReplaceChild("SpiritRingPart2", CubeListBuilder.create(), PartPose.offset(3.0F, -22.0F, 14.0F));

		PartDefinition SpiritRingPart2_r1 = SpiritRingPart2.addOrReplaceChild("SpiritRingPart2_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F))
		.texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition SpiritRingPart3 = SpiritRing.addOrReplaceChild("SpiritRingPart3", CubeListBuilder.create(), PartPose.offset(-3.0F, -22.0F, -14.0F));

		PartDefinition SpiritRingPart3_r1 = SpiritRingPart3.addOrReplaceChild("SpiritRingPart3_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F))
		.texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		SpiritRing.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}

	@Override
	public ModelPart root() {
		return this.Root;
	}

	@Override
	public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.rotate.startIfStopped(pEntity.tickCount);

		this.animate(this.rotate, SpiritAuraAnimation.ROTATE, pAgeInTicks);
	}
}