package com.midasdaepik.wanderlust.client.armpose;

import net.minecraft.client.model.HumanoidModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WLArmPose {
    public static HumanoidModel.ArmPose CHARYBDIS = HumanoidModel.ArmPose.valueOf("WANDERLUST_CHARYBDIS");
}
