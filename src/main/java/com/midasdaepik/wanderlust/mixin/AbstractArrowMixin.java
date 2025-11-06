package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.config.WLCommonConfig;
import com.midasdaepik.wanderlust.networking.DragonChargeSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.WLEffects;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.DRAGON_CHARGE;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.SPECIAL_ARROW_TYPE;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Shadow
    protected boolean inGround;

    @Shadow
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;

    @Inject(method = "tick", at = @At("HEAD"))
    private void specialArrowTick(CallbackInfo pCallbackInfo) {
        AbstractArrow pThis = (AbstractArrow) (Object) this;
        switch (pThis.getData(SPECIAL_ARROW_TYPE)) {
            case 1 -> {
                if (pThis.level() instanceof ServerLevel pServerLevel && !this.inGround) {
                    pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pThis.getX(), pThis.getY(), pThis.getZ(), 1, 0, 0, 0, 0);
                }
            }
        }
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void spawnSpecialArrowOnHitEntity(EntityHitResult pResult, CallbackInfo pCallbackInfo) {
        AbstractArrow pThis = (AbstractArrow) (Object) this;
        switch (pThis.getData(SPECIAL_ARROW_TYPE)) {
            case 0 -> {
                if (pThis.level() instanceof ServerLevel pServerLevel) {
                    pServerLevel.sendParticles(ParticleTypes.GUST_EMITTER_SMALL, pThis.getX(), pThis.getY(), pThis.getZ(), 1, 0, 0, 0, 0);
                }
            }
            case 1 -> {
                Entity pOwner = pThis.getOwner();
                if (pThis.level() instanceof ServerLevel pServerLevel) {
                    if (pResult.getEntity() instanceof LivingEntity pTarget) {
                        pTarget.addEffect(new MobEffectInstance(WLEffects.PLUNGING, 140, 0));
                    }

                    if (pOwner instanceof ServerPlayer pServerPlayer) {
                        int DragonCharge = pServerPlayer.getData(DRAGON_CHARGE);
                        int DragonChargeCap = WLCommonConfig.CONFIG.DragonChargeCap.get();

                        if (DragonCharge < DragonChargeCap) {
                            if (this.piercingIgnoreEntityIds == null || (this.piercingIgnoreEntityIds != null && this.piercingIgnoreEntityIds.isEmpty())) {
                                DragonCharge = Math.min(DragonCharge + WLCommonConfig.CONFIG.DragonChargeOnRangedHit.get(), DragonChargeCap);
                            } else {
                                DragonCharge = Math.min(DragonCharge + WLCommonConfig.CONFIG.DragonChargeOnRangedHitConsecutive.get(), DragonChargeCap);
                            }
                            pServerPlayer.setData(DRAGON_CHARGE, DragonCharge);
                            PacketDistributor.sendToPlayer(pServerPlayer, new DragonChargeSyncS2CPacket(DragonCharge));
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "onHitBlock", at = @At("HEAD"))
    private void spawnSpecialArrowOnHitBlock(BlockHitResult pResult, CallbackInfo pCallbackInfo) {
        AbstractArrow pThis = (AbstractArrow) (Object) this;
        switch (pThis.getData(SPECIAL_ARROW_TYPE)) {
            case 0 -> {
                if (pThis.level() instanceof ServerLevel pServerLevel) {
                    pServerLevel.sendParticles(ParticleTypes.GUST_EMITTER_SMALL, pThis.getX(), pThis.getY(), pThis.getZ(), 1, 0, 0, 0, 0);
                }
            }
        }
    }
}