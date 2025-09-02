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

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

public class WeaponAbilityHudOverlay implements LayeredDraw.Layer {
	static String MOD_ID = Wanderlust.MOD_ID;

	private static final ResourceLocation CROSSHAIR_ABILITY_BAR_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/crosshair_ability_bar_background");

	private static final ResourceLocation CROSSHAIR_CHARYBDIS_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/crosshair_charybdis_bar_progress");
	private static final ResourceLocation CROSSHAIR_DRAGONS_RAGE_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/crosshair_dragons_rage_bar_progress");
	private static final ResourceLocation CROSSHAIR_PYROSWEEP_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/crosshair_pyrosweep_bar_progress");

	private static final ResourceLocation HOTBAR_CHARYBDIS_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_charybdis_bar_background");
	private static final ResourceLocation HOTBAR_CHARYBDIS_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_charybdis_bar_progress");

	private static final ResourceLocation HOTBAR_DRAGONS_RAGE_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragons_rage_bar_background");
	private static final ResourceLocation HOTBAR_DRAGONS_RAGE_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragons_rage_bar_progress");
	private static final ResourceLocation HOTBAR_DRAGONS_RAGE_FULL_0_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragons_rage_bar_full_0");
	private static final ResourceLocation HOTBAR_DRAGONS_RAGE_FULL_1_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragons_rage_bar_full_1");
	private static final ResourceLocation HOTBAR_DRAGONS_RAGE_FULL_2_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragons_rage_bar_full_2");
	private static final ResourceLocation HOTBAR_DRAGONS_RAGE_FULL_3_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragons_rage_bar_full_3");
	private static final ResourceLocation HOTBAR_DRAGONS_RAGE_FULL_4_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_dragons_rage_bar_full_4");

	private static final ResourceLocation HOTBAR_PYROSWEEP_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_pyrosweep_bar_background");
	private static final ResourceLocation HOTBAR_PYROSWEEP_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/hotbar_pyrosweep_bar_progress");

	private final Minecraft minecraft;

	public WeaponAbilityHudOverlay(Minecraft pMinecraft) {
		this.minecraft = pMinecraft;
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) {
		Player pPlayer = this.minecraft.player;
		ClientLevel pClientLevel = this.minecraft.level;
		int pConfig = WLClientConfig.CONFIG.ChargeBarRendering.get();

		if (pPlayer != null && pClientLevel != null && !this.minecraft.options.hideGui) {
			Item pPlayerMainhandItem = pPlayer.getMainHandItem().getItem();
			Item pPlayerOffhandItem = pPlayer.getOffhandItem().getItem();

			if (pPlayerMainhandItem == WLItems.CHARYBDIS.get()) {
				renderCharybdis(pGuiGraphics, pPlayer, pClientLevel, pConfig);

			} else if (pPlayerMainhandItem == WLItems.DRAGONS_RAGE.get() || pPlayerMainhandItem == WLItems.DRAGONS_BREATH_ARBALEST.get()) {
				renderDragonsRage(pGuiGraphics, pPlayer, pClientLevel, pConfig);

			} else if (pPlayerMainhandItem == WLItems.PYROSWEEP.get()) {
				renderPyrosweep(pGuiGraphics, pPlayer, pClientLevel, pConfig);

			} else if (pPlayerOffhandItem == WLItems.DRAGONS_BREATH_ARBALEST.get()) {
				renderDragonsRage(pGuiGraphics, pPlayer, pClientLevel, pConfig);
			}
		}
	}

	public void renderCharybdis(GuiGraphics pGuiGraphics, Player pPlayer, ClientLevel pClientLevel, int pConfig) {
		int CharybdisCharge = pPlayer.getData(CHARYBDIS_CHARGE);

		if (pConfig == 0) {
			int pScreenCenterX = pGuiGraphics.guiWidth() / 2 - 9;
			int pScreenCenterY = pGuiGraphics.guiHeight() - 36 - 16 - 4;
			int height = 15 - Mth.clamp(Mth.floor((float) CharybdisCharge / WLCommonConfig.CONFIG.CharybdisChargeCap.get() * 14f), 0, 14);

			this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

			RenderSystem.enableBlend();
			pGuiGraphics.blitSprite(HOTBAR_CHARYBDIS_PROGRESS_SPRITE,  pScreenCenterX, pScreenCenterY, 18, 18);
			pGuiGraphics.blitSprite(HOTBAR_CHARYBDIS_BACKGROUND_SPRITE, 18, 18, 0, 0,  pScreenCenterX, pScreenCenterY, 18, height);
			RenderSystem.disableBlend();

			this.minecraft.getProfiler().pop();

		} else {
			int pScreenCenterX = pGuiGraphics.guiWidth() / 2 - 8;
			int pScreenCenterY = pGuiGraphics.guiHeight() / 2 - 14;
			int width = Mth.clamp(Mth.floor((float) CharybdisCharge / WLCommonConfig.CONFIG.CharybdisChargeCap.get() * 16f), 0, 16);

			this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			pGuiGraphics.blitSprite(CROSSHAIR_ABILITY_BAR_BACKGROUND_SPRITE,  pScreenCenterX, pScreenCenterY, 16, 4);
			if (pConfig == 2) {
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			}
			pGuiGraphics.blitSprite(CROSSHAIR_CHARYBDIS_PROGRESS_SPRITE, 16, 4, 0, 0,  pScreenCenterX, pScreenCenterY, width, 4);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();

			this.minecraft.getProfiler().pop();
		}
	}

	public void renderDragonsRage(GuiGraphics pGuiGraphics, Player pPlayer, ClientLevel pClientLevel, int pConfig) {
		int DragonCharge = pPlayer.getData(DRAGON_CHARGE);

		if (pConfig == 0) {
			int pScreenCenterX = pGuiGraphics.guiWidth() / 2;
			int pScreenCenterY = pGuiGraphics.guiHeight() - 38 - 32;
			int height = 30 - Mth.clamp(Mth.floor((float) DragonCharge / WLCommonConfig.CONFIG.DragonChargeCap.get() * 18f), 0, 18);
			if (DragonCharge > 0) {
				height -= 1;
			}
			this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

			RenderSystem.enableBlend();
			if (DragonCharge == WLCommonConfig.CONFIG.DragonChargeCap.get()) {
				double Timer = pClientLevel.getGameTime();
				if (Timer % 29 == 0 || Timer % 29 == 1) {
					pGuiGraphics.blitSprite(HOTBAR_DRAGONS_RAGE_FULL_0_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
				} else if (Timer % 29 == 2 || Timer % 29 == 3) {
					pGuiGraphics.blitSprite(HOTBAR_DRAGONS_RAGE_FULL_1_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
				} else if (Timer % 29 == 4 || Timer % 29 == 5) {
					pGuiGraphics.blitSprite(HOTBAR_DRAGONS_RAGE_FULL_2_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
				} else if (Timer % 29 == 6 || Timer % 29 == 7) {
					pGuiGraphics.blitSprite(HOTBAR_DRAGONS_RAGE_FULL_3_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
				} else if (Timer % 29 == 8 || Timer % 29 == 9) {
					pGuiGraphics.blitSprite(HOTBAR_DRAGONS_RAGE_FULL_4_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
				} else {
					pGuiGraphics.blitSprite(HOTBAR_DRAGONS_RAGE_PROGRESS_SPRITE, pScreenCenterX - 8, pScreenCenterY, 16, 32);
				}

			} else {
				pGuiGraphics.blitSprite(HOTBAR_DRAGONS_RAGE_PROGRESS_SPRITE, pScreenCenterX - 8, pScreenCenterY, 16, 32);
				pGuiGraphics.blitSprite(HOTBAR_DRAGONS_RAGE_BACKGROUND_SPRITE, 16, 32, 0, 0, pScreenCenterX - 8, pScreenCenterY, 16, height);
			}
			RenderSystem.disableBlend();

			this.minecraft.getProfiler().pop();

		} else {
			int pScreenCenterX = pGuiGraphics.guiWidth() / 2 - 8;
			int pScreenCenterY = pGuiGraphics.guiHeight() / 2 - 14;
			int width = Mth.clamp(Mth.floor((float) DragonCharge / WLCommonConfig.CONFIG.DragonChargeCap.get() * 16f), 0, 16);

			this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			pGuiGraphics.blitSprite(CROSSHAIR_ABILITY_BAR_BACKGROUND_SPRITE,  pScreenCenterX, pScreenCenterY, 16, 4);
			if (pConfig == 2) {
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			}
			pGuiGraphics.blitSprite(CROSSHAIR_DRAGONS_RAGE_PROGRESS_SPRITE, 16, 4, 0, 0,  pScreenCenterX, pScreenCenterY, width, 4);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();

			this.minecraft.getProfiler().pop();
		}
	}

	public void renderPyrosweep(GuiGraphics pGuiGraphics, Player pPlayer, ClientLevel pClientLevel, int pConfig) {
		int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);

		if (pConfig == 0) {
			int pScreenCenterX = pGuiGraphics.guiWidth() / 2 - 6;
			int pScreenCenterY = pGuiGraphics.guiHeight() - 36 - 16 - 4;
			int height = 17 - Mth.clamp(Mth.floor((float) PyrosweepCharge / WLCommonConfig.CONFIG.PyrosweepChargeCap.get() * 16f), 0, 16);

			this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

			RenderSystem.enableBlend();
			pGuiGraphics.blitSprite(HOTBAR_PYROSWEEP_PROGRESS_SPRITE,  pScreenCenterX, pScreenCenterY, 12, 18);
			pGuiGraphics.blitSprite(HOTBAR_PYROSWEEP_BACKGROUND_SPRITE, 12, 18, 0, 0,  pScreenCenterX, pScreenCenterY, 12, height);
			RenderSystem.disableBlend();

			this.minecraft.getProfiler().pop();

		} else {
			int pScreenCenterX = pGuiGraphics.guiWidth() / 2 - 8;
			int pScreenCenterY = pGuiGraphics.guiHeight() / 2 - 14;
			int width = Mth.clamp(Mth.floor((float) PyrosweepCharge / WLCommonConfig.CONFIG.PyrosweepChargeCap.get() * 16f), 0, 16);

			this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			pGuiGraphics.blitSprite(CROSSHAIR_ABILITY_BAR_BACKGROUND_SPRITE,  pScreenCenterX, pScreenCenterY, 16, 4);
			if (pConfig == 2) {
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			}
			pGuiGraphics.blitSprite(CROSSHAIR_PYROSWEEP_PROGRESS_SPRITE, 16, 4, 0, 0,  pScreenCenterX, pScreenCenterY, width, 4);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();

			this.minecraft.getProfiler().pop();
		}
	}
}
