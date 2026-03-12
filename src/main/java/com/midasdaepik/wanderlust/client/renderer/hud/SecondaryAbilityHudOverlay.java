package com.midasdaepik.wanderlust.client.renderer.hud;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLClientConfig;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

public class SecondaryAbilityHudOverlay implements LayeredDraw.Layer {
	static String MOD_ID = Wanderlust.MOD_ID;

	private static final ResourceLocation ACTIONBAR_PHANTOM_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/actionbar_phantom_bar_background");
	private static final ResourceLocation ACTIONBAR_PHANTOM_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/actionbar_phantom_bar_progress");

	private static final ResourceLocation BAR_PHANTOM_EMPTY_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/bar_phantom_bar_empty");
	private static final ResourceLocation BAR_PHANTOM_HALF1_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/bar_phantom_bar_half1");
	private static final ResourceLocation BAR_PHANTOM_HALF2_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/bar_phantom_bar_half2");
	private static final ResourceLocation BAR_PHANTOM_FULL_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/bar_phantom_bar_full");

	private final Minecraft minecraft;

	public SecondaryAbilityHudOverlay(Minecraft pMinecraft) {
		this.minecraft = pMinecraft;
	}

	public enum SecondaryRenderType {
		PHANTOM(PHANTOM_HOVER, 800, BAR_PHANTOM_EMPTY_SPRITE, BAR_PHANTOM_HALF1_SPRITE, BAR_PHANTOM_HALF2_SPRITE, BAR_PHANTOM_FULL_SPRITE);

		public final Supplier<AttachmentType<Integer>> dataType;
		public final int chargeCap;
		public final ResourceLocation barEmpty;
		public final ResourceLocation barHalf1;
		public final ResourceLocation barHalf2;
		public final ResourceLocation barFull;

		SecondaryRenderType(Supplier<AttachmentType<Integer>> pDataType, int pChargeCap, ResourceLocation pBarEmpty, ResourceLocation pBarHalf1, ResourceLocation pBarHalf2, ResourceLocation pBarFull) {
			this.dataType = pDataType;
			this.chargeCap = pChargeCap;
			this.barEmpty = pBarEmpty;
			this.barHalf1 = pBarHalf1;
			this.barHalf2 = pBarHalf2;
			this.barFull = pBarFull;
		}
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) {
		Player pPlayer = this.minecraft.player;
		ClientLevel pClientLevel = this.minecraft.level;
		int pConfig = WLClientConfig.CONFIG.ChargeBarRendering.get();

		if (pPlayer != null && pClientLevel != null && !this.minecraft.options.hideGui) {
			Item pPlayerMainhandItem = pPlayer.getMainHandItem().getItem();
			Item pPlayerOffhandItem = pPlayer.getOffhandItem().getItem();

			SecondaryRenderType pSecondaryRenderType = null;
			if (pPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.PHANTOM_CLOAK.get()) {
				pSecondaryRenderType = SecondaryRenderType.PHANTOM;
				renderBar(pGuiGraphics, pClientLevel, pPlayer.getData(pSecondaryRenderType.dataType), phantomHoverCap(pPlayer), pSecondaryRenderType.barEmpty, pSecondaryRenderType.barHalf1, pSecondaryRenderType.barHalf2, pSecondaryRenderType.barFull);
			}
		}
	}

	public int phantomHoverCap(Player pPlayer) {
		int PhantomHoverMax = 1100;
		if (pPlayer.getItemBySlot(EquipmentSlot.HEAD).is(WLItems.PHANTOM_HOOD)) {
			PhantomHoverMax += 500;
		}
		if (pPlayer.getItemBySlot(EquipmentSlot.LEGS).is(WLItems.PHANTOM_LEGGINGS)) {
			PhantomHoverMax += 900;
		}
		if (pPlayer.getItemBySlot(EquipmentSlot.FEET).is(WLItems.PHANTOM_BOOTS)) {
			PhantomHoverMax += 500;
		}
		return PhantomHoverMax;
	}

	public void renderActionbar(GuiGraphics pGuiGraphics, ClientLevel pClientLevel, int pCharge, int pMaxCharge, ResourceLocation pBackground, ResourceLocation pProgress) {
		int pScreenCenterX = pGuiGraphics.guiWidth() / 2 - 20;
		int pScreenCenterY = pGuiGraphics.guiHeight() - 36 - 32;
		int pChargePercent = Mth.clamp(Mth.floor((float) pCharge / pMaxCharge * 32f), 0, 32);
		this.minecraft.getProfiler().push("secondary_hud_overlay");

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		pGuiGraphics.blitSprite(pBackground, pScreenCenterX, pScreenCenterY, 40, 8);
		pGuiGraphics.blitSprite(pProgress, 40, 8, 0, 0, pScreenCenterX, pScreenCenterY, 4 + pChargePercent, 8);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();

		this.minecraft.getProfiler().pop();
	}

	public void renderBar(GuiGraphics pGuiGraphics, ClientLevel pClientLevel, int pCharge, int pMaxCharge, ResourceLocation pEmpty, ResourceLocation pHalf1, ResourceLocation pHalf2, ResourceLocation pFull) {
		int pScreenCenterX = pGuiGraphics.guiWidth() / 2 + 91;
		int pScreenCenterY = pGuiGraphics.guiHeight() - this.minecraft.gui.rightHeight;

		this.minecraft.getProfiler().push("secondary_hud_overlay");

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		float pChargePercent = Mth.clamp((float) pCharge / pMaxCharge * 10f, 0, 10);

		for (int pIteration = 0; pIteration < 10; pIteration++) {
			float pValue = pChargePercent - pIteration;
			if (pValue >= 1) {
				pGuiGraphics.blitSprite(pFull, pScreenCenterX - pIteration * 8 - 9, pScreenCenterY, 9, 9);
			} else if (pValue >= 0.67) {
				pGuiGraphics.blitSprite(pHalf2, pScreenCenterX - pIteration * 8 - 9, pScreenCenterY, 9, 9);
			} else if (pValue >= 0.33) {
				pGuiGraphics.blitSprite(pHalf1, pScreenCenterX - pIteration * 8 - 9, pScreenCenterY, 9, 9);
			} else {
				pGuiGraphics.blitSprite(pEmpty, pScreenCenterX - pIteration * 8 - 9, pScreenCenterY, 9, 9);
			}
		}

		this.minecraft.gui.rightHeight += 10;

		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();

		this.minecraft.getProfiler().pop();
	}
}
