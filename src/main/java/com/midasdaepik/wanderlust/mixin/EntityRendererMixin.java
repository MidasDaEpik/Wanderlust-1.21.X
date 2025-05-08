package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.WLItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
    private void renderNameTag(T pEntity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, float pPartialTick, CallbackInfo pCallbackInfo) {
        if (pEntity instanceof LivingEntity pLivingEntity && pLivingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == WLItems.PHANTOM_HOOD.get()) {
            pCallbackInfo.cancel();
        }
    }
}