package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Equipable.class)
public interface EquipableMixin {
    @WrapMethod(method = "swapWithEquipmentSlot")
    private InteractionResultHolder<ItemStack> swapWithEquipmentSlot(Item pItem, Level pLevel, Player pPlayer, InteractionHand pHand, Operation<InteractionResultHolder<ItemStack>> pOriginal) {
        if (WLCommonConfig.CONFIG.EquippingCooldownPreventChange.get()) {
            ItemStack pItemstack = pPlayer.getItemInHand(pHand);
            EquipmentSlot pEquipmentslot = pPlayer.getEquipmentSlotForItem(pItemstack);
            ItemStack pArmor = pPlayer.getItemBySlot(pEquipmentslot);

            if (pPlayer.getCooldowns().isOnCooldown(pArmor.getItem())) {
                return InteractionResultHolder.fail(pItemstack);
            } else {
                return pOriginal.call(pItem, pLevel, pPlayer, pHand);
            }
        } else {
            return pOriginal.call(pItem, pLevel, pPlayer, pHand);
        }
    }
}