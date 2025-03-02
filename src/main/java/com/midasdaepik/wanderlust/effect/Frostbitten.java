package com.midasdaepik.wanderlust.effect;

import com.midasdaepik.wanderlust.registries.RREffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class Frostbitten extends MobEffect {
    public Frostbitten(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.isOnFire()) {
            int pFireTicks = pLivingEntity.getRemainingFireTicks();
            int pFreezeTicks = pLivingEntity.getEffect(RREffects.FROSBITTEN).getDuration();
            if (pFreezeTicks % 20 == 0) {
                if (pFireTicks > pFreezeTicks) {
                    pLivingEntity.setRemainingFireTicks(pFireTicks - pFreezeTicks);
                    pLivingEntity.removeEffect(RREffects.FROSBITTEN);
                } else if (pFireTicks == pFreezeTicks) {
                    pLivingEntity.clearFire();
                    pLivingEntity.removeEffect(RREffects.FROSBITTEN);
                } else {
                    pLivingEntity.clearFire();
                    pLivingEntity.removeEffect(RREffects.FROSBITTEN);
                    pLivingEntity.addEffect(new MobEffectInstance(RREffects.FROSBITTEN, pFreezeTicks - pFireTicks, pAmplifier, true, true));
                }
            }
        } else {
            if (pLivingEntity.getTicksFrozen() < 180) {
                int pFreezeTicks = pLivingEntity.getTicksFrozen() + 4 + pAmplifier * 2;
                if (pFreezeTicks > 180) {
                    pFreezeTicks = 180;
                }
                pLivingEntity.setTicksFrozen(pFreezeTicks);
            }
        }
        return true;
    }
}
