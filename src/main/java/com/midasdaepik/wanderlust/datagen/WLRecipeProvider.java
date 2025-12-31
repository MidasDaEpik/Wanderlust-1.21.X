package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.recipe.MaskRecipeBuilder;
import com.midasdaepik.wanderlust.recipe.NbtKeepingShapedRecipeBuilder;
import com.midasdaepik.wanderlust.recipe.SmithingCosmeticRecipeBuilder;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class WLRecipeProvider extends RecipeProvider implements IConditionBuilder {
    static String MOD_ID = Wanderlust.MOD_ID;

    public WLRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {

        copySmithingTemplate(pRecipeOutput, WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SOUL_SAND);

        trimSmithing(pRecipeOutput, WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "atrophy_armor_trim_smithing_template_smithing_trim"));

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.BLAZE_REAP, 4)
                .pattern("nAN")
                .pattern(" M ")
                .pattern(" n ")
                .define('M', WLItems.MOLTEN_PICKAXE)
                .define('A', WLItems.ANCIENT_TABLET_REINFORCEMENT)
                .define('N', Items.NETHERITE_INGOT)
                .define('n', Items.NETHERITE_SCRAP)
                .unlockedBy("has_condition", has(WLItems.ANCIENT_TABLET_REINFORCEMENT)).save(pRecipeOutput.withConditions(not(modLoaded("bosses_of_mass_destruction"))));

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.BLAZE_REAP, 4)
                .pattern("nBN")
                .pattern(" M ")
                .pattern(" n ")
                .define('M', WLItems.MOLTEN_PICKAXE)
                .define('B', WLTags.COMPAT_BOSSES_OF_MASS_DESTRUCTION_BLAZING_EYE)
                .define('N', Items.NETHERITE_INGOT)
                .define('n', Items.NETHERITE_SCRAP)
                .unlockedBy("has_condition", has(WLTags.COMPAT_BOSSES_OF_MASS_DESTRUCTION_BLAZING_EYE)).save(pRecipeOutput.withConditions(modLoaded("bosses_of_mass_destruction")), ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "blaze_reap_bomd_compat"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.CHARYBDIS)
                .pattern("SEC")
                .pattern(" BE")
                .pattern("S S")
                .define('E', WLItems.ELDER_SPINE)
                .define('B', Items.COPPER_BLOCK)
                .define('S', Items.PRISMARINE_SHARD)
                .define('C', Items.PRISMARINE_CRYSTALS)
                .unlockedBy("has_condition", has(WLItems.ELDER_SPINE)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WLItems.CATALYST_CHALICE)
                .pattern("EGE")
                .pattern("ESE")
                .pattern(" E ")
                .define('E', Items.ECHO_SHARD)
                .define('G', Items.GOLD_BLOCK)
                .define('S', Items.SCULK_CATALYST)
                .unlockedBy("has_condition", has(Items.ECHO_SHARD)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WLItems.CATALYST_CRYSTAL)
                .pattern(" EE")
                .pattern("ESE")
                .pattern("EE ")
                .define('E', Items.ECHO_SHARD)
                .define('S', Items.SCULK_CATALYST)
                .unlockedBy("has_condition", has(Items.ECHO_SHARD)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.DRAGONS_BREATH_ARBALEST)
                .pattern("DGD")
                .pattern("STS")
                .pattern(" D ")
                .define('D', WLItems.DRAGONBONE)
                .define('G', Items.GOLD_BLOCK)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('S', Items.STRING)
                .unlockedBy("has_condition", has(WLItems.DRAGONBONE)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.DRAGONS_RAGE)
                .pattern("D")
                .pattern("D")
                .pattern("G")
                .define('D', WLItems.DRAGONBONE)
                .define('G', Items.GOLD_BLOCK)
                .unlockedBy("has_condition", has(WLItems.DRAGONBONE)).save(pRecipeOutput);

        nineBlockReversibleCompactingRecipe(pRecipeOutput, Items.ECHO_SHARD, WLItems.ECHO_GEM);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.ECHO_TUNER)
                .pattern("e e")
                .pattern("EBE")
                .pattern(" B ")
                .define('E', WLItems.ECHO_GEM)
                .define('e', Items.ECHO_SHARD)
                .define('B', Items.BONE_BLOCK)
                .unlockedBy("has_condition", has(Items.ECHO_SHARD)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.ELDER_CHESTPLATE)
                .pattern("S S")
                .pattern("EHE")
                .pattern("SCS")
                .define('E', WLItems.ELDER_SPINE)
                .define('H', Items.HEART_OF_THE_SEA)
                .define('S', Items.PRISMARINE_SHARD)
                .define('C', Items.PRISMARINE_CRYSTALS)
                .unlockedBy("has_condition", has(WLItems.ELDER_SPINE)).save(pRecipeOutput);

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.FANGS_OF_FROST, 4)
                .pattern("BBB")
                .pattern("BDB")
                .pattern("BBB")
                .define('D', WLItems.DAGGER)
                .define('B', Items.BLUE_ICE)
                .unlockedBy("has_condition", has(WLItems.DAGGER)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.FIRESTORM_KATANA)
                .pattern("B ")
                .pattern("BN")
                .pattern("NA")
                .define('A', WLItems.ANCIENT_TABLET_IMBUEMENT)
                .define('N', Items.NETHERITE_SCRAP)
                .define('B', Items.BLAZE_ROD)
                .unlockedBy("has_condition", has(WLItems.ANCIENT_TABLET_IMBUEMENT)).save(pRecipeOutput);

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.GOLD_INGOT, "halo", 0,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_0"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.AMETHYST_SHARD, "halo", 1,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_1"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.QUARTZ, "halo", 2,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_2"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.COPPER_INGOT, "halo", 3,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_3"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.LAPIS_LAZULI, "halo", 4,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_4"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.DIAMOND, "halo", 5,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_5"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.REDSTONE, "halo", 6,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_6"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.NETHERITE_INGOT, "halo", 7,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_7"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.EMERALD, "halo", 8,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_8"));

        cosmeticSmithing(pRecipeOutput, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE, Ingredient.of(WLTags.HEAD_EQUIPABLES), Items.IRON_INGOT, "halo", 9,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "halo_armor_effect_smithing_template_smithing_effect_9"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.KERIS)
                .pattern("R")
                .pattern("B")
                .define('B', WLTags.COMPAT_WETLAND_WHIMSY_BLEMISH_ROD)
                .define('R', WLTags.COMPAT_WETLAND_WHIMSY_RUSTED_ARTIFACT)
                .unlockedBy("has_condition", has(WLItems.ELDER_SPINE)).save(pRecipeOutput.withConditions(modLoaded("wetland_whimsy")));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.MASK)
                .pattern("SSS")
                .pattern("S S")
                .pattern("SLS")
                .define('S', Items.STRING)
                .define('L', Items.LEATHER)
                .unlockedBy("has_condition", has(Items.STRING)).save(pRecipeOutput);

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 1)
                .pattern("FBF")
                .pattern("BMB")
                .define('M', WLItems.MASK.get())
                .define('B', Items.BREEZE_ROD)
                .define('F', Items.FEATHER)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_1");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 2)
                .pattern("SSS")
                .pattern("OMO")
                .define('M', WLItems.MASK.get())
                .define('O', Items.ORANGE_WOOL)
                .define('S', Items.SWEET_BERRIES)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_2");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 3)
                .pattern("PGG")
                .pattern("PMA")
                .define('M', WLItems.MASK.get())
                .define('P', Items.PHANTOM_MEMBRANE)
                .define('G', Items.GOLD_INGOT)
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_3");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 4)
                .pattern("SBS")
                .pattern("SMS")
                .define('M', WLItems.MASK.get())
                .define('S', Items.MOSS_BLOCK)
                .define('B', Items.SPORE_BLOSSOM)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_4");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 5)
                .pattern("GGG")
                .pattern("GMG")
                .define('M', WLItems.MASK.get())
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_5");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 6)
                .pattern("PPP")
                .pattern("PMP")
                .define('M', WLItems.MASK.get())
                .define('P', Items.PRISMARINE_SHARD)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_6");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 7)
                .pattern("SRO")
                .pattern("SMO")
                .define('M', WLItems.MASK.get())
                .define('S', Items.SNOW)
                .define('O', Items.OBSIDIAN)
                .define('R', Items.ROSE_BUSH)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_7");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 8)
                .pattern("GQG")
                .pattern("QMQ")
                .define('M', WLItems.MASK.get())
                .define('Q', Items.QUARTZ)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_8");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 9)
                .pattern("LLL")
                .pattern("LML")
                .define('M', WLItems.MASK.get())
                .define('L', Items.MAGMA_BLOCK)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_9");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 10)
                .pattern("SHS")
                .pattern("SMS")
                .define('M', WLItems.MASK.get())
                .define('S', Items.SCULK)
                .define('H', Items.SCULK_SHRIEKER)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_10");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 11)
                .pattern("BLR")
                .pattern("RMB")
                .define('M', WLItems.MASK.get())
                .define('R', Items.RED_CONCRETE)
                .define('B', Items.BLACK_CONCRETE)
                .define('L', Items.LIME_CONCRETE)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_11");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 12)
                .pattern("QGQ")
                .pattern("QMQ")
                .define('M', WLItems.MASK.get())
                .define('Q', Items.QUARTZ)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_12");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 13)
                .pattern("LLL")
                .pattern("EME")
                .define('M', WLItems.MASK.get())
                .define('L', ItemTags.LOGS)
                .define('E', ItemTags.LEAVES)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_13");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 14)
                .pattern("BBB")
                .pattern("BMB")
                .define('M', WLItems.MASK.get())
                .define('B', Items.BONE)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_14");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 15)
                .pattern("ICF")
                .pattern("IMF")
                .define('M', WLItems.MASK.get())
                .define('I', Items.BLUE_ICE)
                .define('C', Items.CALCITE)
                .define('F', Items.MAGMA_BLOCK)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_15");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 16)
                .pattern("BNB")
                .pattern("NMN")
                .define('M', WLItems.MASK.get())
                .define('N', Items.NETHER_WART_BLOCK)
                .define('B', Items.BONE_BLOCK)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_16");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 17)
                .pattern("JGJ")
                .pattern("JMG")
                .define('M', WLItems.MASK.get())
                .define('J', Items.JUNGLE_PLANKS)
                .define('G', Items.GLOWSTONE_DUST)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_17");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 18)
                .pattern("YCB")
                .pattern("RMP")
                .define('M', WLItems.MASK.get())
                .define('R', Items.RED_DYE)
                .define('Y', Items.YELLOW_DYE)
                .define('C', Items.CYAN_DYE)
                .define('B', Items.BLUE_DYE)
                .define('P', Items.PURPLE_DYE)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_18");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 19)
                .pattern("PPP")
                .pattern("PMP")
                .define('M', WLItems.MASK.get())
                .define('P', Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_19");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 20)
                .pattern("BBB")
                .pattern("BMB")
                .define('M', WLItems.MASK.get())
                .define('B', Items.BLAZE_POWDER)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_20");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 21)
                .pattern("OOO")
                .pattern("CMC")
                .define('M', WLItems.MASK.get())
                .define('O', Items.OBSIDIAN)
                .define('C', Items.CRYING_OBSIDIAN)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_21");

        MaskRecipeBuilder.shaped(RecipeCategory.COMBAT, 22)
                .pattern("SRC")
                .pattern("SMC")
                .define('M', WLItems.MASK.get())
                .define('R', Items.RED_MUSHROOM)
                .define('S', Items.MOSS_BLOCK)
                .define('C', Items.COARSE_DIRT)
                .unlockedBy("has_condition", has(WLItems.MASK.get())).save(pRecipeOutput, "mask_22");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.MOLTEN_PICKAXE)
                .pattern("BAB")
                .pattern(" N ")
                .pattern(" N ")
                .define('A', WLItems.ANCIENT_TABLET_IMBUEMENT)
                .define('N', Items.NETHERITE_SCRAP)
                .define('B', Items.BLAZE_ROD)
                .unlockedBy("has_condition", has(WLItems.ANCIENT_TABLET_IMBUEMENT)).save(pRecipeOutput);

        smithingReversible(pRecipeOutput, WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE, WLItems.FIRESTORM_KATANA, WLItems.REFINED_WITHERBLADE, WLItems.MYCORIS, RecipeCategory.COMBAT,has(WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.OBSIDIAN_BULWARK)
                .pattern("OAO")
                .pattern("OGO")
                .pattern("GSG")
                .define('A', WLItems.ANCIENT_TABLET_REINFORCEMENT)
                .define('O', Items.OBSIDIAN)
                .define('G', Items.GOLD_BLOCK)
                .define('S', Items.STICK)
                .unlockedBy("has_condition", has(WLItems.ANCIENT_TABLET_REINFORCEMENT)).save(pRecipeOutput);

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.PHANTOM_BOOTS, 4)
                .pattern("P P")
                .pattern("PLP")
                .define('L', Items.LEATHER_BOOTS)
                .define('P', Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_condition", has(Items.PHANTOM_MEMBRANE)).save(pRecipeOutput);

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.PHANTOM_CLOAK, 7)
                .pattern("PPP")
                .pattern("PPP")
                .pattern("PLP")
                .define('L', Items.LEATHER_CHESTPLATE)
                .define('P', Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_condition", has(Items.PHANTOM_MEMBRANE)).save(pRecipeOutput);

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.PHANTOM_HOOD, 4)
                .pattern("PPP")
                .pattern("PLP")
                .define('L', Items.LEATHER_HELMET)
                .define('P', Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_condition", has(Items.PHANTOM_MEMBRANE)).save(pRecipeOutput);

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.PHANTOM_LEGGINGS, 4)
                .pattern("PPP")
                .pattern("PLP")
                .pattern("P P")
                .define('L', Items.LEATHER_LEGGINGS)
                .define('P', Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_condition", has(Items.PHANTOM_MEMBRANE)).save(pRecipeOutput);

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.PHANTOM_TUNIC, 1)
                .pattern("PLP")
                .pattern("PPP")
                .pattern("PPP")
                .define('L', Items.LEATHER_CHESTPLATE)
                .define('P', Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_condition", has(Items.PHANTOM_MEMBRANE)).save(pRecipeOutput);

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.PIGLIN_WARAXE, 4)
                .pattern("BBN")
                .pattern("BGA")
                .pattern(" N ")
                .define('A', WLItems.ANCIENT_TABLET_REINFORCEMENT)
                .define('G', Items.GOLDEN_AXE)
                .define('N', Items.NETHERITE_SCRAP)
                .define('B', Items.GOLD_BLOCK)
                .unlockedBy("has_condition", has(WLItems.ANCIENT_TABLET_REINFORCEMENT)).save(pRecipeOutput);

        smithingReversible(pRecipeOutput, WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE, Items.NETHERITE_SWORD, WLItems.REFINED_WITHERBLADE, WLItems.PYROSWEEP, RecipeCategory.COMBAT, has(WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE));

        NbtKeepingShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.REFINED_WITHERBLADE, 4)
                .pattern("BAB")
                .pattern("NWN")
                .pattern("BNB")
                .define('A', WLItems.ANCIENT_TABLET_REINFORCEMENT)
                .define('W', WLItems.WITHERBLADE)
                .define('N', Items.NETHERITE_SCRAP)
                .define('B', Items.BLACKSTONE)
                .unlockedBy("has_condition", has(WLItems.ANCIENT_TABLET_REINFORCEMENT)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.SEARING_STAFF)
                .pattern("NAN")
                .pattern(" B ")
                .pattern(" N ")
                .define('A', WLItems.ANCIENT_TABLET_IMBUEMENT)
                .define('N', Items.NETHERITE_SCRAP)
                .define('B', Items.BLAZE_ROD)
                .unlockedBy("has_condition", has(WLItems.ANCIENT_TABLET_IMBUEMENT)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.SONIC_ARPEGGIO)
                .pattern("BEB")
                .pattern("SSS")
                .pattern("BEB")
                .define('E', WLItems.ECHO_GEM)
                .define('B', Items.BONE_BLOCK)
                .define('S', Items.STRING)
                .unlockedBy("has_condition", has(Items.ECHO_SHARD)).save(pRecipeOutput);

        smithingReversible(pRecipeOutput, WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE, WLItems.OBSIDIAN_BULWARK, WLItems.REFINED_WITHERBLADE, WLItems.SOULGORGE, RecipeCategory.COMBAT, has(WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.TRIDENT)
                .pattern(" EE")
                .pattern(" SE")
                .pattern("S  ")
                .define('E', WLItems.ELDER_SPINE)
                .define('S', Items.PRISMARINE_SHARD)
                .unlockedBy("has_condition", has(WLItems.ELDER_SPINE)).save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(MOD_ID, "trident"));

        copySmithingTemplate(pRecipeOutput, WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE, Items.OBSIDIAN);

        trimSmithing(pRecipeOutput, WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "tyrant_armor_trim_smithing_template_smithing_trim"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.WARPED_RAPIER)
                .pattern(" E")
                .pattern(" E")
                .pattern("EO")
                .define('E', Items.ENDER_PEARL)
                .define('O', Items.CRYING_OBSIDIAN)
                .unlockedBy("has_condition", has(Items.ENDER_PEARL)).save(pRecipeOutput);

        smithingReversible(pRecipeOutput, WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE, WLItems.WARPED_RAPIER, WLItems.REFINED_WITHERBLADE, WLItems.WARPTHISTLE, RecipeCategory.COMBAT, has(WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WLItems.WHISPERWIND)
                .pattern(" BS")
                .pattern("H S")
                .pattern(" BS")
                .define('H', Items.HEAVY_CORE)
                .define('B', Items.BREEZE_ROD)
                .define('S', Items.STRING)
                .unlockedBy("has_condition", has(Items.HEAVY_CORE)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE)
                .pattern("WAw")
                .pattern("nNn")
                .pattern("MnT")
                .define('A', WLItems.ANCIENT_TABLET_IMBUEMENT)
                .define('N', Items.NETHER_STAR)
                .define('W', Items.WITHER_ROSE)
                .define('w', Items.WEEPING_VINES)
                .define('M', Items.MAGMA_BLOCK)
                .define('T', Items.TWISTING_VINES)
                .define('n', Items.NETHERITE_SCRAP)
                .unlockedBy("has_condition", has(WLItems.ANCIENT_TABLET_IMBUEMENT)).save(pRecipeOutput);
    }

    protected static void cosmeticSmithing(RecipeOutput recipeOutput, ItemLike ingredientItem, ItemLike ingredientBase, ItemLike ingredientAddition, String cosmeticType, int cosmeticMaterial, ResourceLocation location) {
        SmithingCosmeticRecipeBuilder.smithingCosmetic(Ingredient.of(ingredientItem), Ingredient.of(ingredientBase), Ingredient.of(ingredientAddition), RecipeCategory.MISC, cosmeticType, cosmeticMaterial).unlocks("has_smithing_effect_template", has(ingredientItem)).save(recipeOutput, location);
    }

    protected static void cosmeticSmithing(RecipeOutput recipeOutput, ItemLike ingredientItem, Ingredient ingredientBase, ItemLike ingredientAddition, String cosmeticType, int cosmeticMaterial, ResourceLocation location) {
        SmithingCosmeticRecipeBuilder.smithingCosmetic(Ingredient.of(ingredientItem), ingredientBase, Ingredient.of(ingredientAddition), RecipeCategory.MISC, cosmeticType, cosmeticMaterial).unlocks("has_smithing_effect_template", has(ingredientItem)).save(recipeOutput, location);
    }

    protected static void nineBlockReversibleCompactingRecipe(RecipeOutput pRecipeOutput, ItemLike pUncompacted, ItemLike pCompacted) {
        String pUncompactedName = pUncompacted.asItem().toString().split(":")[1];
        String pCompactedName = pCompacted.asItem().toString().split(":")[1];

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pCompacted, 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', pUncompacted)
                .unlockedBy("has_item", has(pUncompacted)).save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(MOD_ID, pCompactedName));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pUncompacted, 9)
                .requires(pCompacted, 1)
                .unlockedBy("has_item", has(pCompacted)).save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(MOD_ID, pUncompactedName + "_from_" + pCompactedName));
    }

    protected static void smithingReversible(RecipeOutput pRecipeOutput, ItemLike pTemplate, ItemLike pBase, ItemLike pAddition, ItemLike pResult, RecipeCategory pRecipeCategory, Criterion<?> pCriterion) {
        smithingTransform(pRecipeOutput, pTemplate, pBase, pAddition, pResult, pRecipeCategory, pCriterion);
        smithingTransform(pRecipeOutput, pTemplate, pAddition, pBase, pResult, pRecipeCategory, pCriterion, pResult.asItem().toString().split(":")[1] + "_reverse");
    }

    protected static void smithingTransform(RecipeOutput pRecipeOutput, ItemLike pTemplate, ItemLike pBase, ItemLike pAddition, ItemLike pResult, RecipeCategory pRecipeCategory, ItemLike pConditionItem) {
        smithingTransform(pRecipeOutput, pTemplate, pBase, pAddition, pResult, pRecipeCategory, RecipeProvider.has(pConditionItem));
    }

    protected static void smithingTransform(RecipeOutput pRecipeOutput, ItemLike pTemplate, ItemLike pBase, ItemLike pAddition, ItemLike pResult, RecipeCategory pRecipeCategory, Criterion<?> pCriterion) {
        smithingTransform(pRecipeOutput, pTemplate, pBase, pAddition, pResult, pRecipeCategory, pCriterion, pResult.asItem().toString().split(":")[1]);
    }

    protected static void smithingTransform(RecipeOutput pRecipeOutput, ItemLike pTemplate, ItemLike pBase, ItemLike pAddition, ItemLike pResult, RecipeCategory pRecipeCategory, Criterion<?> pCriterion, String pPath) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(pTemplate), Ingredient.of(pBase), Ingredient.of(pAddition), pRecipeCategory, pResult.asItem())
                .unlocks("has_condition", pCriterion)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(MOD_ID, pPath));
    }
}
