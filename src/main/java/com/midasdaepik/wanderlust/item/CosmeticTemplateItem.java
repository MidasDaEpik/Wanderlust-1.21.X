package com.midasdaepik.wanderlust.item;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class CosmeticTemplateItem extends SmithingTemplateItem {
    private static final ResourceLocation EMPTY_SLOT_HELMET = ResourceLocation.parse("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = ResourceLocation.parse("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = ResourceLocation.parse("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = ResourceLocation.parse("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_HOE = ResourceLocation.parse("item/empty_slot_hoe");
    private static final ResourceLocation EMPTY_SLOT_AXE = ResourceLocation.parse("item/empty_slot_axe");
    private static final ResourceLocation EMPTY_SLOT_SWORD = ResourceLocation.parse("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_SHOVEL = ResourceLocation.parse("item/empty_slot_shovel");
    private static final ResourceLocation EMPTY_SLOT_PICKAXE = ResourceLocation.parse("item/empty_slot_pickaxe");

    private static final ResourceLocation EMPTY_SLOT_INGOT = ResourceLocation.parse("item/empty_slot_ingot");
    private static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST = ResourceLocation.parse("item/empty_slot_redstone_dust");
    private static final ResourceLocation EMPTY_SLOT_QUARTZ = ResourceLocation.parse("item/empty_slot_quartz");
    private static final ResourceLocation EMPTY_SLOT_EMERALD = ResourceLocation.parse("item/empty_slot_emerald");
    private static final ResourceLocation EMPTY_SLOT_DIAMOND = ResourceLocation.parse("item/empty_slot_diamond");
    private static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI = ResourceLocation.parse("item/empty_slot_lapis_lazuli");
    private static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD = ResourceLocation.parse("item/empty_slot_amethyst_shard");

    private static final ResourceLocation EMPTY_SLOT_NETHER_STAR = ResourceLocation.parse("wanderlust:item/empty_slot_nether_star");

    private static final Component HALO = Component.translatable("item.wanderlust.halo_armor_effect_smithing_template_desc").withStyle(ChatFormatting.GRAY);
    private static final Component HALO_APPLIES_TO = Component.translatable("smithing_template.halo_armor_effect_smithing_template.applies_to").withStyle(ChatFormatting.GOLD);
    private static final Component HALO_INGREDIENTS = Component.translatable("smithing_template.halo_armor_effect_smithing_template.ingredients").withStyle(ChatFormatting.GOLD);
    private static final Component HALO_BASE_SLOT_DESCRIPTION = Component.translatable("smithing_template.halo_armor_effect_smithing_template.base_slot_description");
    private static final Component HALO_ADDITIONS_SLOT_DESCRIPTION = Component.translatable("smithing_template.halo_armor_effect_smithing_template.additions_slot_description");


    public CosmeticTemplateItem(Component pAppliesTo, Component pIngredients, Component pUpgradeDescription, Component pBaseSlotDescription, Component pAdditionsSlotDescription, List<ResourceLocation> pBaseSlotEmptyIcons, List<ResourceLocation> pAdditonalSlotEmptyIcons) {
        super(pAppliesTo, pIngredients, pUpgradeDescription, pBaseSlotDescription, pAdditionsSlotDescription, pBaseSlotEmptyIcons, pAdditonalSlotEmptyIcons);
    }

    public static CosmeticTemplateItem createHaloTemplate() {
        return new CosmeticTemplateItem(HALO_APPLIES_TO, HALO_INGREDIENTS, HALO, HALO_BASE_SLOT_DESCRIPTION, HALO_ADDITIONS_SLOT_DESCRIPTION, createHaloIconList(), createHaloMaterialList());
    }

    private static List<ResourceLocation> createHaloIconList() {
        return List.of(EMPTY_SLOT_HELMET);
    }

    private static List<ResourceLocation> createHaloMaterialList() {
        return List.of(EMPTY_SLOT_INGOT, EMPTY_SLOT_REDSTONE_DUST, EMPTY_SLOT_LAPIS_LAZULI, EMPTY_SLOT_QUARTZ, EMPTY_SLOT_DIAMOND, EMPTY_SLOT_EMERALD, EMPTY_SLOT_AMETHYST_SHARD);
    }

    private static List<ResourceLocation> createPlaceholderMaterialList() {
        return List.of(EMPTY_SLOT_INGOT, EMPTY_SLOT_REDSTONE_DUST, EMPTY_SLOT_LAPIS_LAZULI, EMPTY_SLOT_QUARTZ, EMPTY_SLOT_DIAMOND, EMPTY_SLOT_EMERALD, EMPTY_SLOT_AMETHYST_SHARD);
    }

    @Override
    public String getDescriptionId() {
        return Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this));
    }
}
