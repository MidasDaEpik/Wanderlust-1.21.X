package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.client.renderer.entity.layers.DragonWingsLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public class HumanoidMobRendererMixin {
    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/HumanoidModel;FFFF)V", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context pContext, HumanoidModel pModel, float pShadowRadius, float pScaleX, float pScaleY, float pScaleZ, CallbackInfo pCallbackInfo) {
        HumanoidMobRenderer pThis = (HumanoidMobRenderer) (Object) this;
        pThis.addLayer(new DragonWingsLayer<>(pThis, pContext.getModelSet()));
    }
}