package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.RRDataComponents;
import com.midasdaepik.wanderlust.registries.RRItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    private void init(Level pLevel, double posX, double posY, double posZ, ItemStack pItemStack, CallbackInfo pCallbackInfo) {
        ItemEntity pThis = (ItemEntity) (Object) this;
        if (pItemStack.getOrDefault(RRDataComponents.NO_GRAVITY, false) || pItemStack.getItem() == RRItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get()) {
            pThis.setDeltaMovement(pThis.getDeltaMovement().multiply(0.98, 0.98, 0.98));
            if (!pThis.isNoGravity()) {
                pThis.setNoGravity(true);
            }
        }
    }
}