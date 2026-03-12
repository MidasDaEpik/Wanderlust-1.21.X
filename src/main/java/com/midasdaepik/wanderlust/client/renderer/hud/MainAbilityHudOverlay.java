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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

public class MainAbilityHudOverlay implements LayeredDraw.Layer {
	static String MOD_ID = Wanderlust.MOD_ID;

	private static final ResourceLocation CROSSHAIR_ABILITY_BAR_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/crosshair_ability_bar_background");

	private static final ResourceLocation CROSSHAIR_CHARYBDIS_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/crosshair_charybdis_bar_progress");
	private static final ResourceLocation CROSSHAIR_DRAGON_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/crosshair_dragon_bar_progress");
	private static final ResourceLocation CROSSHAIR_PYROSWEEP_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/crosshair_pyrosweep_bar_progress");

	private static final ResourceLocation HOTBAR_CHARYBDIS_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_charybdis_bar_background");
	private static final ResourceLocation HOTBAR_CHARYBDIS_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_charybdis_bar_progress");
	private static final ResourceLocation HOTBAR_CHARYBDIS_FULL_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_charybdis_bar_full");

	private static final ResourceLocation HOTBAR_DRAGON_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragon_bar_background");
	private static final ResourceLocation HOTBAR_DRAGON_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragon_bar_progress");
	private static final ResourceLocation HOTBAR_DRAGON_FULL_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragon_bar_full");

	private static final ResourceLocation HOTBAR_PYROSWEEP_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_pyrosweep_bar_background");
	private static final ResourceLocation HOTBAR_PYROSWEEP_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_pyrosweep_bar_progress");
	private static final ResourceLocation HOTBAR_PYROSWEEP_FULL_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_pyrosweep_bar_full");

	private final Minecraft minecraft;

	public MainAbilityHudOverlay(Minecraft pMinecraft) {
		this.minecraft = pMinecraft;
	}

	public enum MainRenderType {
		CHARYBDIS(CHARYBDIS_CHARGE, WLCommonConfig.CONFIG.CharybdisChargeCap.get(), HOTBAR_CHARYBDIS_BACKGROUND_SPRITE, HOTBAR_CHARYBDIS_PROGRESS_SPRITE, HOTBAR_CHARYBDIS_FULL_SPRITE, CROSSHAIR_CHARYBDIS_PROGRESS_SPRITE),
		DRAGON(DRAGON_CHARGE, WLCommonConfig.CONFIG.DragonChargeCap.get(), HOTBAR_DRAGON_BACKGROUND_SPRITE, HOTBAR_DRAGON_PROGRESS_SPRITE, HOTBAR_DRAGON_FULL_SPRITE, CROSSHAIR_DRAGON_PROGRESS_SPRITE),
		PYROSWEEP(PYROSWEEP_CHARGE, WLCommonConfig.CONFIG.PyrosweepChargeCap.get(), HOTBAR_PYROSWEEP_BACKGROUND_SPRITE, HOTBAR_PYROSWEEP_PROGRESS_SPRITE, HOTBAR_PYROSWEEP_FULL_SPRITE, CROSSHAIR_PYROSWEEP_PROGRESS_SPRITE);

		public final Supplier<AttachmentType<Integer>> dataType;
		public final int chargeCap;
		public final ResourceLocation hotbarBackground;
		public final ResourceLocation hotbarProgress;
		public final ResourceLocation hotbarFull;
		public final ResourceLocation crosshairProgress;

		MainRenderType(Supplier<AttachmentType<Integer>> pDataType, int pChargeCap, ResourceLocation pHotbarBackground, ResourceLocation pHotbarProgress, ResourceLocation pHotbarFull, ResourceLocation pCrosshairProgress) {
			this.dataType = pDataType;
			this.chargeCap = pChargeCap;
			this.hotbarBackground = pHotbarBackground;
			this.hotbarProgress = pHotbarProgress;
			this.hotbarFull = pHotbarFull;
			this.crosshairProgress = pCrosshairProgress;
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

			MainRenderType pMainRenderType = null;
			if (pPlayerMainhandItem == WLItems.CHARYBDIS.get()) {
				pMainRenderType = MainRenderType.CHARYBDIS;

			} else if (pPlayerMainhandItem == WLItems.DRAGONS_RAGE.get() || pPlayerMainhandItem == WLItems.DRAGONS_BREATH_ARBALEST.get()) {
				pMainRenderType = MainRenderType.DRAGON;

			} else if (pPlayerMainhandItem == WLItems.PYROSWEEP.get()) {
				pMainRenderType = MainRenderType.PYROSWEEP;

			} else if (pPlayerOffhandItem == WLItems.DRAGONS_BREATH_ARBALEST.get()) {
				pMainRenderType = MainRenderType.DRAGON;
			}

			if (pMainRenderType != null) {
				switch (pConfig) {
					case 1 -> {
						renderCrosshair(pGuiGraphics, pClientLevel, pPlayer.getData(pMainRenderType.dataType), pMainRenderType.chargeCap, pMainRenderType.crosshairProgress, false);
					}
					case 2 -> {
						renderCrosshair(pGuiGraphics, pClientLevel, pPlayer.getData(pMainRenderType.dataType), pMainRenderType.chargeCap, pMainRenderType.crosshairProgress, true);
					}
					default -> {
						renderHotbar(pGuiGraphics, pClientLevel, pPlayer.getData(pMainRenderType.dataType), pMainRenderType.chargeCap, pMainRenderType.hotbarBackground, pMainRenderType.hotbarProgress, pMainRenderType.hotbarFull);
					}
				}
			}
		}
	}

	public void renderHotbar(GuiGraphics pGuiGraphics, ClientLevel pClientLevel, int pCharge, int pMaxCharge, ResourceLocation pBackground, ResourceLocation pProgress, ResourceLocation pFull) {
		int pScreenCenterX = pGuiGraphics.guiWidth() / 2 - 10;
		int pScreenCenterY = pGuiGraphics.guiHeight() - 36 - 20;
		int pChargePercent = Mth.clamp(Mth.floor((float) pCharge / pMaxCharge * 16f), 0, 16);
		this.minecraft.getProfiler().push("main_hud_overlay");

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		pGuiGraphics.blitSprite(pProgress, pScreenCenterX, pScreenCenterY, 20, 20);
		pGuiGraphics.blitSprite(pBackground, 20, 20, 0, 0, pScreenCenterX, pScreenCenterY, 20, 18 - pChargePercent);
		if (pChargePercent == 16) {
			double pTimer = pClientLevel.getGameTime();
			pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, (float) Math.min(Math.cos(pTimer / 10 * Math.PI) / 2 + 0.75F, 1));
			pGuiGraphics.blitSprite(pFull, pScreenCenterX, pScreenCenterY, 20, 20);
			pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();

		this.minecraft.getProfiler().pop();
	}

	public void renderCrosshair(GuiGraphics pGuiGraphics, ClientLevel pClientLevel, int pCharge, int pMaxCharge, ResourceLocation pProgress, boolean pHighSaturation) {
		int pScreenCenterX = pGuiGraphics.guiWidth() / 2 - 8;
		int pScreenCenterY = pGuiGraphics.guiHeight() / 2 - 14;
		int pChargePercent = Mth.clamp(Mth.floor((float) pCharge / pMaxCharge * 16f), 0, 16);

		this.minecraft.getProfiler().push("main_hud_overlay");

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		pGuiGraphics.blitSprite(CROSSHAIR_ABILITY_BAR_BACKGROUND_SPRITE, pScreenCenterX, pScreenCenterY, 16, 4);
		if (pHighSaturation) {
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		}
		pGuiGraphics.blitSprite(pProgress, 16, 4, 0, 0, pScreenCenterX, pScreenCenterY, pChargePercent, 4);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();

		this.minecraft.getProfiler().pop();
	}
}
