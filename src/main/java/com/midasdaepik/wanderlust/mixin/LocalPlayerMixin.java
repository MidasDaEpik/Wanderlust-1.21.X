package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PHANTOM_HOVER;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @WrapMethod(method = "isMovingSlowly")
    private boolean isMovingSlowly(Operation<Boolean> pOriginal) {
        LocalPlayer pLocalPlayer = (LocalPlayer) (Object) this;
        if (pLocalPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.PHANTOM_CLOAK.get() && pLocalPlayer.getData(PHANTOM_HOVER) > 0 && pLocalPlayer.isCrouching() && pLocalPlayer.fallDistance >= 0.1 && !pLocalPlayer.getCooldowns().isOnCooldown(WLItems.PHANTOM_CLOAK.get())) {
            return false;
        } else {
            return pOriginal.call();
        }
    }
}