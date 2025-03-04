package com.midasdaepik.wanderlust.entity;

import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEntities;
import com.midasdaepik.wanderlust.registries.WLUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
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
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private int duration = 160;
    private int durationOnUse = -20;
    private int attackDamage = 6;

    public DragonsBreath(EntityType<? extends DragonsBreath> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public DragonsBreath(Level pLevel, Vec3 pVec3) {
        this(WLEntities.DRAGONS_BREATH.get(), pLevel);
        this.setPos(pVec3.x, pVec3.y, pVec3.z);
    }

    public DragonsBreath(Level pLevel, LivingEntity pOwner, int pDuration, int pDurationOnUse, int pAttackDamage, Vec3 pVec3) {
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
            LivingEntity pOwner = this.getOwner();

            pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY() + 0.25, this.getZ(), 1, 0.8, 0.3, 0.8, 0);

            if (this.duration % 20 == 0) {
                final Vec3 AABBCenter = new Vec3(this.getX(), this.getY() + 0.25, this.getZ());
                List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(2.5, 1, 2.5), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
                for (LivingEntity pEntityIterator : pFoundTarget) {
                    boolean pSuccess = pEntityIterator.hurt(WLDamageSource.damageSource(pServerLevel, pOwner, WLDamageSource.MAGIC), this.attackDamage);
                    if (pSuccess) {
                        this.duration += this.durationOnUse;
                        if (this.duration <= 0) {
                            this.discard();
                        }
                    }
                }

                WLUtil.particleCircle(pServerLevel, ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 2.5, 2);
                WLUtil.particleCircle(pServerLevel, ParticleTypes.DRAGON_BREATH, this.getX(), this.getY() + 0.5, this.getZ(), 2.5, 2);
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
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }
        return this.owner;
    }

    public void setOwner(@Nullable LivingEntity owner) {
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