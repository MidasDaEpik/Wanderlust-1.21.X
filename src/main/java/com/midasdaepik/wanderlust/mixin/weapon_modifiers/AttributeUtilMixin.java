package com.midasdaepik.wanderlust.mixin.weapon_modifiers;

import com.llamalad7.mixinextras.sugar.Local;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import net.neoforged.neoforge.common.util.AttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(AttributeUtil.class)
public class AttributeUtilMixin {
    @Inject(method = "applyModifierTooltips", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/AttributeUtil;applyTextFor(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;Lcom/google/common/collect/Multimap;Lnet/neoforged/neoforge/common/util/AttributeTooltipContext;)V", shift = At.Shift.AFTER))
    private static void addTwoHandedTooltip(ItemStack pItemStack, Consumer<Component> pTooltip, AttributeTooltipContext pTooltipContext, CallbackInfo pCallbackInfo, @Local EquipmentSlotGroup pEquipmentSlotGroup) {
        if (pEquipmentSlotGroup == EquipmentSlotGroup.MAINHAND) {
            if (pItemStack.is(WLTags.CRITLESS_WEAPONS)) {
                pTooltip.accept(Component.literal(" ").append(Component.translatable("item.wanderlust.critless")).withStyle(ChatFormatting.DARK_PURPLE));
            }
            if (pItemStack.is(WLTags.SWEEPING_WEAPONS)) {
                pTooltip.accept(Component.literal(" ").append(Component.translatable("item.wanderlust.sweeping")).withStyle(ChatFormatting.DARK_PURPLE));
            }
            if (pItemStack.is(WLTags.TWO_HANDED_WEAPONS)) {
                pTooltip.accept(Component.literal(" ").append(Component.translatable("item.wanderlust.two_handed")).withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
    }
}