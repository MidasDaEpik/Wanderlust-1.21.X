package com.midasdaepik.wanderlust.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal")
public abstract class PhantomSweepAttackGoalMixin {
    @Shadow(aliases = {"this$0"})
    private Phantom Phantom;

    @WrapMethod(method = "start")
    private void start(Operation<Void> pOriginal) {
        if (WLTweaksConfig.CONFIG.PhantomPhantasmal.get()) {
            Phantom.removeEffect(WLEffects.PHANTASMAL);
        }

        pOriginal.call();
    }

    @WrapMethod(method = "stop")
    private void stop(Operation<Void> pOriginal) {
        if (WLTweaksConfig.CONFIG.PhantomPhantasmal.get()) {
            Phantom.level().playSound(null, Phantom.getEyePosition().x, Phantom.getEyePosition().y, Phantom.getEyePosition().z, WLSounds.ENTITY_PHANTOM_PHANTASMAL, SoundSource.PLAYERS, 2f, 1.1f);
            if (Phantom.level() instanceof ServerLevel pServerLevel) {
                AABB pLivingEntitySize = Phantom.getBoundingBox();
                pServerLevel.sendParticles(ParticleTypes.SMOKE, Phantom.getX(), Phantom.getY(0.5), Phantom.getZ(), 24, pLivingEntitySize.getXsize() / 3, pLivingEntitySize.getYsize() / 3, pLivingEntitySize.getZsize() / 3, 0);
            }
            Phantom.addEffect(new MobEffectInstance(WLEffects.PHANTASMAL, 80, 0, false, false));
        }

        pOriginal.call();
    }
}