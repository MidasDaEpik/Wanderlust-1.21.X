package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

@Mod(value = Wanderlust.MOD_ID, dist = Dist.CLIENT)
public class WLClientEnumExtensions {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static final EnumProxy<Gui.HeartType> HEART_SCULK = new EnumProxy<>(
            Gui.HeartType.class,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/echo_full"),
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/echo_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/echo_half"),
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/echo_half_blinking"),
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/echo_hardcore_full"),
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/echo_hardcore_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/echo_hardcore_half"),
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "hud/heart/echo_hardcore_half_blinking")
    );
}
