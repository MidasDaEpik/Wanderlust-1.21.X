package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.misc.ExplosionInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.joml.Vector4d;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Explosion.class)
public abstract class ExplosionMixin implements ExplosionInterface {
    @Mutable
    @Final
    @Shadow
    private final boolean fire;

    @Mutable
    @Final
    @Shadow
    private final Explosion.BlockInteraction blockInteraction;

    @Mutable
    @Final
    @Shadow
    private final Level level;

    @Mutable
    @Final
    @Shadow
    private final DamageSource damageSource;

    public ExplosionMixin(boolean fire, Explosion.BlockInteraction blockInteraction, Level level, DamageSource damageSource) {
        this.fire = fire;
        this.blockInteraction = blockInteraction;
        this.level = level;
        this.damageSource = damageSource;
    }

    @Unique
    public float wanderlust_1_21_X$entityExplosionRadius = -1;

    @Unique
    public float wanderlust_1_21_X$entityDamageCap = -1;

    @Unique
    public boolean wanderlust_1_21_X$damageItems = true;

    @Unique
    public TagKey<Block> wanderlust_1_21_X$damageableBlocks = null;

    @Unique
    public Vector4d wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier = null;

    @Unique
    public Vector4d wanderlust_1_21_X$nonDamageableBlockMinMaxLimitMultiplier = null;

    @Override
    public void set_wanderlust_1_21_X$updateExplosion(float pEntityExplosionRadius, float pEntityDamageCap, boolean pDamageItems, TagKey<Block> pDamageableBlocks, Vector4d pDamageableBlockMinMaxLimitMultiplier, Vector4d pNonDamageableBlockMinMaxLimitMultiplier) {
        wanderlust_1_21_X$entityExplosionRadius = pEntityExplosionRadius;
        wanderlust_1_21_X$entityDamageCap = pEntityDamageCap;
        wanderlust_1_21_X$damageItems = pDamageItems;
        wanderlust_1_21_X$damageableBlocks = pDamageableBlocks;
        wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier = pDamageableBlockMinMaxLimitMultiplier;
        wanderlust_1_21_X$nonDamageableBlockMinMaxLimitMultiplier = pNonDamageableBlockMinMaxLimitMultiplier;

    }

    @WrapOperation(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean explodeHurtEntity(Entity pEntity, DamageSource pDamageSource, float pAmount, Operation<Boolean> pOriginal) {
        if (wanderlust_1_21_X$entityExplosionRadius == 0 || (!wanderlust_1_21_X$damageItems && pEntity instanceof ItemEntity)) {
            return false;
        } else {
            if (wanderlust_1_21_X$entityDamageCap > 0) {
                return pOriginal.call(pEntity, pDamageSource, Math.min(pAmount, wanderlust_1_21_X$entityDamageCap));
            } else {
                return pOriginal.call(pEntity, pDamageSource, pAmount);
            }
        }
    }

    @WrapOperation(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ExplosionDamageCalculator;getEntityDamageAmount(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/entity/Entity;)F"))
    private float explodeDamage(ExplosionDamageCalculator pInstance, Explosion pExplosion, Entity pEntity, Operation<Float> pOriginal) {
        if (wanderlust_1_21_X$entityExplosionRadius <= 0) {
            return pOriginal.call(pInstance, pExplosion, pEntity);
        } else {
            Explosion pReExplosion = new Explosion(this.level, pExplosion.getDirectSourceEntity(), this.damageSource, pInstance, pExplosion.center().x, pExplosion.center().y, pExplosion.center().z, wanderlust_1_21_X$entityExplosionRadius, this.fire, this.blockInteraction, pExplosion.getSmallExplosionParticles(), pExplosion.getLargeExplosionParticles(), pExplosion.getExplosionSound());
            return pOriginal.call(pInstance, pReExplosion, pEntity);
        }
    }

    @WrapOperation(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ExplosionDamageCalculator;getBlockExplosionResistance(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Ljava/util/Optional;"))
    private Optional<Float> modifyBlockHardness(ExplosionDamageCalculator pInstance, Explosion pExplosion, BlockGetter pReader, BlockPos pBlockPos, BlockState pBlockState, FluidState pFluidState, Operation<Optional<Float>> pOriginal) {
        Optional<Float> pReturn = pOriginal.call(pInstance, pExplosion, pReader, pBlockPos, pBlockState, pFluidState);
        if (pReturn.isPresent()) {
            if (wanderlust_1_21_X$damageableBlocks != null) {
                if (pBlockState.is(wanderlust_1_21_X$damageableBlocks)) {
                    if (wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier != null) {
                        pReturn = pReturn.map(pValue -> pValue * (float) wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier.w)
                                .map(pValue -> pValue > wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier.z ? pValue : (float) Math.clamp(pValue, wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier.x, wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier.y));
                    }
                } else {
                    if (wanderlust_1_21_X$nonDamageableBlockMinMaxLimitMultiplier != null) {
                        pReturn = pReturn.map(pValue -> pValue * (float) wanderlust_1_21_X$nonDamageableBlockMinMaxLimitMultiplier.w)
                                .map(pValue -> pValue > wanderlust_1_21_X$nonDamageableBlockMinMaxLimitMultiplier.z ? pValue : (float) Math.clamp(pValue, wanderlust_1_21_X$nonDamageableBlockMinMaxLimitMultiplier.x, wanderlust_1_21_X$nonDamageableBlockMinMaxLimitMultiplier.y));
                    }
                }
            } else {
                if (wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier != null) {
                    pReturn = pReturn.map(pValue -> pValue *(float)  wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier.w)
                            .map(pValue -> pValue > wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier.z ? pValue : (float) Math.clamp(pValue, wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier.x, wanderlust_1_21_X$damageableBlockMinMaxLimitMultiplier.y));
                }
            }
        }
        return pReturn;
    }
}