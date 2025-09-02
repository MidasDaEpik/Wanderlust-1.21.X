package com.midasdaepik.wanderlust.entity;

import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class DragonsRageBreath extends Projectile {
    public int duration = 20;
    public int attackDamage = 8;

    public DragonsRageBreath(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    public DragonsRageBreath(Level pLevel, Entity pShooter, int pDuration) {
        super(WLEntities.DRAGONS_RAGE_BREATH.get(), pLevel);
        this.setOwner(pShooter);
        this.duration = pDuration;
    }

    public DragonsRageBreath(Level pLevel, Entity pShooter, int pDuration, int pAttackDamage) {
        super(WLEntities.DRAGONS_RAGE_BREATH.get(), pLevel);
        this.setOwner(pShooter);
        this.duration = pDuration;
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
        Entity pOwner = this.getOwner();
        if (this.level().isClientSide || (pOwner == null || !pOwner.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();

            if (this.level() instanceof ServerLevel pServerLevel) {
                this.duration -= 1;
                if (this.isInWater()) {
                    this.duration -= 2;
                }
                if (this.duration <= 0) {
                    this.discard();
                }
            }

            if (this.level().isClientSide) {
                if (this.level() instanceof ClientLevel pClientLevel) {
                    int XZDegrees = Mth.nextInt(RandomSource.create(), 1, 360);
                    float XZRange = Mth.nextFloat(RandomSource.create(), 0f, 0.5f);

                    pClientLevel.addParticle(ParticleTypes.DRAGON_BREATH, this.getX() + Mth.cos(XZDegrees) * XZRange, this.getY() + Mth.nextFloat(RandomSource.create(), 0.25f, 0.75f), this.getZ() + Math.sin(XZDegrees) * XZRange, this.getDeltaMovement().x * 1.5 + Mth.nextFloat(RandomSource.create(), -0.05f, 0.05f), this.getDeltaMovement().y * 1.5 + Mth.nextFloat(RandomSource.create(), -0.05f, 0.05f), this.getDeltaMovement().z * 1.5 + Mth.nextFloat(RandomSource.create(), -0.05f, 0.05f));
                }
            }

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, ClipContext.Block.COLLIDER);
            if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);

            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (this.level() instanceof ServerLevel pServerLevel) {
            Entity pTarget = pResult.getEntity();
            pTarget.hurt(WLDamageSource.damageSource(pServerLevel, this.getOwner(), WLDamageSource.MAGIC), this.attackDamage);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (this.level() instanceof ServerLevel pServerLevel) {
            this.discard();
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }

        d0 *= 64.0;
        return distance < d0 * d0;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.duration = pCompound.getInt("Duration");
        this.attackDamage = pCompound.getInt("AttackDamage");
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("AttackDamage", this.attackDamage);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity p_entity) {
        Entity entity = this.getOwner();
        int i = entity == null ? 0 : entity.getId();
        Vec3 vec3 = p_entity.getPositionBase();
        return new ClientboundAddEntityPacket(
                this.getId(),
                this.getUUID(),
                vec3.x(),
                vec3.y(),
                vec3.z(),
                p_entity.getLastSentXRot(),
                p_entity.getLastSentYRot(),
                this.getType(),
                i,
                p_entity.getLastSentMovement(),
                0.0
        );
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDeltaMovement(packet.getXa(), packet.getYa(), packet.getZa());
    }
}
