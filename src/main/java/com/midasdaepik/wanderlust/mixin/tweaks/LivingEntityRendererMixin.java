package com.midasdaepik.wanderlust.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.INSOMNIA;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private void render(EntityModel pEntityModel, PoseStack pPoseStack, VertexConsumer pVertexConsumer, int pPackedLight, int pPackedOverlay, int pColor, Operation<Void> pOriginal, @Local(argsOnly = true) T pLivingEntity, @Local(argsOnly = true) MultiBufferSource pBuffer) {
        LivingEntityRenderer pThis = (LivingEntityRenderer) (Object) this;
        Player pPlayer = Minecraft.getInstance().player;
        if (WLTweaksConfig.CONFIG.PhantomSpawningReworkToggle.get() && WLTweaksConfig.CONFIG.PhantomDoNotTargetLowInsomnia.get()
                && pThis instanceof PhantomRenderer
                && pPlayer != null) {
            int Insomnia = pPlayer.getData(INSOMNIA) / 24000;
            ResourceLocation pResourcelocation = this.getTextureLocation(pLivingEntity);
            if (Insomnia <= 4) {
                pOriginal.call(pEntityModel, pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(pResourcelocation)), pPackedLight, pPackedOverlay, 0x3FFFFFFF);
            } else if (Insomnia <= 8) {
                pOriginal.call(pEntityModel, pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(pResourcelocation)), pPackedLight, pPackedOverlay, 0x7FFFFFFF);
            } else if (Insomnia <= 12) {
                pOriginal.call(pEntityModel, pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(pResourcelocation)), pPackedLight, pPackedOverlay, 0xBFFFFFFF);
            } else {
                pOriginal.call(pEntityModel, pPoseStack, pVertexConsumer, pPackedLight, pPackedOverlay, pColor);
            }
        } else {
            pOriginal.call(pEntityModel, pPoseStack, pVertexConsumer, pPackedLight, pPackedOverlay, pColor);
        }
    }
}