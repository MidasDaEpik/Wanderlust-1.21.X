package com.midasdaepik.wanderlust.mixin.weapon_extensions;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.midasdaepik.wanderlust.item.extensions.HitParticleExtension;
import com.midasdaepik.wanderlust.item.extensions.OnHitExtension;
import com.midasdaepik.wanderlust.item.extensions.WLHitParticleUtil;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @WrapOperation(method = "sweepAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    private <T extends ParticleOptions> int sweep(ServerLevel pServerLevel, T pParticle, double pPosX, double pPosY, double pPosZ, int pCount, double pXOffset, double pYOffSet, double pZOffset, double pSpeed, Operation<Integer> pOriginal) {
        Player pThis = (Player) (Object) this;
        ItemStack pItemStack = pThis.getWeaponItem();
        if (pItemStack.getItem() instanceof HitParticleExtension pHitParticleExtension) {
            return 0;
        } else {
            return pOriginal.call(pServerLevel, pParticle, pPosX, pPosY, pPosZ, pCount, pXOffset, pYOffSet, pZOffset, pSpeed);
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    private void attack(Entity pTarget, CallbackInfo pCallbackInfo, @Local(ordinal = 0) ItemStack pItemStack, @Local(ordinal = 3) boolean pIsSweep, @Local(ordinal = 2) boolean pIsCrit, @Local(ordinal = 0) boolean pIsStrongHit) {
        Player pThis = (Player) (Object) this;
        if (pThis.level() instanceof ServerLevel pServerLevel) {
            if (pIsSweep) {
                //Sweep
                if (pItemStack.getItem() instanceof OnHitExtension pOnHitExtension) {
                    pOnHitExtension.onSweepHit(pServerLevel, pThis, pItemStack);
                }
                if (pItemStack.getItem() instanceof HitParticleExtension pHitParticleExtension) {
                    pHitParticleExtension.particleSweepHit(pServerLevel, pThis, pItemStack);
                } else if (pItemStack.is(WLTags.CUSTOM_SWEEP_PARTICLE_WEAPONS)) {
                    if (pItemStack.is(WLTags.WEAPON_MODIFIER_PIERCE)) {
                        WLHitParticleUtil.thrustHit(pServerLevel, pThis, pItemStack);
                    } else {
                        WLHitParticleUtil.sweepHit(pServerLevel, pThis, pItemStack);
                    }
                }

            } else {
                if (pIsCrit) {
                    //Crit
                    if (pItemStack.getItem() instanceof OnHitExtension pOnHitExtension) {
                        pOnHitExtension.onCritHit(pServerLevel, pThis, pItemStack);
                    }
                    if (pItemStack.getItem() instanceof HitParticleExtension pHitParticleExtension) {
                        pHitParticleExtension.particleCritHit(pServerLevel, pThis, pItemStack);
                    } else if (pItemStack.is(WLTags.CUSTOM_SWEEP_PARTICLE_WEAPONS)) {
                        WLHitParticleUtil.critHit(pServerLevel, pThis, pItemStack);
                    }

                } else {
                    if (pIsStrongHit) {
                        //Strong Hit
                        if (pItemStack.getItem() instanceof OnHitExtension pOnHitExtension) {
                            pOnHitExtension.onStrongHit(pServerLevel, pThis, pItemStack);
                        }
                        if (pItemStack.getItem() instanceof HitParticleExtension pHitParticleExtension) {
                            pHitParticleExtension.particleStrongHit(pServerLevel, pThis, pItemStack);
                        } else if (pItemStack.is(WLTags.CUSTOM_SWEEP_PARTICLE_WEAPONS)) {
                            if (pItemStack.is(WLTags.WEAPON_MODIFIER_CLEAVE)) {
                                WLHitParticleUtil.sweepHit(pServerLevel, pThis, pItemStack);
                            } else {
                                WLHitParticleUtil.thrustHit(pServerLevel, pThis, pItemStack);
                            }
                        }

                    } else {
                        //Weak Hit
                        if (pItemStack.getItem() instanceof OnHitExtension pOnHitExtension) {
                            pOnHitExtension.onWeakHit(pServerLevel, pThis, pItemStack);
                        }
                        if (pItemStack.getItem() instanceof HitParticleExtension pHitParticleExtension) {
                            pHitParticleExtension.particleWeakHit(pServerLevel, pThis, pItemStack);
                        } else if (pItemStack.is(WLTags.CUSTOM_SWEEP_PARTICLE_WEAPONS)) {

                        }

                    }
                }
            }
            if (pItemStack.getItem() instanceof OnHitExtension pOnHitExtension) {
                pOnHitExtension.onHit(pServerLevel, pThis, pItemStack);
            }
        }
    }
}