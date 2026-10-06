package com.midasdaepik.wanderlust.mixin.weapon_modifiers;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    private void getItemBySlot(EquipmentSlot pEquipmentSlot, CallbackInfoReturnable<ItemStack> pReturn) {
        Player pThis = (Player) (Object) this;
        if (pEquipmentSlot == EquipmentSlot.OFFHAND) {
            Inventory pInventory = pThis.getInventory();
            ItemStack pMainhand = pInventory.getSelected();
            ItemStack pOffhand = pInventory.offhand.get(0);
            if (pMainhand.is(WLTags.TWO_HANDED_WEAPONS) || pOffhand.is(WLTags.TWO_HANDED_WEAPONS)) {
                pReturn.setReturnValue(ItemStack.EMPTY);
                pReturn.cancel();
            }
        }
    }

    @WrapMethod(method = "setItemSlot")
    private void setItemSlot(EquipmentSlot pEquipmentSlot, ItemStack pItemStack, Operation<Void> pOriginal) {
        if (pEquipmentSlot == EquipmentSlot.OFFHAND) {
            Player pThis = (Player) (Object) this;
            Inventory pInventory = pThis.getInventory();
            ItemStack pMainhand = pInventory.getSelected();
            ItemStack pOffhand = pInventory.offhand.get(0);
            if (pMainhand.is(WLTags.TWO_HANDED_WEAPONS) || pOffhand.is(WLTags.TWO_HANDED_WEAPONS)) {
                if (!pInventory.add(pItemStack)) {
                    pThis.drop(pItemStack, false);
                }
                return;
            }
        }
        pOriginal.call(pEquipmentSlot, pItemStack);
    }

    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getSweepHitBox(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/AABB;"))
    private AABB attack(ItemStack pItemStack, Player pPlayer, Entity pEntity, Operation<AABB> pOriginal) {
        if (pItemStack.is(WLTags.SWEEPING_WEAPONS)) {
            return pOriginal.call(pItemStack, pPlayer, pEntity).inflate(0.5F, 0.25F, 0.5F);
        }
        return pOriginal.call(pItemStack, pPlayer, pEntity);
    }
}