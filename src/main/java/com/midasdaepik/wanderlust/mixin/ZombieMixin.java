package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieMixin {
    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void addItem(RandomSource pRandomSource, DifficultyInstance pDifficultyInstance, CallbackInfo pCallbackInfo) {
        Zombie pThis = (Zombie) (Object) this;
        if (pThis.getMainHandItem().isEmpty() && pRandomSource.nextFloat() < 0.025f) {
            pThis.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(WLItems.DAGGER.get()));
            pThis.setDropChance(EquipmentSlot.MAINHAND, 0.5f);

            pThis.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(WLItems.DAGGER.get()));
            pThis.setDropChance(EquipmentSlot.OFFHAND, 0.5f);

            if (pThis.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && pRandomSource.nextFloat() < 0.8f) {
                pThis.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));

                if (pThis.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && pRandomSource.nextFloat() < 0.8f) {
                    pThis.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));

                    if (pRandomSource.nextFloat() < 0.8f) {
                        if (pThis.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
                            pThis.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                        }

                        if (pThis.getItemBySlot(EquipmentSlot.FEET).isEmpty()) {
                            pThis.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
                        }
                    }
                }
            }
        }
    }
}