package com.midasdaepik.wanderlust.entity;

import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.particle.CubeOptions;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEntities;
import com.midasdaepik.wanderlust.registries.WLParticles;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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

public class SporeCloud extends Projectile {
    public int duration = 200;

    public SporeCloud(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SporeCloud(Level pLevel, LivingEntity pShooter, int pDuration) {
        super(WLEntities.SPORE_CLOUD.get(), pLevel);
        this.setOwner(pShooter);
        this.duration = pDuration;
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
            double x = this.getX() + vec3.x * 0.1;
            double y = this.getY() + vec3.y * 0.1;
            double z = this.getZ() + vec3.z * 0.1;
            ProjectileUtil.rotateTowardsMovement(this, 0.4F);
            this.setPos(x, y, z);

            if (pOwner != null) {
                Vec3 pMov = new Vec3(pOwner.getX() - this.getX(), pOwner.getY(0.5) - this.getY(0.5), pOwner.getZ() - this.getZ());
                double dx = vec3.x * 0.95 + pMov.x * 0.05;
                double dy = vec3.y * 0.95 + pMov.y * 0.05;
                double dz = vec3.z * 0.95 + pMov.z * 0.05;
                this.setDeltaMovement(dx, dy, dz);
            }
        }
    }

    protected void AttackTick(ServerLevel pServerLevel) {
        if (this.duration % 2 < 1) {
            final Vec3 AABBCenter = new Vec3(this.getX(), this.getY(0.5), this.getZ());
            List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter.x + 5, AABBCenter.y + 5, AABBCenter.z + 5, AABBCenter.x - 5, AABBCenter.y - 5, AABBCenter.z - 5), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                if (!(pEntityIterator == this.getOwner())) {
                    if (!pEntityIterator.hasEffect(WLEffects.SPORARUM) || (pEntityIterator.hasEffect(WLEffects.SPORARUM) && pEntityIterator.getEffect(WLEffects.SPORARUM).getDuration() < 10)) {
                        pEntityIterator.addEffect(new MobEffectInstance(WLEffects.SPORARUM, 30, 0));
                    }
                    if (!pEntityIterator.hasEffect(MobEffects.WITHER) || (pEntityIterator.hasEffect(MobEffects.WITHER) && pEntityIterator.getEffect(MobEffects.WITHER).getDuration() < 10)) {
                        pEntityIterator.addEffect(new MobEffectInstance(MobEffects.WITHER, 30, 1));
                    }
                }
            }
        }
        if (this.duration % 10 < 1) {
            WLUtil.particleSphere(pServerLevel, new DustColorTransitionOptions(new Vector3f(0.772f,0.203f,0.223f), new Vector3f(0.482f,0f,0f), 0.9f), this.getX(), this.getY(), this.getZ(), 5);
        }
        if (this.duration % 20 < 1) {
            pServerLevel.playSound(null, this.getX(), this.getY(0.5), this.getZ(), WLSounds.ITEM_MYCORIS_CLOUD, SoundSource.NEUTRAL, 1.0f, 1.1f);
        }
        pServerLevel.sendParticles(WLParticles.SPORE.get(), this.getX(), this.getY(0.5), this.getZ(), 1, 1.25, 1.25, 1.25, 0.03);
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.duration = pCompound.getInt("Duration");
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Duration", this.duration);
    }
}
