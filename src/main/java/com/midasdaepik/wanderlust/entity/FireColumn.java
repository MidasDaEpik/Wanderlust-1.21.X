package com.midasdaepik.wanderlust.entity;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.RREntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
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

import java.util.Comparator;
import java.util.List;

public class FireColumn extends Projectile {
    public int duration = 240;
    public int delayduration = 40;

    public FireColumn(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireColumn(Level pLevel, LivingEntity pOwner, int pDuration, int pDelayDuration) {
        super(RREntities.FIRE_COLUMN.get(), pLevel);
        this.setOwner(pOwner);
        this.duration = pDuration;
        this.delayduration = pDelayDuration;
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
            }

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, ClipContext.Block.COLLIDER);
            if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x * 0.1;
            double d1 = this.getY() + vec3.y * 0.1;
            double d2 = this.getZ() + vec3.z * 0.1;
            ProjectileUtil.rotateTowardsMovement(this, 0.4F);

            this.setPos(d0, d1, d2);
        }
    }

    protected void AttackTick(ServerLevel pServerLevel) {
        if (this.delayduration > 0) {
            if (this.delayduration % 4 == 0) {
                particlePillar(pServerLevel, ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ());
            }

            if (this.delayduration % 10 == 0) {
                pServerLevel.playSeededSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.DISPENSER_FAIL, SoundSource.NEUTRAL, 1.5f, 1.6f,0);
            }

            this.delayduration -= 1;

        } else if (this.duration > 0) {
            if (this.duration % 4 == 0) {
                particlePillar(pServerLevel, ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ());
            }

            if (this.duration % 20 == 0) {
                pServerLevel.playSeededSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1.5f, 0.9f,0);
            }

            final Vec3 AABBCenter = new Vec3(this.getX(), this.getY(), this.getZ());
            List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(2d, 8d, 2d), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                if (this.duration % 20 == 0) {
                    pEntityIterator.hurt(new DamageSource(pServerLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "burn_no_cooldown"))), this.getOwner()), 6);
                    pEntityIterator.igniteForTicks(60);
                }

                if (this.duration % 10 == 0) {
                    double pXPush = pEntityIterator.getX() - this.getX();
                    double pZPush = pEntityIterator.getZ() - this.getZ();
                    double pIteratorDiagonal = Math.sqrt(Math.pow(pXPush, 2) + Math.pow(pZPush, 2));
                    pEntityIterator.setDeltaMovement(pEntityIterator.getDeltaMovement().add(pXPush / pIteratorDiagonal * 0.4, 0, pZPush / pIteratorDiagonal * 0.4));
                }
            }

            this.duration -= 1;

        } else {
            this.discard();
        }
    }

    public static void particlePillar(ServerLevel pServerLevel, ParticleOptions pParticle, double pX, double pY, double pZ) {
        for (int Loop = 1; Loop <= 33; Loop++) {
            pServerLevel.sendParticles(pParticle, pX, pY + (double) Loop / 2 - 8.5, pZ, 1, 0.6, 0.2, 0.6, 0);
        }
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.duration = pCompound.getInt("Duration");
        this.delayduration = pCompound.getInt("DelayDuration");
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("DelayDuration", this.delayduration);
    }
}
