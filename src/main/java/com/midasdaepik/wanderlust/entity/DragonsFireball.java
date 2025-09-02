package com.midasdaepik.wanderlust.entity;

import com.midasdaepik.wanderlust.registries.WLEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DragonsFireball extends AbstractHurtingProjectile {
    public int despawnDuration = 80;

    public DragonsFireball(EntityType<? extends DragonsFireball> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public DragonsFireball(Level pLevel, LivingEntity pShooter, Vec3 pVec3) {
        super(WLEntities.DRAGONS_FIREBALL.get(), pShooter, pVec3, pLevel);
    }

    public DragonsFireball(Level pLevel, LivingEntity pShooter, Vec3 pVec3, int pDespawnDuration) {
        this(pLevel, pShooter, pVec3);
        this.despawnDuration = pDespawnDuration;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel pServerLevel) {
            if (this.despawnDuration <= 0) {
                Vec3 pSpawnLocation = new Vec3(this.getX(), this.getY(), this.getZ());

                detonate(pServerLevel, pSpawnLocation);
            }

            this.despawnDuration -= 1;

        } else if (this.level() instanceof ClientLevel pClientLevel) {
            for (int Loop = 1; Loop <= 2; Loop++) {
                pClientLevel.addParticle(new DustColorTransitionOptions(new Vector3f(0.82f,0.34f,0.92f), new Vector3f(0.25f,0.05f,0.48f), 1.5f),
                        this.getX() + Mth.nextFloat(RandomSource.create(), -0.5f, 0.5f),
                        this.getY() + Mth.nextFloat(RandomSource.create(), 0f, 1.0f),
                        this.getZ() + Mth.nextFloat(RandomSource.create(), -0.5f, 0.5f),
                        0, 0, 0);
            }
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (this.level() instanceof ServerLevel pServerLevel) {
            if (pResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult pEntityHitResult = (EntityHitResult) pResult;
                Entity pTarget = pEntityHitResult.getEntity();
                Vec3 pSpawnLocation = new Vec3(pTarget.getX(), pTarget.getY(), pTarget.getZ());

                detonate(pServerLevel, pSpawnLocation);

            } else if (pResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult pBlockHitResult = (BlockHitResult) pResult;
                Vec3 pSpawnLocation = pBlockHitResult.getLocation();

                detonate(pServerLevel, pSpawnLocation);
            }
        }
    }

    public void detonate(ServerLevel pServerLevel, Vec3 pSpawnLocation){
        pServerLevel.playSeededSound(null, pSpawnLocation.x, pSpawnLocation.y, pSpawnLocation.z, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.NEUTRAL, 1.6f, 1.2f, 0);

        DragonsBreath dragonsBreath = new DragonsBreath(pServerLevel, this.getOwner(), 160, -10, 6, pSpawnLocation);
        pServerLevel.addFreshEntity(dragonsBreath);

        this.discard();
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    protected float getInertia() {
        return 0.9F;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.despawnDuration = pCompound.getInt("DespawnDuration");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("DespawnDuration", this.despawnDuration);
    }
}
