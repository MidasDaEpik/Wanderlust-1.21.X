package com.midasdaepik.wanderlust.entity;

import com.midasdaepik.wanderlust.registries.RREntities;
import com.midasdaepik.wanderlust.registries.RRUtil;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;

public class Firestorm extends Projectile {
    public int duration = 200;
    public int effectDuration = 40;
    public boolean witherSpore = false;

    public Firestorm(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public Firestorm(Level pLevel, LivingEntity pShooter, int pDuration, int pEffectDuration, boolean pWitherSpore) {
        super(RREntities.FIRESTORM.get(), pLevel);
        this.setOwner(pShooter);
        this.duration = pDuration;
        this.effectDuration = pEffectDuration;
        this.witherSpore = pWitherSpore;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public void tick() {
        Entity pOwner = this.getOwner();
        if (this.level().isClientSide || (pOwner == null || !pOwner.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();

            if (this.level() instanceof ServerLevel pServerLevel) {
                this.AttackTick(pServerLevel);

                this.duration -= 1;
                if (this.duration <= 0) {
                    this.discard();
                }
            }

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, ClipContext.Block.COLLIDER);
            if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x * 0.1;
            double d1 = this.getY() + vec3.y * 0.05;
            double d2 = this.getZ() + vec3.z * 0.1;
            ProjectileUtil.rotateTowardsMovement(this, 0.4F);

            this.setPos(d0, d1, d2);
        }
    }

    protected void AttackTick(ServerLevel pServerLevel) {
        if (this.witherSpore) {
            pServerLevel.sendParticles(ParticleTypes.CRIMSON_SPORE, this.getX(), this.getY() + 0.25, this.getZ(), 2, 1.6, 1.6, 1.6, 0.02);
            pServerLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, this.getX(), this.getY() + 0.25, this.getZ(), 1, 1.6, 1.6, 1.6, 0.02);
        } else {
            pServerLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY() + 0.25, this.getZ(), 2, 1.6, 1.6, 1.6, 0.02);
            pServerLevel.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.25, this.getZ(), 1, 1.6, 1.6, 1.6, 0.02);
        }

        if (this.duration % 20 == 0) {
            pServerLevel.playSeededSound(null, this.getX(), this.getY() + 0.25, this.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1f, 0.9f,0);

            final Vec3 AABBCenter = new Vec3(this.getX(), this.getY() + 0.25, this.getZ());
            List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(5d), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                if (pEntityIterator != this.getOwner()) {
                    if (this.witherSpore) {
                        pEntityIterator.addEffect(new MobEffectInstance(MobEffects.WITHER, this.effectDuration, 2));
                        pEntityIterator.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, this.effectDuration, 0));
                    } else {
                        pEntityIterator.igniteForTicks(this.effectDuration);
                        pEntityIterator.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, this.effectDuration, 0));
                    }
                }
            }

            if (this.witherSpore) {
                RRUtil.particleSphere(pServerLevel, new DustColorTransitionOptions(new Vector3f(0.772f,0.203f,0.223f), new Vector3f(0.482f,0f,0f), 0.9f), this.getX(), this.getY(), this.getZ(), 5);
            } else {
                RRUtil.particleSphere(pServerLevel, ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 5);
            }
        }
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.duration = pCompound.getInt("Duration");
        this.effectDuration = pCompound.getInt("EffectDuration");
        this.witherSpore = pCompound.getBoolean("WitherSpore");
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("EffectDuration", this.effectDuration);
        pCompound.putBoolean("WitherSpore", this.witherSpore);
    }
}
