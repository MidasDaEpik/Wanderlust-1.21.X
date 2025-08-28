package com.midasdaepik.wanderlust.effect;

import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;

public class DragonsAscension extends MobEffect {
    public DragonsAscension(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level() instanceof ServerLevel pServerLevel) {
            int pEffectTicks = pLivingEntity.getEffect(WLEffects.DRAGONS_ASCENSION).getDuration();
            if (pEffectTicks == 1) {
                if (pAmplifier == 0) {
                    pLivingEntity.addEffect(new MobEffectInstance(WLEffects.DRAGONS_ASCENSION, 240, 1, true, false, true));

                    AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
                    double pLivingEntityHalfY = pLivingEntitySize.getYsize() / 2;

                    pServerLevel.sendParticles(ParticleTypes.FLASH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 1, 0, 0, 0, 0);
                    pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 16, pLivingEntitySize.getXsize() / 2, pLivingEntityHalfY / 2, pLivingEntitySize.getZsize() / 2, 0.1);

                    pServerLevel.playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.EFFECT_DRAGONS_ASCENSION_ASCEND, SoundSource.PLAYERS, 2.0f, 1.1f,0);
                } else if (pAmplifier == 1) {
                    pLivingEntity.addEffect(new MobEffectInstance(WLEffects.DRAGONS_ASCENSION, 20, 2, true, false, true));

                    pLivingEntity.setDeltaMovement(0, -2, 0);
                    if (pLivingEntity instanceof ServerPlayer pServerPlayerLivingEntity) {
                        pServerPlayerLivingEntity.connection.send(new ClientboundSetEntityMotionPacket(pServerPlayerLivingEntity));
                    }

                    pServerLevel.sendParticles(
                            WLUtil.orientedCircleVec3dInput(new Vector3f(0.64f, 0.08f, 0.80f), 8,0.6f, 3.6f,  0, 1, 0),
                            pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntity.getBoundingBox().getYsize() / 2 + 1, pLivingEntity.getZ(), 1, 0, 0, 0, 0);

                    if (pLivingEntity instanceof Player pPlayer) {
                        pPlayer.getCooldowns().addCooldown(WLItems.DRAGONS_RAGE.get(), 800);
                    }

                } else if (pAmplifier == 2) {
                    groundSmash(pServerLevel, pLivingEntity, pLivingEntity.position());
                }
            } else {
                pLivingEntity.fallDistance = -1.0f;

                if (pAmplifier == 0) {
                    pServerLevel.sendParticles(new DustColorTransitionOptions(new Vector3f(0.82f,0.34f,0.92f), new Vector3f(0.25f,0.05f,0.48f), 1.5f),
                            pLivingEntity.getX() + Math.cos((double) pEffectTicks / 10 * Math.PI),
                            pLivingEntity.getY() + 2.25 - (double) pEffectTicks / 40,
                            pLivingEntity.getZ() + Math.sin((double) pEffectTicks / 10 * Math.PI), 1, 0, 0, 0, 0);
                    pServerLevel.sendParticles(new DustColorTransitionOptions(new Vector3f(0.82f,0.34f,0.92f), new Vector3f(0.25f,0.05f,0.48f), 1.5f),
                            pLivingEntity.getX() - Math.cos((double) pEffectTicks / 10 * Math.PI),
                            pLivingEntity.getY() + 2.25 - (double) pEffectTicks / 40,
                            pLivingEntity.getZ() - Math.sin((double) pEffectTicks / 10 * Math.PI), 1, 0, 0, 0, 0);

                    if (pEffectTicks % 20 == 0) {
                        pServerLevel.playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.EFFECT_DRAGONS_ASCENSION_CHIME, SoundSource.PLAYERS, 1.5f, 1f,0);
                    }

                } else if (pAmplifier == 1) {
                    if (pEffectTicks % 20 == 0) {
                        pServerLevel.playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.EFFECT_DRAGONS_ASCENSION_FLAP, SoundSource.PLAYERS, 1.0f, 1.2f,0);
                    }

                } else if (pAmplifier == 2) {
                    Vec3 pMoveVector = pLivingEntity.getDeltaMovement();
                    Vec3 pModMoveVector = WLUtil.checkCollidedVec3(pLivingEntity, pMoveVector);

                    if (!pModMoveVector.equals(pMoveVector)) {
                        Vec3 pLandingLocation = new Vec3(pLivingEntity.getX() + pModMoveVector.x, pLivingEntity.getY() + pLivingEntity.getBoundingBox().getYsize() / 2 + pModMoveVector.y, pLivingEntity.getZ() + pModMoveVector.z);

                        groundSmash(pServerLevel, pLivingEntity, pLandingLocation);
                    }
                }
            }
        }
        return true;
    }

    public void groundSmash(ServerLevel pServerLevel, LivingEntity pLivingEntity, Vec3 pSmashLocation) {
        pLivingEntity.removeEffect(WLEffects.DRAGONS_ASCENSION);

        final Vec3 AABBCenter = new Vec3(pSmashLocation.x, pSmashLocation.y, pSmashLocation.z);
        List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(3d), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
        for (LivingEntity pEntityIterator : pFoundTarget) {
            if (!(pEntityIterator == pLivingEntity)) {
                pEntityIterator.addEffect(new MobEffectInstance(WLEffects.PLUNGING, 120, 1));
                pEntityIterator.hurt(WLDamageSource.damageSource(pServerLevel, pLivingEntity, WLDamageSource.MAGIC), 30);

                Vec3 pDistVector = pEntityIterator.position().subtract(pLivingEntity.position());
                float pVecAmplifier = (float) Math.sqrt(pDistVector.x * pDistVector.x + pDistVector.y * pDistVector.y + pDistVector.z * pDistVector.z);
                pVecAmplifier = Math.clamp(3 - pVecAmplifier, 0, 1.5f) / pVecAmplifier * 2f;
                Vec3 pEntityVector = pEntityIterator.getDeltaMovement();

                pEntityIterator.setDeltaMovement(pEntityVector.x + pDistVector.x * pVecAmplifier, pEntityVector.y + pDistVector.y * pVecAmplifier + 0.3, pEntityVector.z + pDistVector.z * pVecAmplifier);
                if (pEntityIterator instanceof ServerPlayer pServerPlayerIterator) {
                    pServerPlayerIterator.connection.send(new ClientboundSetEntityMotionPacket(pServerPlayerIterator));
                }
            }
        }
        pServerLevel.sendParticles(ParticleTypes.FLASH, pSmashLocation.x, pSmashLocation.y, pSmashLocation.z, 1, 0, 0, 0, 0);
        pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pSmashLocation.x, pSmashLocation.y, pSmashLocation.z, 16, 0.1, 0.1, 0.1, 0.1);

        pServerLevel.sendParticles(
                WLUtil.orientedCircleVec3dInput(new Vector3f(0.64f, 0.08f, 0.80f), 12,0.6f, 5.6f,  0, 1, 0),
                pSmashLocation.x, pSmashLocation.y, pSmashLocation.z, 1, 0, 0, 0, 0);
        pServerLevel.sendParticles(
                WLUtil.orientedCircleVec3dInput(new Vector3f(0.64f, 0.08f, 0.80f), 12,0.6f, 5.6f,  1, 0, 0),
                pSmashLocation.x, pSmashLocation.y, pSmashLocation.z, 1, 0, 0, 0, 0);
        pServerLevel.sendParticles(
                WLUtil.orientedCircleVec3dInput(new Vector3f(0.64f, 0.08f, 0.80f), 12,0.6f, 5.6f,  0, 0, 1),
                pSmashLocation.x, pSmashLocation.y, pSmashLocation.z, 1, 0, 0, 0, 0);

        pServerLevel.playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.EFFECT_DRAGONS_ASCENSION_SMASH, SoundSource.PLAYERS, 2.0f, 1f,0);
    }
}