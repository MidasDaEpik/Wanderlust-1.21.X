package com.midasdaepik.wanderlust.mixin.weapon_modifiers;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    private void getItemBySlot(EquipmentSlot pEquipmentSlot, CallbackInfoReturnable<ItemStack> pReturn) {
        Player pThis = (Player) (Object) this;
        if (pEquipmentSlot == EquipmentSlot.OFFHAND) {
            Inventory pInventory = pThis.getInventory();
            ItemStack pMainhand = pInventory.getSelected();
            ItemStack pOffhand = pInventory.offhand.get(0);
            if (pMainhand.is(WLTags.WEAPON_MODIFIER_TWO_HANDED) || pOffhand.is(WLTags.WEAPON_MODIFIER_TWO_HANDED)) {
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
            if (pMainhand.is(WLTags.WEAPON_MODIFIER_TWO_HANDED) || pOffhand.is(WLTags.WEAPON_MODIFIER_TWO_HANDED)) {
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
        if (pItemStack.is(WLTags.WEAPON_MODIFIER_CLEAVE)) {
            return pOriginal.call(pItemStack, pPlayer, pEntity).inflate(0.5F, 0.25F, 0.5F);
        }
        return pOriginal.call(pItemStack, pPlayer, pEntity);
    }

    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private List attack(Level pLevel, Class pClass, AABB pAABB, Operation<List> pOriginal, @Local(ordinal = 0) ItemStack pItemStack, @Local(argsOnly = true) Entity pTarget) {
        Player pThis = (Player) (Object) this;
        if (pItemStack.is(WLTags.WEAPON_MODIFIER_PIERCE)) {
            Set<LivingEntity> pFoundTargets = new HashSet<>(Set.of());
            AABB pTargetAABB = pTarget.getBoundingBox().inflate(0.2F, 0.2F, 0.2F);
            pFoundTargets.addAll(new HashSet<>(pLevel.getEntitiesOfClass(pClass, pTargetAABB)));

            Vec3 pTargetCenter = pAABB.getCenter();
            Vec3 pPlayerCenter = pThis.getBoundingBox().getCenter();

            int pPierce = (int) Math.ceil((pThis.entityInteractionRange() - pTargetCenter.distanceTo(pPlayerCenter)) * 2) + 4;

            Vec3 pModifier = pThis.getLookAngle();

            for (int i = 1; i <= pPierce; i++) {
                Vec3 pCenter = new Vec3(pTargetCenter.x + pModifier.x * i / 2, pTargetCenter.y + pModifier.y * i / 2, pTargetCenter.z + pModifier.z * i / 2);
                AABB pNewHitbox = new AABB(pCenter.x - pTargetAABB.getXsize() / 2, pCenter.y - pTargetAABB.getYsize() / 2, pCenter.z - pTargetAABB.getZsize() / 2,
                        pCenter.x + pTargetAABB.getXsize() / 2, pCenter.y + pTargetAABB.getYsize() / 2, pCenter.z + pTargetAABB.getZsize() / 2);

                //Wanderlust.LOGGER.info("Pierce: " + i + " Pos: " + pNewHitbox.getCenter().x + ", " + pNewHitbox.getCenter().y + ", " + pNewHitbox.getCenter().z + " Size: " + pNewHitbox.getXsize() + ", " + pNewHitbox.getYsize() + ", " + pNewHitbox.getZsize());

                pFoundTargets.addAll(new HashSet<>(pLevel.getEntitiesOfClass(pClass, pNewHitbox)));
            }

            return Arrays.asList(pFoundTargets.toArray());
        }
        return pOriginal.call(pLevel, pClass, pAABB);
    }

    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;entityInteractionRange()D"))
    private double attack(Player pThis, Operation<Double> pOriginal, @Local(ordinal = 0) ItemStack pItemStack, @Local(argsOnly = true) Entity pTarget) {
        if (pItemStack.is(WLTags.WEAPON_MODIFIER_PIERCE)) {
            return pOriginal.call(pThis) + 2;
        }
        return pOriginal.call(pThis);
    }
}