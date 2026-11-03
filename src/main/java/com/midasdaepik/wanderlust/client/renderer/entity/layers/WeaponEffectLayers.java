package com.midasdaepik.wanderlust.client.renderer.entity.layers;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.client.model.BlazeAuraModel;
import com.midasdaepik.wanderlust.client.model.SoulWispsModel;
import com.midasdaepik.wanderlust.client.model.SpiritAuraModel;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

@OnlyIn(Dist.CLIENT)
public class WeaponEffectLayers<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private static final ResourceLocation TEXTURE_LOCATION_BLAZE_AURA = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/blaze_aura.png");
	private static final ResourceLocation TEXTURE_LOCATION_SPIRIT_AURA = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/spirit_aura.png");
	private static final ResourceLocation TEXTURE_LOCATION_SOUL_WISPS = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/entity/spirit_aura.png");

	public static final ModelLayerLocation LAYER_LOCATION_BLAZE_AURA = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "blaze_aura"), "main");
	public static final ModelLayerLocation LAYER_LOCATION_SPIRIT_AURA = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "spirit_aura"), "main");
	public static final ModelLayerLocation LAYER_LOCATION_SOUL_WISPS = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "soul_wisps"), "main");

	private final BlazeAuraModel<T> blazeAuraModel;
	private final SpiritAuraModel<T> spiritAuraModel;
	private final SoulWispsModel<T> soulWispsModel;

	public WeaponEffectLayers(RenderLayerParent<T, M> pRenderer, EntityModelSet pModelSet) {
		super(pRenderer);
		this.blazeAuraModel = new BlazeAuraModel<>(pModelSet.bakeLayer(LAYER_LOCATION_BLAZE_AURA));
		this.spiritAuraModel = new SpiritAuraModel<>(pModelSet.bakeLayer(LAYER_LOCATION_SPIRIT_AURA));
		this.soulWispsModel = new SoulWispsModel<>(pModelSet.bakeLayer(LAYER_LOCATION_SOUL_WISPS));
	}

	@Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		boolean KatanaAbilityActive = pLivingEntity.getData(KATANA_ABILITY_ACTIVE);
		if ((KatanaAbilityActive || pLivingEntity.getData(KATANA_ABILITY_POST_TIMER) > 0) && pLivingEntity.getData(KATANA_ABILITY_WEAPON).equals(WLItems.FIRESTORM_KATANA.get().getDescriptionId())) {
			pPoseStack.pushPose();

			blazeAuraModel.setupAnim(
					pLivingEntity, pLimbSwing, pLimbSwingAmount,
					pAgeInTicks, pNetHeadYaw, pHeadPitch
			);

			blazeAuraModel.renderToBuffer(
					pPoseStack,
					pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION_BLAZE_AURA)),
					pPackedLight,
					LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F),
					0xFFFFFFFF
			);

			blazeAuraModel.renderToBuffer(
					pPoseStack,
					pBuffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION_BLAZE_AURA)),
					pPackedLight,
					LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F),
					0xFFFFFFFF
			);

			pPoseStack.popPose();
		}
		if (KatanaAbilityActive && pLivingEntity.getData(KATANA_ABILITY_WEAPON).equals(WLItems.KITSUNEBI.get().getDescriptionId())) {
			pPoseStack.pushPose();

			if (!pLivingEntity.isCrouching()) {
				spiritAuraModel.setupAnim(
						pLivingEntity, pLimbSwing, pLimbSwingAmount,
						pAgeInTicks, pNetHeadYaw, pHeadPitch
				);
			}
			
			spiritAuraModel.renderToBuffer(
					pPoseStack,
					pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION_SPIRIT_AURA)),
					pPackedLight,
					LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F),
					0xFFFFFFFF
			);

			spiritAuraModel.renderToBuffer(
					pPoseStack,
					pBuffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION_SPIRIT_AURA)),
					pPackedLight,
					LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F),
					0xFFFFFFFF
			);

			pPoseStack.popPose();
		}
		if (pLivingEntity.getData(SOUL_WISPS_STATUS) >= 0) {
			pPoseStack.pushPose();

			if (!pLivingEntity.isCrouching()) {
				soulWispsModel.setupAnim(
						pLivingEntity, pLimbSwing, pLimbSwingAmount,
						pAgeInTicks, pNetHeadYaw, pHeadPitch
				);
			}

			soulWispsModel.renderToBuffer(
					pPoseStack,
					pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE_LOCATION_SOUL_WISPS)),
					pPackedLight,
					LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F),
					0xFFFFFFFF
			);

			soulWispsModel.renderToBuffer(
					pPoseStack,
					pBuffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE_LOCATION_SOUL_WISPS)),
					pPackedLight,
					LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F),
					0xFFFFFFFF
			);

			pPoseStack.popPose();
		}
	}
}