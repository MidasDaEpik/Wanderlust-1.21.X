package com.midasdaepik.wanderlust.effect;

import com.midasdaepik.wanderlust.registries.WLParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class Marked extends MobEffect {
    public Marked(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration % 4 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level pLevel = pLivingEntity.level();
        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            pServerLevel.sendParticles(WLParticles.MARK_EFFECT_PARTICLE.get(), pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 4, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 4, 0.01);
        }
        return true;
    }
}
