package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Drowned.class)
public class DrownedMixin {
    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void addItem(RandomSource pRandomSource, DifficultyInstance pDifficultyInstance, CallbackInfo pCallbackInfo) {
        Drowned pThis = (Drowned) (Object) this;
        if (pThis.getMainHandItem().isEmpty() && pRandomSource.nextFloat() < 0.1f) {
            pThis.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(WLItems.CUTLASS.get()));
            pThis.setDropChance(EquipmentSlot.MAINHAND, 0.66f);
        }
    }
}