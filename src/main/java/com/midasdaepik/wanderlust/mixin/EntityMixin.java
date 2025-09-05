package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

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
}