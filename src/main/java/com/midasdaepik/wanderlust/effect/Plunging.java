package com.midasdaepik.wanderlust.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class Plunging extends MobEffect {
    public Plunging(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.getDeltaMovement().y() <= -0.1) {
            pLivingEntity.setDeltaMovement(new Vec3((pLivingEntity.getDeltaMovement().x()), (pLivingEntity.getDeltaMovement().y() - (pAmplifier + 1) * 0.04), (pLivingEntity.getDeltaMovement().z())));
            if (pLivingEntity.fallDistance >= 1f) {
                pLivingEntity.fallDistance = pLivingEntity.fallDistance + (pAmplifier + 1) * 0.5f;
            }
        }
        return true;
    }
}
