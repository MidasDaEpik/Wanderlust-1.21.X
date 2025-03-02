package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

@Mod(value = Wanderlust.MOD_ID, dist = Dist.CLIENT)
public class RRClientEnumExtensions {
    public static final EnumProxy<Gui.HeartType> HEART_FROSTBITE = new EnumProxy<>(
            Gui.HeartType.class, ResourceLocation.withDefaultNamespace("hud/heart/frozen_full"),
            ResourceLocation.withDefaultNamespace("hud/heart/frozen_full_blinking"),
            ResourceLocation.withDefaultNamespace("hud/heart/frozen_half"),
            ResourceLocation.withDefaultNamespace("hud/heart/frozen_half_blinking"),
            ResourceLocation.withDefaultNamespace("hud/heart/frozen_hardcore_full"),
            ResourceLocation.withDefaultNamespace("hud/heart/frozen_hardcore_full_blinking"),
            ResourceLocation.withDefaultNamespace("hud/heart/frozen_hardcore_half"),
            ResourceLocation.withDefaultNamespace("hud/heart/frozen_hardcore_half_blinking")
    );
}
