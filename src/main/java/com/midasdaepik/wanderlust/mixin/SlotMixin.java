package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Slot.class)
public class SlotMixin {
    @WrapMethod(method = "mayPickup")
    private boolean mayPickup(Player pPlayer, Operation<Boolean> pOriginal) {
        if (WLCommonConfig.CONFIG.EquippingCooldownPreventChange.get()) {
            Slot pThis = (Slot) (Object) this;
            ItemStack pItemstack = pThis.getItem();

            if (pThis.container instanceof Inventory pInventory && pItemstack != null && (pThis.index == 5 || pThis.index == 6 || pThis.index == 7|| pThis.index == 8) && pPlayer.getCooldowns().isOnCooldown(pItemstack.getItem())) {
                return false;
            } else {
                return pOriginal.call(pPlayer);
            }
        } else {
            return pOriginal.call(pPlayer);
        }
    }
}