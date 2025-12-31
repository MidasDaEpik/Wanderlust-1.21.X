package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.client.model.MaskModel;
import com.midasdaepik.wanderlust.item.Mask;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Map;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Unique
    private HumanoidArmorModel<T> wanderlust$maskModel;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("TAIL"))
    private void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo pCallbackInfo) {
        ItemStack pMaskItemStack = WLUtil.maskItemStack(pLivingEntity.getItemBySlot(EquipmentSlot.HEAD));

        if (!pMaskItemStack.isEmpty() && pMaskItemStack.getItem() instanceof Mask pMask) {
            HumanoidArmorLayer<T, M, A> pThis = (HumanoidArmorLayer<T, M, A>)(Object)this;
            boolean pIsMask = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD).is(WLItems.MASK);

            if (wanderlust$maskModel == null) {
                wanderlust$maskModel = new HumanoidArmorModel<>(
                        new ModelPart(Collections.emptyList(), Map.of(
                                "head", new MaskModel(Minecraft.getInstance().getEntityModels().bakeLayer(MaskModel.LAYER_LOCATION)).mask,
                                "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap())
                        )));
            }
            HumanoidArmorModel<T> pMaskModel = wanderlust$maskModel;

            pThis.getParentModel().copyPropertiesTo(pMaskModel);

            float pModifier = -pMaskItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 0);
            double pYaw = pMaskModel.head.yRot;
            double pPitch = pMaskModel.head.xRot + 1.571;

            pMaskModel.head.offsetPos(new Vector3f((float) (-Math.sin(pYaw) * Math.cos(pPitch) * pModifier), (float) (Math.sin(pPitch) * pModifier), (float) (-Math.cos(pYaw) * Math.cos(pPitch) * pModifier)));

            ResourceLocation pTexture = pMask.getArmorTexture(pMaskItemStack, pLivingEntity, EquipmentSlot.HEAD, pMask.getMaterial().value().layers().get(0), !pIsMask);
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.armorCutoutNoCull(pTexture));

            pMaskModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}