package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemCooldowns.class)
public class ItemCooldownsMixin {
    @WrapMethod(method = "addCooldown")
    private void addCooldown(Item pItem, int pTicks, Operation<Void> pOriginal) {
        if (pItem == WLItems.PHANTOM_TUNIC.get() || pItem == WLItems.PHANTOM_LEGGINGS.get() || pItem == WLItems.PHANTOM_BOOTS.get()) {
            pOriginal.call(WLItems.PHANTOM_HOOD.get(), pTicks);
        }
        pOriginal.call(pItem, pTicks);
    }

    @WrapMethod(method = "removeCooldown")
    private void removeCooldown(Item pItem, Operation<Void> pOriginal) {
        if (pItem == WLItems.PHANTOM_TUNIC.get() || pItem == WLItems.PHANTOM_LEGGINGS.get() || pItem == WLItems.PHANTOM_BOOTS.get()) {
            pOriginal.call(WLItems.PHANTOM_HOOD.get());
        }
        pOriginal.call(pItem);
    }

    @WrapMethod(method = "getCooldownPercent")
    private float getCooldownPercent(Item pItem, float pPartialTicks, Operation<Float> pOriginal) {
        float pOriginalValue = pOriginal.call(pItem, pPartialTicks);
        if (pItem == WLItems.PHANTOM_TUNIC.get() || pItem == WLItems.PHANTOM_LEGGINGS.get() || pItem == WLItems.PHANTOM_BOOTS.get()) {
            return Math.max(pOriginal.call(WLItems.PHANTOM_HOOD.get(), pPartialTicks), pOriginalValue);
        } else {
            return pOriginalValue;
        }
    }
}