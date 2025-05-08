package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DamageSource.class)
public class DamageSourceMixin {
    @WrapOperation(method = "getLocalizedDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    private Component getLocalizedDeathMessage(Entity pEntity, Operation<Component> pOriginal) {
        if (pEntity instanceof LivingEntity pLivingEntity && pLivingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == WLItems.PHANTOM_HOOD.get()) {
            return Component.literal("Somebody").withStyle(ChatFormatting.OBFUSCATED);
        } else {
            return pOriginal.call(pEntity);
        }
    }

    @WrapOperation(method = "getLocalizedDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    private Component getLocalizedDeathMessage(LivingEntity pLivingEntity, Operation<Component> pOriginal) {
        if (pLivingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == WLItems.PHANTOM_HOOD.get()) {
            return Component.literal("Somebody").withStyle(ChatFormatting.OBFUSCATED);
        } else {
            return pOriginal.call(pLivingEntity);
        }
    }
}