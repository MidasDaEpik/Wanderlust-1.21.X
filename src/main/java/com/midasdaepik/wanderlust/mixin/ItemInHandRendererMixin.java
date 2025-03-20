package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.WLTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void hideOffhandItem(LivingEntity pLivingEntity, ItemStack pItemStack, ItemDisplayContext pItemDisplayContext, boolean pLeftHanded, PoseStack pPoseStack, MultiBufferSource pBuffer, int pSeed, CallbackInfo pCallbackInfo) {
        if (pLivingEntity instanceof AbstractClientPlayer) {
            if ((pLivingEntity.isUsingItem() && pLivingEntity.getUseItem().is(WLTags.HIDE_OFFHAND_WHILE_USING_ITEMS))) {
                HumanoidArm pOffhandArm = pLivingEntity.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
                if (pLeftHanded == (pOffhandArm == HumanoidArm.LEFT)) {
                    pCallbackInfo.cancel();
                }
            }
        }
    }
}
