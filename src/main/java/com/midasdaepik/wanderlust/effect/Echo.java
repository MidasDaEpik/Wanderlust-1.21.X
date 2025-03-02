package com.midasdaepik.wanderlust.effect;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Echo extends MobEffect {
    public Echo(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration == 1;
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level() instanceof ServerLevel pServerLevel) {
            pLivingEntity.hurt(new DamageSource(pServerLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "echo")))), (pAmplifier + 1) * 4);

            pServerLevel.sendParticles(ParticleTypes.SCULK_CHARGE_POP, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 16, 0.4, 0.4, 0.4, 0);
            pServerLevel.sendParticles(ParticleTypes.OMINOUS_SPAWNING, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 8, 0.4, 0.4, 0.4, 0);
            pServerLevel.sendParticles(ParticleTypes.SONIC_BOOM, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 1, 0, 0, 0, 0);

        }
        pLivingEntity.level().playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.PLAYERS, 1f, 1.3f, 0);

        return true;
    }
}
