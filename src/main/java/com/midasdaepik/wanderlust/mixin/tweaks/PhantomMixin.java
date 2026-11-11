package com.midasdaepik.wanderlust.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends Mob {
    protected PhantomMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @WrapMethod(method = "aiStep")
    private void aiStep(Operation<Void> pOriginal) {
        if (WLTweaksConfig.CONFIG.PhantomDisappear.get()) {
            Phantom pThis = (Phantom) (Object) this;
            if (pThis.isAlive() && this.isSunBurnTick() && pThis.getHealth() / pThis.getMaxHealth() <= 0.7) {
                pThis.level().playSound(null, pThis.getEyePosition().x, pThis.getEyePosition().y, pThis.getEyePosition().z, WLSounds.ENTITY_PHANTOM_PHANTASMAL, SoundSource.PLAYERS, 2f, 1.1f);

                if (pThis.level() instanceof ServerLevel pServerLevel) {
                    AABB pLivingEntitySize = pThis.getBoundingBox();
                    pServerLevel.sendParticles(ParticleTypes.SMOKE, pThis.getX(), pThis.getY(0.5), pThis.getZ(), 24, pLivingEntitySize.getXsize() / 3, pLivingEntitySize.getYsize() / 3, pLivingEntitySize.getZsize() / 3, 0);
                }

                pThis.remove(RemovalReason.DISCARDED);
            }
        }
        pOriginal.call();
    }
}