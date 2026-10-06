package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.client.renderer.entity.layers.DragonWingsLayer;
import com.midasdaepik.wanderlust.client.renderer.entity.layers.HaloLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context pContext, boolean pUseSlimModel, CallbackInfo pCallbackInfo) {
        PlayerRenderer pThis = (PlayerRenderer) (Object) this;
        pThis.addLayer(new HaloLayer<>(pThis, pContext.getModelSet()));
        pThis.addLayer(new DragonWingsLayer<>(pThis, pContext.getModelSet()));
    }
}