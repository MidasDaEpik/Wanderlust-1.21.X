package com.midasdaepik.wanderlust.item;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class UpgradeTemplateItem extends SmithingTemplateItem {
    private static final Component WITHERBLADE_UPGRADE = Component.translatable("item.wanderlust.witherblade_upgrade_smithing_template_desc").withStyle(ChatFormatting.GRAY);
    private static final Component WITHERBLADE_UPGRADE_APPLIES_TO = Component.translatable("smithing_template.witherblade_upgrade_smithing_template.applies_to").withStyle(ChatFormatting.BLUE);
    private static final Component WITHERBLADE_UPGRADE_INGREDIENTS = Component.translatable("smithing_template.witherblade_upgrade_smithing_template.ingredients").withStyle(ChatFormatting.BLUE);
    private static final Component WITHERBLADE_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable("smithing_template.witherblade_upgrade_smithing_template.base_slot_description");
    private static final Component WITHERBLADE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable("smithing_template.witherblade_upgrade_smithing_template.additions_slot_description");

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

    public UpgradeTemplateItem(Component pAppliesTo, Component pIngredients, Component pUpgradeDescription, Component pBaseSlotDescription, Component pAdditionsSlotDescription, List<ResourceLocation> pBaseSlotEmptyIcons, List<ResourceLocation> pAdditonalSlotEmptyIcons) {
        super(pAppliesTo, pIngredients, pUpgradeDescription, pBaseSlotDescription, pAdditionsSlotDescription, pBaseSlotEmptyIcons, pAdditonalSlotEmptyIcons);
    }

    public static UpgradeTemplateItem createWitherbladeUpgradeTemplate() {
        return new UpgradeTemplateItem(WITHERBLADE_UPGRADE_APPLIES_TO, WITHERBLADE_UPGRADE_INGREDIENTS, WITHERBLADE_UPGRADE, WITHERBLADE_UPGRADE_BASE_SLOT_DESCRIPTION, WITHERBLADE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createWitherbladeUpgradeIconList(), createWitherbladeUpgradeMaterialList());
    }

    private static List<ResourceLocation> createWitherbladeUpgradeIconList() {
        return List.of(EMPTY_SLOT_SWORD);
    }

    private static List<ResourceLocation> createWitherbladeUpgradeMaterialList() {
        return List.of(EMPTY_SLOT_NETHER_STAR);
    }

    @Override
    public String getDescriptionId() {
        return Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this));
    }
}
