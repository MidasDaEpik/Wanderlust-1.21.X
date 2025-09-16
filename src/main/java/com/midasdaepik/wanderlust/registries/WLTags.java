package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class WLTags {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static void initTags() {}

    public static final TagKey<Item> COOLDOWN_ON_EQUIP_ITEM =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "cooldown_on_equip_item"));
    public static final TagKey<Item> CRITLESS_WEAPONS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "critless_weapons"));
    public static final TagKey<Item> HIDE_OFFHAND_WHILE_USING_ITEMS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "hide_offhand_while_using_items"));
    public static final TagKey<Item> OFF_HAND_WEAPONS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "off_hand_weapons"));
    public static final TagKey<Item> TWO_HANDED_WEAPONS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "two_handed_weapons"));

    public static final TagKey<Item> HEAD_EQUIPABLES =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "head_equipables"));

    public static final TagKey<Item> COMPAT_BOSSES_OF_MASS_DESTRUCTION_BLAZING_EYE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "compat/bosses_of_mass_destruction/blazing_eye"));
    public static final TagKey<Item> COMPAT_WETLAND_WHIMSY_BLEMISH_ROD =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "compat/wetland_whimsy/blemish_rod"));
    public static final TagKey<Item> COMPAT_WETLAND_WHIMSY_RUSTED_ARTIFACT =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "compat/wetland_whimsy/rusted_artifact"));
}
