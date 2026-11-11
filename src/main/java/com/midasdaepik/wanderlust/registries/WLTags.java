package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class WLTags {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static void initTags() {}

    //Item Tags
    public static final TagKey<Item> COOLDOWN_ON_EQUIP_ITEM =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "cooldown_on_equip_item"));
    public static final TagKey<Item> HIDE_OFFHAND_WHILE_USING_ITEMS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "hide_offhand_while_using_items"));
    public static final TagKey<Item> OFF_HAND_WEAPONS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "off_hand_weapons"));

    public static final TagKey<Item> WEAPON_MODIFIER_CLEAVE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "weapon_modifier/cleave"));
    public static final TagKey<Item> WEAPON_MODIFIER_CRITICAL =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "weapon_modifier/critical"));
    public static final TagKey<Item> WEAPON_MODIFIER_FOCUSED =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "weapon_modifier/focused"));
    public static final TagKey<Item> WEAPON_MODIFIER_OBSTINATE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "weapon_modifier/obstinate"));
    public static final TagKey<Item> WEAPON_MODIFIER_PIERCE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "weapon_modifier/pierce"));
    public static final TagKey<Item> WEAPON_MODIFIER_TWO_HANDED =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "weapon_modifier/two_handed"));

    public static final TagKey<Item> CUSTOM_SWEEP_PARTICLE_WEAPONS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "custom_sweep_particle_weapons"));

    public static final TagKey<Item> HOARDERS_BUNDLE_BLACKLIST =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "hoarders_bundle_blacklist"));

    public static final TagKey<Item> HEAD_EQUIPABLES =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "head_equipables"));
    public static final TagKey<Item> MASK_ATTACHABLES =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "mask_attachables"));

    public static final TagKey<Item> ENCHANTABLE_MASK =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "enchantable/mask"));
    public static final TagKey<Item> ENCHANTABLE_RANGED_WEAPON =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "enchantable/ranged_weapon"));
    public static final TagKey<Item> ENCHANTABLE_STEALTH =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "enchantable/stealth"));

    public static final TagKey<Item> COMPAT_BOSSES_OF_MASS_DESTRUCTION_BLAZING_EYE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "compat/bosses_of_mass_destruction/blazing_eye"));
    public static final TagKey<Item> COMPAT_WETLAND_WHIMSY_FILTHY_BONE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "compat/wetland_whimsy/filthy_bone"));
    public static final TagKey<Item> COMPAT_WETLAND_WHIMSY_RUSTED_ARTIFACT =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "compat/wetland_whimsy/rusted_artifact"));

    //Block Tags
    public static final TagKey<Block> SCULK_BLOCKS =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "sculk_blocks"));
    public static final TagKey<Block> SCULK_SURFACE_BLOCKS =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "sculk_surface_blocks"));
    public static final TagKey<Block> SCULK_CAN_BE_REPLACED =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "sculk_can_be_replaced"));
    public static final TagKey<Block> SCULK_HULL_REPLACABLE =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "sculk_hull_replacable"));
    public static final TagKey<Block> SCULK_TANGLE_REPLACABLE =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "sculk_tangle_replacable"));

    //Damage Type Tags
    public static final TagKey<DamageType> NO_COOLDOWN =
            TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "no_cooldown"));

    public static final TagKey<DamageType> MARKED_AFFECTS =
            TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "marked_affects"));

    //Enchantment Tags
    public static final TagKey<Enchantment> MASK_EXCLUSIVE =
            TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MOD_ID, "exclusive_set/mask"));
}
