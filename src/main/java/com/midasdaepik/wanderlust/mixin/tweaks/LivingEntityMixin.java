package com.midasdaepik.wanderlust.mixin.tweaks;

import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onEquipItem", at = @At("HEAD"))
    private void onEquipItem(EquipmentSlot pSlot, ItemStack pOldItem, ItemStack pNewItem, CallbackInfo pCallbackInfo) {
        if (WLTweaksConfig.CONFIG.EquippingCooldownActive.get()) {
            LivingEntity pThis = (LivingEntity) (Object) this;
            if (!pThis.level().isClientSide && pThis instanceof Player pPlayer && pSlot.isArmor() && (pNewItem.is(WLTags.COOLDOWN_ON_EQUIP_ITEM) || (WLTweaksConfig.CONFIG.EquippingCooldownAllArmor.get() && (pNewItem.getItem() instanceof Equipable || Block.byItem(pNewItem.getItem()) instanceof Equipable)))) {
                ItemCooldowns pCooldowns = pPlayer.getCooldowns();
                if (!pCooldowns.isOnCooldown(pNewItem.getItem())) {
                    pPlayer.getCooldowns().addCooldown(pNewItem.getItem(), WLTweaksConfig.CONFIG.EquippingCooldownDuration.get());
                }
            }
        }
    }
}