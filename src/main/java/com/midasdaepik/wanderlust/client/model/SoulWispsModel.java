package com.midasdaepik.wanderlust.client.model;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.SOUL_WISPS_STATUS;

public class SoulWispsModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart Root;
	private final ModelPart SoulWisps;
	private final ModelPart SoulWisps0;
	private final ModelPart SoulWispsPart0;
	private final ModelPart SoulWispsPart1;
	private final ModelPart SoulWispsPart2;
	private final ModelPart SoulWispsPart3;
	private final ModelPart SoulWisps1;
	private final ModelPart SoulWispsPart4;
	private final ModelPart SoulWispsPart5;
	private final ModelPart SoulWispsPart6;
	private final ModelPart SoulWispsPart7;
	public AnimationState rotate = new AnimationState();

	public SoulWispsModel(ModelPart pRoot) {
		this.Root = pRoot;
		this.SoulWisps = pRoot.getChild("SoulWisps");
		this.SoulWisps0 = this.SoulWisps.getChild("SoulWisps0");
		this.SoulWispsPart0 = this.SoulWisps0.getChild("SoulWispsPart0");
		this.SoulWispsPart1 = this.SoulWisps0.getChild("SoulWispsPart1");
		this.SoulWispsPart2 = this.SoulWisps0.getChild("SoulWispsPart2");
		this.SoulWispsPart3 = this.SoulWisps0.getChild("SoulWispsPart3");
		this.SoulWisps1 = this.SoulWisps.getChild("SoulWisps1");
		this.SoulWispsPart4 = this.SoulWisps1.getChild("SoulWispsPart4");
		this.SoulWispsPart5 = this.SoulWisps1.getChild("SoulWispsPart5");
		this.SoulWispsPart6 = this.SoulWisps1.getChild("SoulWispsPart6");
		this.SoulWispsPart7 = this.SoulWisps1.getChild("SoulWispsPart7");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition SoulWisps = partdefinition.addOrReplaceChild("SoulWisps", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition SoulWisps0 = SoulWisps.addOrReplaceChild("SoulWisps0", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition SoulWispsPart0 = SoulWisps0.addOrReplaceChild("SoulWispsPart0", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(14.0F, -19.0F, -3.0F));

		PartDefinition SoulWispsPart0_r1 = SoulWispsPart0.addOrReplaceChild("SoulWispsPart0_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition SoulWispsPart1 = SoulWisps0.addOrReplaceChild("SoulWispsPart1", CubeListBuilder.create(), PartPose.offset(-14.0F, -7.0F, 3.0F));

		PartDefinition SoulWispsPart1_r1 = SoulWispsPart1.addOrReplaceChild("SoulWispsPart1_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 3.1416F, 0.0F));

		PartDefinition SoulWispsPart1_r2 = SoulWispsPart1.addOrReplaceChild("SoulWispsPart1_r2", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition SoulWispsPart2 = SoulWisps0.addOrReplaceChild("SoulWispsPart2", CubeListBuilder.create(), PartPose.offset(3.0F, -25.0F, 14.0F));

		PartDefinition SoulWispsPart2_r1 = SoulWispsPart2.addOrReplaceChild("SoulWispsPart2_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, -1.5708F, 0.0F));

		PartDefinition SoulWispsPart2_r2 = SoulWispsPart2.addOrReplaceChild("SoulWispsPart2_r2", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition SoulWispsPart3 = SoulWisps0.addOrReplaceChild("SoulWispsPart3", CubeListBuilder.create(), PartPose.offset(-3.0F, -13.0F, -14.0F));

		PartDefinition SoulWispsPart3_r1 = SoulWispsPart3.addOrReplaceChild("SoulWispsPart3_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F, 0.0F));

		PartDefinition SoulWispsPart3_r2 = SoulWispsPart3.addOrReplaceChild("SoulWispsPart3_r2", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition SoulWisps1 = SoulWisps.addOrReplaceChild("SoulWisps1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition SoulWispsPart4 = SoulWisps1.addOrReplaceChild("SoulWispsPart4", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -1.0F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(14.0F, -7.0F, -3.0F));

		PartDefinition SoulWispsPart4_r1 = SoulWispsPart4.addOrReplaceChild("SoulWispsPart4_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition SoulWispsPart5 = SoulWisps1.addOrReplaceChild("SoulWispsPart5", CubeListBuilder.create(), PartPose.offset(-14.0F, -19.0F, 3.0F));

		PartDefinition SoulWispsPart5_r1 = SoulWispsPart5.addOrReplaceChild("SoulWispsPart5_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 1.5708F, 3.1416F, 0.0F));

		PartDefinition SoulWispsPart5_r2 = SoulWispsPart5.addOrReplaceChild("SoulWispsPart5_r2", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition SoulWispsPart6 = SoulWisps1.addOrReplaceChild("SoulWispsPart6", CubeListBuilder.create(), PartPose.offset(3.0F, -13.0F, 14.0F));

		PartDefinition SoulWispsPart6_r1 = SoulWispsPart6.addOrReplaceChild("SoulWispsPart6_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 1.5708F, -1.5708F, 0.0F));

		PartDefinition SoulWispsPart6_r2 = SoulWispsPart6.addOrReplaceChild("SoulWispsPart6_r2", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition SoulWispsPart7 = SoulWisps1.addOrReplaceChild("SoulWispsPart7", CubeListBuilder.create(), PartPose.offset(-3.0F, -25.0F, -14.0F));

		PartDefinition SoulWispsPart7_r1 = SoulWispsPart7.addOrReplaceChild("SoulWispsPart7_r1", CubeListBuilder.create().texOffs(8, 28).addBox(1.0F, 1.0F, 1.0F, -2.0F, -8.0F, -2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 1.5708F, 1.5708F, 0.0F));

		PartDefinition SoulWispsPart7_r2 = SoulWispsPart7.addOrReplaceChild("SoulWispsPart7_r2", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		SoulWisps.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}

	@Override
	public ModelPart root() {
		return this.Root;
	}

	@Override
	public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		switch (pEntity.getData(SOUL_WISPS_STATUS)) {
			case -1 -> {
				this.SoulWispsPart0.visible = false;
				this.SoulWispsPart4.visible = false;
				this.SoulWispsPart1.visible = false;
				this.SoulWispsPart5.visible = false;
				this.SoulWispsPart2.visible = false;
				this.SoulWispsPart6.visible = false;
				this.SoulWispsPart3.visible = false;
				this.SoulWispsPart7.visible = false;
			}
			case 0 -> {
				this.SoulWispsPart0.visible = true;
				this.SoulWispsPart4.visible = false;
				this.SoulWispsPart1.visible = false;
				this.SoulWispsPart5.visible = false;
				this.SoulWispsPart2.visible = false;
				this.SoulWispsPart6.visible = false;
				this.SoulWispsPart3.visible = false;
				this.SoulWispsPart7.visible = false;
			}
			case 1 -> {
				this.SoulWispsPart0.visible = true;
				this.SoulWispsPart4.visible = true;
				this.SoulWispsPart1.visible = false;
				this.SoulWispsPart5.visible = false;
				this.SoulWispsPart2.visible = false;
				this.SoulWispsPart6.visible = false;
				this.SoulWispsPart3.visible = false;
				this.SoulWispsPart7.visible = false;
			}
			case 2 -> {
				this.SoulWispsPart0.visible = true;
				this.SoulWispsPart4.visible = true;
				this.SoulWispsPart1.visible = true;
				this.SoulWispsPart5.visible = false;
				this.SoulWispsPart2.visible = false;
				this.SoulWispsPart6.visible = false;
				this.SoulWispsPart3.visible = false;
				this.SoulWispsPart7.visible = false;
			}
			case 3 -> {
				this.SoulWispsPart0.visible = true;
				this.SoulWispsPart4.visible = true;
				this.SoulWispsPart1.visible = true;
				this.SoulWispsPart5.visible = true;
				this.SoulWispsPart2.visible = false;
				this.SoulWispsPart6.visible = false;
				this.SoulWispsPart3.visible = false;
				this.SoulWispsPart7.visible = false;
			}
			case 4 -> {
				this.SoulWispsPart0.visible = true;
				this.SoulWispsPart4.visible = true;
				this.SoulWispsPart1.visible = true;
				this.SoulWispsPart5.visible = true;
				this.SoulWispsPart2.visible = true;
				this.SoulWispsPart6.visible = false;
				this.SoulWispsPart3.visible = false;
				this.SoulWispsPart7.visible = false;
			}
			case 5 -> {
				this.SoulWispsPart0.visible = true;
				this.SoulWispsPart4.visible = true;
				this.SoulWispsPart1.visible = true;
				this.SoulWispsPart5.visible = true;
				this.SoulWispsPart2.visible = true;
				this.SoulWispsPart6.visible = true;
				this.SoulWispsPart3.visible = false;
				this.SoulWispsPart7.visible = false;
			}
			case 6 -> {
				this.SoulWispsPart0.visible = true;
				this.SoulWispsPart4.visible = true;
				this.SoulWispsPart1.visible = true;
				this.SoulWispsPart5.visible = true;
				this.SoulWispsPart2.visible = true;
				this.SoulWispsPart6.visible = true;
				this.SoulWispsPart3.visible = true;
				this.SoulWispsPart7.visible = false;
			}
			default -> {
				this.SoulWispsPart0.visible = true;
				this.SoulWispsPart4.visible = true;
				this.SoulWispsPart1.visible = true;
				this.SoulWispsPart5.visible = true;
				this.SoulWispsPart2.visible = true;
				this.SoulWispsPart6.visible = true;
				this.SoulWispsPart3.visible = true;
				this.SoulWispsPart7.visible = true;
			}
		}

		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.rotate.startIfStopped(pEntity.tickCount);

		this.animate(this.rotate, SoulWispsAnimation.ROTATE, pAgeInTicks);
	}
}