package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PHANTASMAL_STATUS;

@Mixin(Entity.class)
public class EntityMixin {
    @WrapMethod(method = "isOnFire")
    private boolean isOnFire(Operation<Boolean> pOriginal) {
        Entity pThis = (Entity) (Object) this;
        if (pThis.level().isClientSide) {
            if (pThis.getData(PHANTASMAL_STATUS)) {
                return false;
            } else {
                return pOriginal.call();
            }
        } else {
            return pOriginal.call();
        }
    }

    @WrapOperation(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean attack(Entity pEntity, DamageSource pDamageSource, float pAmount, Operation<Boolean> pOriginal) {
        if (pEntity instanceof LivingEntity pLivingEntity && pLivingEntity.hasEffect(WLEffects.IMMOLATION)) {
            return pOriginal.call(pEntity, WLDamageSource.damageSource(pEntity.level(), WLDamageSource.IMMOLATION), pAmount);
        } else {
            return pOriginal.call(pEntity, pDamageSource, pAmount);
        }
    }
}