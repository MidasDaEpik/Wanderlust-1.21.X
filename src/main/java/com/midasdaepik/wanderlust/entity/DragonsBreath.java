package com.midasdaepik.wanderlust.entity;

import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class DragonsBreath extends Entity implements TraceableEntity {
    @Nullable
    private Entity owner;
    @Nullable
    private UUID ownerUUID;
    private int duration = 160;
    private int durationOnUse = -10;
    private int attackDamage = 6;

    public DragonsBreath(EntityType<? extends DragonsBreath> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public DragonsBreath(Level pLevel, Vec3 pVec3) {
        this(WLEntities.DRAGONS_BREATH.get(), pLevel);
        this.setPos(pVec3.x, pVec3.y, pVec3.z);
    }

    public DragonsBreath(Level pLevel, Entity pOwner, int pDuration, int pDurationOnUse, int pAttackDamage, Vec3 pVec3) {
        this(pLevel, pVec3);
        this.owner = pOwner;
        this.duration = pDuration;
        this.durationOnUse = pDurationOnUse;
        this.attackDamage = pAttackDamage;
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
        super.tick();

        if (this.level() instanceof ServerLevel pServerLevel) {
            Entity pOwner = this.getOwner();

            pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY() + 0.25, this.getZ(), 2, 1.2, 0.3, 1.2, 0);

            if (this.duration % 20 == 0) {
                final Vec3 AABBCenter = new Vec3(this.getX(), this.getY() + 0.25, this.getZ());
                List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(3.5, 1.5, 3.5), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
                for (LivingEntity pEntityIterator : pFoundTarget) {
                    boolean pSuccess = pEntityIterator.hurt(WLDamageSource.damageSource(pServerLevel, pOwner, WLDamageSource.MAGIC), this.attackDamage);
                    pEntityIterator.addEffect(new MobEffectInstance(WLEffects.PLUNGING, 100, 0));
                    if (pSuccess) {
                        this.duration += this.durationOnUse;
                        if (this.duration <= 0) {
                            this.discard();
                        }
                    }
                }

                WLUtil.particleCircle(pServerLevel, ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 3.5, 2);
                WLUtil.particleCircle(pServerLevel, ParticleTypes.DRAGON_BREATH, this.getX(), this.getY() + 0.5, this.getZ(), 3.5, 2);
            }

            this.duration -= 1;
            if (this.duration <= 0) {
                this.discard();
            }
        }
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Nullable
    public Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else {
            if (this.ownerUUID != null) {
                if (this.level() instanceof ServerLevel pServerLevel) {
                    this.owner = pServerLevel.getEntity(this.ownerUUID);
                    return this.owner;
                }
            }

            return null;
        }
    }

    public void setOwner(@Nullable Entity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    public int getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }


    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("Age");
        this.duration = pCompound.getInt("Duration");
        this.durationOnUse = pCompound.getInt("DurationOnUse");
        this.attackDamage = pCompound.getInt("AttackDamage");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Age", this.tickCount);
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("DurationOnUse", this.durationOnUse);
        pCompound.putInt("AttackDamage", this.attackDamage);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }
}