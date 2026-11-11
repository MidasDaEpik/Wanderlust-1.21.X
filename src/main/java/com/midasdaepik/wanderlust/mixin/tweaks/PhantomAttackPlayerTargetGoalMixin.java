package com.midasdaepik.wanderlust.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.INSOMNIA;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PHANTOM_ANGRY_AT;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomAttackPlayerTargetGoal")
public abstract class PhantomAttackPlayerTargetGoalMixin {
    @WrapOperation(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Phantom;canAttack(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;)Z"))
    private boolean canUseCanAttack(Phantom pPhantom, LivingEntity pLivingEntity, TargetingConditions pTargetingConditions, Operation<Boolean> pOriginal) {
        if (WLTweaksConfig.CONFIG.PhantomSpawningReworkToggle.get() && WLTweaksConfig.CONFIG.PhantomDoNotTargetLowInsomnia.get()
                && pLivingEntity.getData(INSOMNIA) <= 8 * 24000
                && (!pPhantom.hasData(PHANTOM_ANGRY_AT) || (pPhantom.hasData(PHANTOM_ANGRY_AT) && !pPhantom.getData(PHANTOM_ANGRY_AT).equals(pLivingEntity.getUUID())))) {
            return false;
        } else {
            return pOriginal.call(pPhantom, pLivingEntity, pTargetingConditions);
        }
    }
}