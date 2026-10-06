package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.item.KatanaItem;
import com.midasdaepik.wanderlust.item.Keris;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {
    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getAttackDamageBonus(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;)F"))
    private float attack(Item pItem, Entity pTarget, float pDamage, DamageSource pDamageSource, Operation<Float> pOriginal) {
        Player pThis = (Player) (Object) this;
        if (pThis.getOffhandItem().is(WLTags.OFF_HAND_WEAPONS)) {
            ItemStack pOffhandItem = pThis.getOffhandItem();
            float pBonus = 0;
            if (pOffhandItem.getItem() == WLItems.KERIS.get()) {
                pBonus += Keris.calculateAttackDamageBonus(pTarget, pDamage, pDamageSource, 0f) * 0.5f;
            }
            return pOriginal.call(pItem, pTarget, pDamage, pDamageSource) + pBonus;
        } else {
            return pOriginal.call(pItem, pTarget, pDamage, pDamageSource);
        }
    }

    @WrapMethod(method = "tryToStartFallFlying")
    private boolean tryToStartFallFlying(Operation<Boolean> pOriginal) {
        Player pThis = (Player) (Object) this;
        if (pThis.hasEffect(WLEffects.DRAGONS_ASCENSION) || pThis.hasEffect(WLEffects.PHANTASMAL)) {
            return false;
        } else {
            return pOriginal.call();
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
        } else if (pThis.getMainHandItem().getItem() instanceof KatanaItem pKatanaItem) {
            return pKatanaItem.reachModifier(pThis, pOriginal.call());
        } else {
            return pOriginal.call();
        }
    }
}