package com.midasdaepik.wanderlust.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.KATANA_ABILITY_POST_TIMER;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.KATANA_COMBO;

public class BlazeAuraModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart Root;
	private final ModelPart RingUpper;
	private final ModelPart RingUpperPart0;
	private final ModelPart RingUpperPart1;
	private final ModelPart RingUpperPart2;
	private final ModelPart RingUpperPart3;
	private final ModelPart RingMiddle;
	private final ModelPart RingMiddlePart0;
	private final ModelPart RingMiddlePart1;
	private final ModelPart RingMiddlePart2;
	private final ModelPart RingMiddlePart3;
	public AnimationState rotate = new AnimationState();

	public BlazeAuraModel(ModelPart pRoot) {
		this.Root = pRoot;
		this.RingUpper = pRoot.getChild("RingUpper");
		this.RingUpperPart0 = this.RingUpper.getChild("RingUpperPart0");
		this.RingUpperPart1 = this.RingUpper.getChild("RingUpperPart1");
		this.RingUpperPart2 = this.RingUpper.getChild("RingUpperPart2");
		this.RingUpperPart3 = this.RingUpper.getChild("RingUpperPart3");
		this.RingMiddle = pRoot.getChild("RingMiddle");
		this.RingMiddlePart0 = this.RingMiddle.getChild("RingMiddlePart0");
		this.RingMiddlePart1 = this.RingMiddle.getChild("RingMiddlePart1");
		this.RingMiddlePart2 = this.RingMiddle.getChild("RingMiddlePart2");
		this.RingMiddlePart3 = this.RingMiddle.getChild("RingMiddlePart3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RingUpper = partdefinition.addOrReplaceChild("RingUpper", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition RingUpperPart0 = RingUpper.addOrReplaceChild("RingUpperPart0", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(14.0F, -22.0F, -3.0F));

		PartDefinition RingUpperPart1 = RingUpper.addOrReplaceChild("RingUpperPart1", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-14.0F, -22.0F, 3.0F));

		PartDefinition RingUpperPart2 = RingUpper.addOrReplaceChild("RingUpperPart2", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -22.0F, 14.0F));

		PartDefinition RingUpperPart3 = RingUpper.addOrReplaceChild("RingUpperPart3", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -22.0F, -14.0F));

		PartDefinition RingMiddle = partdefinition.addOrReplaceChild("RingMiddle", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition RingMiddlePart0 = RingMiddle.addOrReplaceChild("RingMiddlePart0", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, -14.0F, 0.0F));

		PartDefinition RingMiddlePart1 = RingMiddle.addOrReplaceChild("RingMiddlePart1", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -14.0F, 0.0F));

		PartDefinition RingMiddlePart2 = RingMiddle.addOrReplaceChild("RingMiddlePart2", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, 11.0F));

		PartDefinition RingMiddlePart3 = RingMiddle.addOrReplaceChild("RingMiddlePart3", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, -11.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		RingUpper.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
		RingMiddle.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}

	@Override
	public ModelPart root() {
		return this.Root;
	}

	@Override
	public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		int pCombo = pEntity.getData(KATANA_COMBO);
		int pKatanaAbilityPostTimer = pEntity.getData(KATANA_ABILITY_POST_TIMER);
		if (pKatanaAbilityPostTimer > 0) {
			this.RingUpperPart2.visible = true;
			this.RingUpperPart3.visible = true;
			this.RingMiddlePart0.visible = true;
			this.RingMiddlePart1.visible = true;
			this.RingMiddlePart2.visible = true;
			this.RingMiddlePart3.visible = true;
		} else {
			switch (pCombo) {
				case 0 -> {
					this.RingUpperPart2.visible = false;
					this.RingUpperPart3.visible = false;
					this.RingMiddlePart0.visible = false;
					this.RingMiddlePart1.visible = false;
					this.RingMiddlePart2.visible = false;
					this.RingMiddlePart3.visible = false;
				}
				case 1 -> {
					this.RingUpperPart2.visible = true;
					this.RingUpperPart3.visible = false;
					this.RingMiddlePart0.visible = false;
					this.RingMiddlePart1.visible = false;
					this.RingMiddlePart2.visible = false;
					this.RingMiddlePart3.visible = false;
				}
				case 2 -> {
					this.RingUpperPart2.visible = true;
					this.RingUpperPart3.visible = true;
					this.RingMiddlePart0.visible = false;
					this.RingMiddlePart1.visible = false;
					this.RingMiddlePart2.visible = false;
					this.RingMiddlePart3.visible = false;
				}
				case 3 -> {
					this.RingUpperPart2.visible = true;
					this.RingUpperPart3.visible = true;
					this.RingMiddlePart0.visible = true;
					this.RingMiddlePart1.visible = false;
					this.RingMiddlePart2.visible = false;
					this.RingMiddlePart3.visible = false;
				}
				case 4 -> {
					this.RingUpperPart2.visible = true;
					this.RingUpperPart3.visible = true;
					this.RingMiddlePart0.visible = true;
					this.RingMiddlePart1.visible = true;
					this.RingMiddlePart2.visible = false;
					this.RingMiddlePart3.visible = false;
				}
				case 5 -> {
					this.RingUpperPart2.visible = true;
					this.RingUpperPart3.visible = true;
					this.RingMiddlePart0.visible = true;
					this.RingMiddlePart1.visible = true;
					this.RingMiddlePart2.visible = true;
					this.RingMiddlePart3.visible = false;
				}
				default -> {
					this.RingUpperPart2.visible = true;
					this.RingUpperPart3.visible = true;
					this.RingMiddlePart0.visible = true;
					this.RingMiddlePart1.visible = true;
					this.RingMiddlePart2.visible = true;
					this.RingMiddlePart3.visible = true;
				}
			}
		}

		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.rotate.startIfStopped(pEntity.tickCount);

		this.animate(this.rotate, BlazeAuraAnimation.ROTATE, pAgeInTicks);
	}
}