package com.midasdaepik.wanderlust.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

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
        if (pLivingEntity.getDeltaMovement().y() >= 0.8 || pLivingEntity.getDeltaMovement().y() <= -0.1) {
            pLivingEntity.setDeltaMovement(pLivingEntity.getDeltaMovement().add(0, - (pAmplifier + 1) * 0.05, 0));
        }
        return true;
    }
}
