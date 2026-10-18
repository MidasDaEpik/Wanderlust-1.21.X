package com.midasdaepik.wanderlust.mixin.weapon_modifiers;

import com.llamalad7.mixinextras.sugar.Local;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import net.neoforged.neoforge.common.util.AttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(AttributeUtil.class)
public class AttributeUtilMixin {
    @Inject(method = "applyModifierTooltips", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/AttributeUtil;applyTextFor(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;Lcom/google/common/collect/Multimap;Lnet/neoforged/neoforge/common/util/AttributeTooltipContext;)V", shift = At.Shift.AFTER))
    private static void applyModifierTooltips(ItemStack pItemStack, Consumer<Component> pTooltip, AttributeTooltipContext pTooltipContext, CallbackInfo pCallbackInfo, @Local EquipmentSlotGroup pEquipmentSlotGroup) {
        if (pEquipmentSlotGroup == EquipmentSlotGroup.MAINHAND) {
            wanderlust_1_21_X$applyWeaponModifierTooltip(pItemStack, pTooltip, pTooltipContext, WLTags.WEAPON_MODIFIER_CLEAVE, "cleave");
            wanderlust_1_21_X$applyWeaponModifierTooltip(pItemStack, pTooltip, pTooltipContext, WLTags.WEAPON_MODIFIER_CRITICAL, "critical");
            wanderlust_1_21_X$applyWeaponModifierTooltip(pItemStack, pTooltip, pTooltipContext, WLTags.WEAPON_MODIFIER_FOCUSED, "focused");
            wanderlust_1_21_X$applyWeaponModifierTooltip(pItemStack, pTooltip, pTooltipContext, WLTags.WEAPON_MODIFIER_OBSTINATE, "obstinate");
            wanderlust_1_21_X$applyWeaponModifierTooltip(pItemStack, pTooltip, pTooltipContext, WLTags.WEAPON_MODIFIER_PIERCE, "pierce");
            wanderlust_1_21_X$applyWeaponModifierTooltip(pItemStack, pTooltip, pTooltipContext, WLTags.WEAPON_MODIFIER_TWO_HANDED, "two_handed");
        }
    }

    @Unique
    private static void wanderlust_1_21_X$applyWeaponModifierTooltip(ItemStack pItemStack, Consumer<Component> pTooltip, AttributeTooltipContext pTooltipContext, TagKey<Item> pTag, String pName) {
        if (pItemStack.is(pTag)) {
            pTooltip.accept(Component.literal(" ").append(Component.translatable("weapon_modifier.wanderlust." + pName).withColor(0xAA00FF)));
            if (pTooltipContext.flag().hasShiftDown()) {
                pTooltip.accept(Component.literal(" ┇ ").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("weapon_modifier.wanderlust." + pName + "_description").withStyle(ChatFormatting.DARK_PURPLE)));
            }
        }
    }
}