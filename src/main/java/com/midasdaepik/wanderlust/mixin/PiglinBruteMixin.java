package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinBrute.class)
public class PiglinBruteMixin {
    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void addItem(RandomSource pRandomSource, DifficultyInstance pDifficultyInstance, CallbackInfo pCallbackInfo) {
        PiglinBrute pThis = (PiglinBrute) (Object) this;
        if (pThis.getMainHandItem().getItem() == Items.GOLDEN_AXE && pRandomSource.nextFloat() < 0.3f) {
            pThis.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(WLItems.PIGLIN_WARAXE.get()));
            pThis.setDropChance(EquipmentSlot.MAINHAND, 0.35f);
        }
    }
}