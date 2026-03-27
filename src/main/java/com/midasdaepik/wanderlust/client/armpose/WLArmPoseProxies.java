package com.midasdaepik.wanderlust.client.armpose;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class WLArmPoseProxies {
    private static final IArmPoseTransformer CHARYBDIS_TRANSFORMER = (pModel, pLivingEntity, pArm) -> {
        boolean pRightHanded = pArm == HumanoidArm.RIGHT;
        ModelPart pModelPart1 = pRightHanded ? pModel.rightArm : pModel.leftArm;
        ModelPart pModelPart2 = pRightHanded ? pModel.leftArm : pModel.rightArm;

        pModel.rightArm.xRot = -1.9F;
        pModel.rightArm.yRot = -0.8F * (pRightHanded ? 1 : -1);

        pModel.leftArm.xRot = -1.9F;
        pModel.leftArm.yRot = 0.8F * (pRightHanded ? 1 : -1);
    };

    public static final EnumProxy<HumanoidModel.ArmPose> CHARYBDIS = new EnumProxy<>(HumanoidModel.ArmPose.class,
            true, CHARYBDIS_TRANSFORMER
    );
}
