package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.client.armpose.WLArmPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {
    @Shadow public HumanoidModel.ArmPose rightArmPose;

    @Shadow public HumanoidModel.ArmPose leftArmPose;

    @WrapOperation(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V"))
    private void setupAnim(ModelPart pModelPart, float pAgeInTicks, float pMultiplier, Operation<Void> pOriginal) {
        if (!(this.rightArmPose == WLArmPose.CHARYBDIS) && !(this.leftArmPose == WLArmPose.CHARYBDIS)) {
            pOriginal.call(pModelPart, pAgeInTicks, pMultiplier);
        }
    }
}