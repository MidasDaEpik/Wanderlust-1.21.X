package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.neoforged.neoforge.registries.DeferredItem;

public class WLTrimPatterns {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static final ResourceKey<TrimPattern> ATROPHY = ResourceKey.create(Registries.TRIM_PATTERN,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "atrophy"));

    public static final ResourceKey<TrimPattern> TYRANT = ResourceKey.create(Registries.TRIM_PATTERN,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "tyrant"));

    public static void bootstrap(BootstrapContext<TrimPattern> context) {
        register(context, WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE, ATROPHY);
        register(context, WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE, TYRANT);
    }

    public static void register(BootstrapContext<TrimPattern> context, DeferredItem<Item> item, ResourceKey<TrimPattern> key) {
        TrimPattern trimpattern = new TrimPattern(key.location(), item.getDelegate(),
                Component.translatable(Util.makeDescriptionId("trim_pattern", key.location())), false);
        context.register(key, trimpattern);
    }
}
