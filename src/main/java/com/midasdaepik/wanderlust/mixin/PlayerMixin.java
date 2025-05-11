package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    private void getItemBySlot(EquipmentSlot pEquipmentSlot, CallbackInfoReturnable<ItemStack> pReturn) {
        Player pThis = (Player) (Object) this;
        ItemStack pMainhand = pThis.getInventory().getSelected();
        ItemStack pOffhand = pThis.getInventory().offhand.get(0);
        if (pEquipmentSlot == EquipmentSlot.OFFHAND && (pMainhand.is(WLTags.TWO_HANDED_WEAPONS) || pOffhand.is(WLTags.TWO_HANDED_WEAPONS))) {
            pReturn.setReturnValue(ItemStack.EMPTY);
            pReturn.cancel();
        }
    }

    @WrapMethod(method = "blockInteractionRange")
    private double blockInteractionRange(Operation<Double> pOriginal) {
        Player pThis = (Player) (Object) this;
        if (pThis.hasEffect(WLEffects.PHANTASMAL)) {
            return 0d;
        } else {
            return pOriginal.call();
        }
    }

    @WrapMethod(method = "entityInteractionRange")
    private double entityInteractionRange(Operation<Double> pOriginal) {
        Player pThis = (Player) (Object) this;
        if (pThis.hasEffect(WLEffects.PHANTASMAL)) {
            return 0d;
        } else {
            return pOriginal.call();
        }
    }
}