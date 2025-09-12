package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.client.renderer.entity.layers.DragonWingsLayer;
import com.midasdaepik.wanderlust.client.renderer.entity.layers.HaloLayer;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandRenderer.class)
public class ArmorStandRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context pContext, CallbackInfo pCallbackInfo) {
        ArmorStandRenderer pThis = (ArmorStandRenderer) (Object) this;
        pThis.addLayer(new HaloLayer<>(pThis, pContext.getModelSet()));
        pThis.addLayer(new DragonWingsLayer<>(pThis, pContext.getModelSet()));
    }
}