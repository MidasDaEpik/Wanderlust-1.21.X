package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class WLTags {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static void initTags() {}

    public static final TagKey<Item> CRITLESS_WEAPONS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "critless_weapons"));
    public static final TagKey<Item> HIDE_OFFHAND_WHILE_USING_ITEMS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "hide_offhand_while_using_items"));
    public static final TagKey<Item> OFF_HAND_WEAPONS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "off_hand_weapons"));
    public static final TagKey<Item> TWO_HANDED_WEAPONS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "two_handed_weapons"));

    public static final TagKey<Item> RECIPE_BOMD_BLAZING_EYE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "recipe/bomd_blazing_eye"));
}
