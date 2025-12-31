package com.midasdaepik.wanderlust.client.renderer.hud;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CameraOverlay implements LayeredDraw.Layer {
	static String MOD_ID = Wanderlust.MOD_ID;

	private static final ResourceLocation PHANTASMAL_OVERLAY = ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/misc/phantasmal_outline.png");

	private final Minecraft minecraft;

	public CameraOverlay(Minecraft pMinecraft) {
		this.minecraft = pMinecraft;
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) {
		Player pPlayer = this.minecraft.player;
		ClientLevel pClientLevel = this.minecraft.level;

		if (pPlayer != null && pClientLevel != null && !this.minecraft.options.hideGui) {

			if (pPlayer.hasEffect(WLEffects.PHANTASMAL)) {
				this.renderTextureOverlay(pGuiGraphics, PHANTASMAL_OVERLAY, 1.0F);
			}
		}
	}

	public void renderTextureOverlay(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
		pGuiGraphics.blit(pShaderLocation, 0, 0, -90, 0.0F, 0.0F, pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight(), pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight());
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
