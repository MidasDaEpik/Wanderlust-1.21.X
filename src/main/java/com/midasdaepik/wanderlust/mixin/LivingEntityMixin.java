package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.ECHO_STORED_DAMAGE;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PYROSWEEP_DASH;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onEffectAdded", at = @At("HEAD"))
    private void onEffectAdded(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfo pCallbackInfo) {
        LivingEntity pThis = (LivingEntity) (Object) this;

        if (pEffectInstance.getEffect().value() == WLEffects.ECHO.value()) {
            pThis.setData(ECHO_STORED_DAMAGE, 0.0f);
        }
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    private void onEffectRemoved(MobEffectInstance pEffectInstance, CallbackInfo pCallbackInfo) {
        LivingEntity pThis = (LivingEntity) (Object) this;

        if (pEffectInstance.getEffect().value() == WLEffects.ECHO.value()) {
            float pEchoDamage = pThis.getData(ECHO_STORED_DAMAGE);

            if (pEchoDamage > 0.0f) {
                Level pLevel = pThis.level();

                if (pLevel instanceof ServerLevel pServerLevel) {
                    pThis.hurt(WLDamageSource.damageSource(pServerLevel, WLDamageSource.ECHO), pEchoDamage);

                    AABB pSize = pThis.getBoundingBox();
                    pServerLevel.sendParticles(ParticleTypes.SCULK_CHARGE_POP, pThis.getX(), pThis.getY() + pSize.getYsize() / 2, pThis.getZ(), 16, pSize.getXsize() / 2, pSize.getYsize() / 4, pSize.getZsize() / 2, 0);
                    pServerLevel.sendParticles(ParticleTypes.OMINOUS_SPAWNING, pThis.getX(), pThis.getY() + pSize.getYsize() / 2, pThis.getZ(), 8, pSize.getXsize() / 2, pSize.getYsize() / 4, pSize.getZsize() / 2, 0);
                    pServerLevel.sendParticles(ParticleTypes.SONIC_BOOM, pThis.getX(), pThis.getY() + pSize.getYsize() / 2, pThis.getZ(), 1, 0, 0, 0, 0);
                }

                pLevel.playSeededSound(null, pThis.getX(), pThis.getY(), pThis.getZ(), WLSounds.EFFECT_ECHO_RELEASE, SoundSource.MASTER, 1f, 1.3f, 0);

                pThis.setData(ECHO_STORED_DAMAGE, 0.0f);
            }
        }
    }

    @Inject(method = "shouldDiscardFriction", at = @At("HEAD"), cancellable = true)
    private void shouldDiscardFriction(CallbackInfoReturnable<Boolean> pCallbackInfo) {
        LivingEntity pThis = (LivingEntity) (Object) this;
        if (pThis instanceof Player && pThis.getData(PYROSWEEP_DASH) > 0) {
            pCallbackInfo.setReturnValue(true);
        }
    }
}