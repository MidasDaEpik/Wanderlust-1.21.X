package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEnchantmentEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @WrapMethod(method = "renderNameTag")
    private void renderNameTag(T pEntity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, float pPartialTick, Operation<Void> pOriginal) {
        if (pEntity instanceof LivingEntity pLivingEntity) {
            ItemStack pItemStack = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD);
            if (WLUtil.hasMaskEnchantment(pItemStack, WLEnchantmentEffects.CONCEALMENT.get())) {
                return;
            }
            if (WLUtil.hasMaskEnchantment(pItemStack, WLEnchantmentEffects.NAMELESS.get())) {
                pOriginal.call(pEntity, WLUtil.itemNamelessName(pLivingEntity.getItemBySlot(EquipmentSlot.HEAD)), pPoseStack, pBufferSource, pPackedLight, pPartialTick);
            } else {
                pOriginal.call(pEntity, pDisplayName, pPoseStack, pBufferSource, pPackedLight, pPartialTick);
            }
        } else {
            pOriginal.call(pEntity, pDisplayName, pPoseStack, pBufferSource, pPackedLight, pPartialTick);
        }
    }
}