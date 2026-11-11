package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.config.WLAbilitiesConfig;
import com.midasdaepik.wanderlust.networking.DragonChargeSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnchantmentEffects;
import com.midasdaepik.wanderlust.registries.WLParticles;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import java.util.Comparator;
import java.util.List;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
    //Arrow Weapon Type
    //0 -> Whisperwind
    //1 -> Dragon's Breath Arbalest
    //2 -> Sovereign

    //Arrow Modifier Type
    //0 -> Homing Arrow

    @Shadow
    protected boolean inGround;

    @Shadow
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;

    protected AbstractArrowMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    private void init(EntityType pEntityType, LivingEntity pOwner, Level pLevel, ItemStack pPickupItemStack, ItemStack pFiredFromWeapon, CallbackInfo pCallbackInfo) {
        AbstractArrow pThis = (AbstractArrow) (Object) this;
        if (EnchantmentHelper.has(pFiredFromWeapon, WLEnchantmentEffects.HOMING_ARROW.get())) {
            if (pOwner instanceof Player pPlayer) {
                List<LivingEntity> pFoundTarget = pLevel.getEntitiesOfClass(LivingEntity.class, pOwner.getBoundingBox().inflate(256d),
                                pFoundTargetEntity -> {
                    Vec3 pLookAngle = pPlayer.getLookAngle();
                    Vec3 pEnemyAngle = new Vec3(pFoundTargetEntity.getX() - pPlayer.getX(), pFoundTargetEntity.getEyeY() - pPlayer.getEyeY(), pFoundTargetEntity.getZ() - pPlayer.getZ());
                    double pDistance = Math.sqrt(pEnemyAngle.x * pEnemyAngle.x + pEnemyAngle.y * pEnemyAngle.y + pEnemyAngle.z * pEnemyAngle.z);
                    pEnemyAngle = new Vec3(pEnemyAngle.x / pDistance, pEnemyAngle.y / pDistance, pEnemyAngle.z / pDistance);
                    double pDotProduct = pLookAngle.x * pEnemyAngle.x + pLookAngle.y * pEnemyAngle.y + pLookAngle.z * pEnemyAngle.z;
                    return pDotProduct > 1.0 - 0.015 * pFoundTargetEntity.getBoundingBox().getSize() / pDistance && wanderlust_1_21_X$hasLineOfSight(pPlayer, pFoundTargetEntity);
                                })
                        .stream().sorted(Comparator.comparingDouble(pDistance -> pDistance.distanceToSqr(new Vec3(pOwner.getX(), pOwner.getY(0.5), pOwner.getZ())))).toList();

                if (!pFoundTarget.isEmpty()) {
                    pThis.setData(HOMING_ARROW_TARGET, pFoundTarget.getFirst().getId());
                    pThis.setData(SPECIAL_ARROW_MODIFIER_TYPE, 0);
                    pPlayer.getCooldowns().addCooldown(pFiredFromWeapon.getItem(), 20);
                }

            } else if (pOwner instanceof Mob pMob) {
                if (pMob.getTarget() != null) {
                    pThis.setData(HOMING_ARROW_TARGET, pMob.getTarget().getId());
                    pThis.setData(SPECIAL_ARROW_MODIFIER_TYPE, 0);
                }
            }
        }
    }

    @Unique
    public boolean wanderlust_1_21_X$hasLineOfSight(Entity pViewer, Entity pTarget) {
        if (pTarget.level() != pViewer.level()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(pViewer.getX(), pViewer.getEyeY(), pViewer.getZ());
            Vec3 vec31 = new Vec3(pTarget.getX(), pTarget.getEyeY(), pTarget.getZ());
            return !(vec31.distanceTo(vec3) > 256d) && pViewer.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pViewer)).getType() == HitResult.Type.MISS;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void specialArrowTick(CallbackInfo pCallbackInfo) {
        AbstractArrow pThis = (AbstractArrow) (Object) this;
        if (pThis.level() instanceof ServerLevel pServerLevel && pThis.hasData(SPECIAL_ARROW_WEAPON_TYPE)) {
            switch (pThis.getData(SPECIAL_ARROW_WEAPON_TYPE)) {
                case 1 -> {
                    if (!this.inGround) {
                        pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pThis.getX(), pThis.getY(), pThis.getZ(), 1, 0, 0, 0, 0);
                    }
                }
            }
        }
        if (pThis.hasData(SPECIAL_ARROW_MODIFIER_TYPE)) {
            switch (pThis.getData(SPECIAL_ARROW_MODIFIER_TYPE)) {
                case 0 -> {
                    if (pThis.level() instanceof ServerLevel && !this.inGround && pThis.hasData(HOMING_ARROW_TARGET) && pThis.level().getEntity(pThis.getData(HOMING_ARROW_TARGET)) instanceof LivingEntity pTarget) {
                        Vec3 pMovement = pThis.getDeltaMovement();
                        double pSpeed = Math.max(pMovement.length(), 0.8);

                        Vec3 pVector = new Vec3(pTarget.getX() - pThis.getX(), pTarget.getEyeY() - pThis.getEyeY(), pTarget.getZ() - pThis.getZ());
                        pVector = pVector.normalize().multiply(pSpeed * 0.2, pSpeed * 0.2, pSpeed * 0.2);

                        pThis.setDeltaMovement(pMovement.multiply(0.8, 0.8, 0.8).add(pVector));
                        if (!pThis.isNoGravity()) {
                            pThis.setNoGravity(true);
                        }
                        if (pThis.level() instanceof ServerLevel) {
                            pThis.hasImpulse = true;
                        }
                    } else {
                        if (pThis.isNoGravity()) {
                            pThis.setNoGravity(false);
                        }
                    }
                }
            }
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z"))
    private boolean tickIsCritArrow(AbstractArrow pAbstractArrow, Operation<Boolean> pOriginal) {
        AbstractArrow pThis = (AbstractArrow) (Object) this;
        if (pThis.hasData(SPECIAL_ARROW_MODIFIER_TYPE) && pThis.getData(SPECIAL_ARROW_MODIFIER_TYPE) == 0) {
            return false;
        } else {
            return pOriginal.call(pAbstractArrow);
        }
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void spawnSpecialArrowOnHitEntity(EntityHitResult pResult, CallbackInfo pCallbackInfo) {
        AbstractArrow pThis = (AbstractArrow) (Object) this;
        if (pThis.level() instanceof ServerLevel pServerLevel && pThis.hasData(SPECIAL_ARROW_WEAPON_TYPE)) {
            switch (pThis.getData(SPECIAL_ARROW_WEAPON_TYPE)) {
                case 0 -> {
                    pServerLevel.sendParticles(ParticleTypes.GUST_EMITTER_SMALL, pThis.getX(), pThis.getY(), pThis.getZ(), 1, 0, 0, 0, 0);

                }
                case 1 -> {
                    if (pResult.getEntity() instanceof LivingEntity pTarget) {
                        pTarget.addEffect(new MobEffectInstance(WLEffects.PLUNGING, 140, 0));
                    }

                    if (pThis.getOwner() instanceof ServerPlayer pServerPlayer) {
                        int DragonCharge = pServerPlayer.getData(DRAGON_CHARGE);
                        int DragonChargeCap = WLAbilitiesConfig.CONFIG.DragonChargeCap.get();

                        if (DragonCharge < DragonChargeCap) {
                            if (this.piercingIgnoreEntityIds == null || (this.piercingIgnoreEntityIds != null && this.piercingIgnoreEntityIds.isEmpty())) {
                                DragonCharge = Math.min(DragonCharge + WLAbilitiesConfig.CONFIG.DragonChargeOnRangedHit.get(), DragonChargeCap);
                            } else {
                                DragonCharge = Math.min(DragonCharge + WLAbilitiesConfig.CONFIG.DragonChargeOnRangedHitConsecutive.get(), DragonChargeCap);
                            }
                            pServerPlayer.setData(DRAGON_CHARGE, DragonCharge);
                            PacketDistributor.sendToPlayer(pServerPlayer, new DragonChargeSyncS2CPacket(DragonCharge));
                        }
                    }
                }
                case 2 -> {
                    if (pResult.getEntity() instanceof LivingEntity pTarget) {
                        pServerLevel.sendParticles(WLParticles.MARKED.get(), pTarget.getX(), pTarget.getY() + pTarget.getBoundingBox().getYsize() + 0.5, pTarget.getZ(), 1, 0, 0, 0, 0);
                        pTarget.addEffect(new MobEffectInstance(WLEffects.MARKED, 120, 0, false, false, true));
                    }
                }
            }
        }
    }

    @Inject(method = "onHitBlock", at = @At("HEAD"))
    private void spawnSpecialArrowOnHitBlock(BlockHitResult pResult, CallbackInfo pCallbackInfo) {
        AbstractArrow pThis = (AbstractArrow) (Object) this;
        if (pThis.level() instanceof ServerLevel pServerLevel && pThis.hasData(SPECIAL_ARROW_WEAPON_TYPE)) {
            switch (pThis.getData(SPECIAL_ARROW_WEAPON_TYPE)) {
                case 0 -> {
                    pServerLevel.sendParticles(ParticleTypes.GUST_EMITTER_SMALL, pThis.getX(), pThis.getY(), pThis.getZ(), 1, 0, 0, 0, 0);
                }
            }
        }
    }
}