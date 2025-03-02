package com.midasdaepik.wanderlust.renderer.hud;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

public class WeaponAbilityHudOverlay implements LayeredDraw.Layer {
	private static final ResourceLocation CHARYBDIS_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/charybdis_bar_background");
	private static final ResourceLocation CHARYBDIS_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/charybdis_bar_progress");

	private static final ResourceLocation PYROSWEEP_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/pyrosweep_bar_background");
	private static final ResourceLocation PYROSWEEP_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/pyrosweep_bar_progress");

	private static final ResourceLocation DRAGONS_RAGE_BACKGROUND_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/dragons_rage_bar_background");
	private static final ResourceLocation DRAGONS_RAGE_PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/dragons_rage_bar_progress");
	private static final ResourceLocation DRAGONS_RAGE_FULL_0_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/dragons_rage_bar_full_0");
	private static final ResourceLocation DRAGONS_RAGE_FULL_1_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/dragons_rage_bar_full_1");
	private static final ResourceLocation DRAGONS_RAGE_FULL_2_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/dragons_rage_bar_full_2");
	private static final ResourceLocation DRAGONS_RAGE_FULL_3_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/dragons_rage_bar_full_3");
	private static final ResourceLocation DRAGONS_RAGE_FULL_4_SPRITE = ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "hud/dragons_rage_bar_full_4");

	private final Minecraft minecraft;

	public WeaponAbilityHudOverlay(Minecraft pMinecraft) {
		this.minecraft = pMinecraft;
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) {
		Player pPlayer = this.minecraft.player;
		ClientLevel pLevel = this.minecraft.level;
		if (pPlayer != null && pLevel != null) {
			if (pPlayer.getMainHandItem().getItem() == WLItems.CHARYBDIS.get()) {
				int pScreenCenterX = pGuiGraphics.guiWidth() / 2;
				int pScreenCenterY = pGuiGraphics.guiHeight() - 36 - 16 - 4;
				int CharybdisCharge = pPlayer.getData(CHARYBDIS_CHARGE);
				int height = 15 - Mth.clamp(Mth.floor(CharybdisCharge / 100f), 0, 14);

				this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

				RenderSystem.enableBlend();
				pGuiGraphics.blitSprite(CHARYBDIS_PROGRESS_SPRITE,  pScreenCenterX - 9, pScreenCenterY, 18, 18);
				pGuiGraphics.blitSprite(CHARYBDIS_BACKGROUND_SPRITE, 18, 18, 0, 0,  pScreenCenterX - 9, pScreenCenterY, 18, height);
				RenderSystem.disableBlend();

				this.minecraft.getProfiler().pop();

			} else if (pPlayer.getMainHandItem().getItem() == WLItems.PYROSWEEP.get()) {
				int pScreenCenterX = pGuiGraphics.guiWidth() / 2;
				int pScreenCenterY = pGuiGraphics.guiHeight() - 36 - 16 - 4;
				int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
				int height = 17 - PyrosweepCharge;

				this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

				RenderSystem.enableBlend();
				pGuiGraphics.blitSprite(PYROSWEEP_PROGRESS_SPRITE,  pScreenCenterX - 6, pScreenCenterY, 12, 18);
				pGuiGraphics.blitSprite(PYROSWEEP_BACKGROUND_SPRITE, 12, 18, 0, 0,  pScreenCenterX - 6, pScreenCenterY, 12, height);
				RenderSystem.disableBlend();

				this.minecraft.getProfiler().pop();

			}else if (pPlayer.getMainHandItem().getItem() == WLItems.DRAGONS_RAGE.get()) {
				int pScreenCenterX = pGuiGraphics.guiWidth() / 2;
				int pScreenCenterY = pGuiGraphics.guiHeight() - 38 - 32;
				int DragonsRageCharge = pPlayer.getData(DRAGONS_RAGE_CHARGE);
				int height = 30 - Mth.clamp(Mth.floor(DragonsRageCharge / 100f), 0, 18);
				if (DragonsRageCharge > 0) {
					height -= 1;
				}
				this.minecraft.getProfiler().push("weapon_ability_hud_overlay");

				RenderSystem.enableBlend();
				if (DragonsRageCharge == 1800) {
					double Timer = pLevel.getGameTime();
					if (Timer % 29 == 0 || Timer % 29 == 1) {
						pGuiGraphics.blitSprite(DRAGONS_RAGE_FULL_0_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
					} else if (Timer % 29 == 2 || Timer % 29 == 3) {
						pGuiGraphics.blitSprite(DRAGONS_RAGE_FULL_1_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
					} else if (Timer % 29 == 4 || Timer % 29 == 5) {
						pGuiGraphics.blitSprite(DRAGONS_RAGE_FULL_2_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
					} else if (Timer % 29 == 6 || Timer % 29 == 7) {
						pGuiGraphics.blitSprite(DRAGONS_RAGE_FULL_3_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
					} else if (Timer % 29 == 8 || Timer % 29 == 9) {
						pGuiGraphics.blitSprite(DRAGONS_RAGE_FULL_4_SPRITE, pScreenCenterX - 10, pScreenCenterY, 20, 32);
					} else {
						pGuiGraphics.blitSprite(DRAGONS_RAGE_PROGRESS_SPRITE, pScreenCenterX - 8, pScreenCenterY, 16, 32);
					}

				} else {
					pGuiGraphics.blitSprite(DRAGONS_RAGE_PROGRESS_SPRITE, pScreenCenterX - 8, pScreenCenterY, 16, 32);
					pGuiGraphics.blitSprite(DRAGONS_RAGE_BACKGROUND_SPRITE, 16, 32, 0, 0, pScreenCenterX - 8, pScreenCenterY, 16, height);
				}
				RenderSystem.disableBlend();

				this.minecraft.getProfiler().pop();
			}
		}
	}
}
