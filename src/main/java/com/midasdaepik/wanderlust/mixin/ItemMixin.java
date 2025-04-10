package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.item.AncientKnowledgeItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getCraftingRemainingItem", at = @At("HEAD"), cancellable = true)
    private void getCraftingRemainder(CallbackInfoReturnable<Item> pReturn) {
        Item pItem = (Item) (Object) this;
        if (pItem instanceof AncientKnowledgeItem) {
            pReturn.setReturnValue(pItem);
        }
    }
}