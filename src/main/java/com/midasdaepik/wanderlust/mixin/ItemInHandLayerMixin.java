package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {
    @WrapOperation(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void init(ItemInHandRenderer pInstance, LivingEntity pLivingEntity, ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pSeed, Operation<Void> pOriginal) {
        if (pItemStack.is(WLItems.CHARYBDIS) && pLivingEntity.isUsingItem() && pLivingEntity.getUseItem() == pItemStack) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(-3F));
            pPoseStack.translate(0.1, 0.1f, 0);

            pOriginal.call(pInstance, pLivingEntity, pItemStack, pDisplayContext, pLeftHand, pPoseStack, pBuffer, pSeed);

        } else if ((pItemStack.is(WLItems.WARPED_RAPIER) || pItemStack.is(WLItems.WARPTHISTLE)) && pLivingEntity.isUsingItem() && pLivingEntity.getUseItem() == pItemStack) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            pPoseStack.translate(0, 0f, -0.15);

            pOriginal.call(pInstance, pLivingEntity, pItemStack, pDisplayContext, pLeftHand, pPoseStack, pBuffer, pSeed);
        } else {
            pOriginal.call(pInstance, pLivingEntity, pItemStack, pDisplayContext, pLeftHand, pPoseStack, pBuffer, pSeed);
        }
    }
}