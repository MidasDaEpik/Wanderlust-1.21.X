package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.RRItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class RRRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public RRRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {

        copySmithingTemplate(pRecipeOutput, RRItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SOUL_SAND);

        trimSmithing(pRecipeOutput, RRItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "atrophy")
                .withPath("atrophy_armor_trim_smithing_template_smithing_trim"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RRItems.CATALYST_CHALICE)
                .pattern("EGE")
                .pattern("ESE")
                .pattern(" E ")
                .define('E', Items.ECHO_SHARD)
                .define('G', Items.GOLD_BLOCK)
                .define('S', Items.SCULK_CATALYST)
                .unlockedBy("has_condition", has(Items.ECHO_SHARD)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.DRAGONS_BREATH_ARBALEST)
                .pattern("DGD")
                .pattern("STS")
                .pattern(" D ")
                .define('D', RRItems.DRAGONBONE)
                .define('G', Items.GOLD_BLOCK)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('S', Items.STRING)
                .unlockedBy("has_condition", has(RRItems.DRAGONBONE)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.DRAGONS_RAGE)
                .pattern("D")
                .pattern("D")
                .pattern("G")
                .define('D', RRItems.DRAGONBONE)
                .define('G', Items.GOLD_BLOCK)
                .unlockedBy("has_condition", has(RRItems.DRAGONBONE)).save(pRecipeOutput);

        nineBlockReversibleCompactingRecipe(pRecipeOutput, Items.ECHO_SHARD, RRItems.ECHO_GEM);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.ELDER_CHESTPLATE)
                .pattern("S S")
                .pattern("ECE")
                .pattern("SCS")
                .define('E', RRItems.ELDER_SPINE)
                .define('S', Items.PRISMARINE_SHARD)
                .define('C', Items.PRISMARINE_CRYSTALS)
                .unlockedBy("has_condition", has(RRItems.ELDER_SPINE)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.FIRESTORM_KATANA)
                .pattern("B ")
                .pattern("BN")
                .pattern("N ")
                .define('N', Items.NETHERITE_SCRAP)
                .define('B', Items.BLAZE_ROD)
                .unlockedBy("has_condition", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance())).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.LYRE_OF_ECHOES)
                .pattern("BEB")
                .pattern("SSS")
                .pattern("BEB")
                .define('E', RRItems.ECHO_GEM)
                .define('B', Items.BONE_BLOCK)
                .define('S', Items.STRING)
                .unlockedBy("has_condition", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance())).save(pRecipeOutput);

        smithingReversible(pRecipeOutput, RRItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE, RRItems.FIRESTORM_KATANA, RRItems.REFINED_WITHERBLADE, RRItems.MYCORIS, RecipeCategory.COMBAT, CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.OBSIDIAN_BULWARK)
                .pattern(" O ")
                .pattern("OGO")
                .pattern("GSG")
                .define('O', Items.OBSIDIAN)
                .define('G', Items.GOLD_BLOCK)
                .define('S', Items.STICK)
                .unlockedBy("has_condition", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance())).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.SCYLLA)
                .pattern("e e")
                .pattern("EBE")
                .pattern(" B ")
                .define('E', RRItems.ECHO_GEM)
                .define('e', Items.ECHO_SHARD)
                .define('B', Items.BONE_BLOCK)
                .unlockedBy("has_condition", has(Items.ECHO_SHARD)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.SEARING_STAFF)
                .pattern("NFN")
                .pattern(" B ")
                .pattern(" N ")
                .define('N', Items.NETHERITE_SCRAP)
                .define('F', Items.FIRE_CHARGE)
                .define('B', Items.BLAZE_ROD)
                .unlockedBy("has_condition", has(Items.ECHO_SHARD)).save(pRecipeOutput);

        smithingReversible(pRecipeOutput, RRItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE, RRItems.OBSIDIAN_BULWARK, RRItems.REFINED_WITHERBLADE, RRItems.SOULGORGE, RecipeCategory.COMBAT, CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.TRIDENT)
                .pattern(" EE")
                .pattern(" SE")
                .pattern("S  ")
                .define('E', RRItems.ELDER_SPINE)
                .define('S', Items.PRISMARINE_SHARD)
                .unlockedBy("has_condition", has(RRItems.ELDER_SPINE)).save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "trident"));

        copySmithingTemplate(pRecipeOutput, RRItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE, Items.OBSIDIAN);

        trimSmithing(pRecipeOutput, RRItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "tyrant")
                .withPath("tyrant_armor_trim_smithing_template_smithing_trim"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.WARPED_RAPIER)
                .pattern(" E")
                .pattern(" E")
                .pattern("EO")
                .define('E', Items.ENDER_PEARL)
                .define('O', Items.CRYING_OBSIDIAN)
                .unlockedBy("has_condition", has(Items.ENDER_PEARL)).save(pRecipeOutput);

        smithingReversible(pRecipeOutput, RRItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE, RRItems.WARPED_RAPIER, RRItems.REFINED_WITHERBLADE, RRItems.WARPTHISTLE, RecipeCategory.COMBAT, CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RRItems.WHISPERWIND)
                .pattern(" BS")
                .pattern("H S")
                .pattern(" BS")
                .define('H', Items.HEAVY_CORE)
                .define('B', Items.BREEZE_ROD)
                .define('S', Items.STRING)
                .unlockedBy("has_condition", has(Items.HEAVY_CORE)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RRItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE)
                .pattern("Wnw")
                .pattern("nNn")
                .pattern("MnT")
                .define('N', Items.NETHER_STAR)
                .define('W', Items.WITHER_ROSE)
                .define('w', Items.WEEPING_VINES)
                .define('M', Items.MAGMA_BLOCK)
                .define('T', Items.TWISTING_VINES)
                .define('n', Items.NETHERITE_SCRAP)
                .unlockedBy("has_condition", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance())).save(pRecipeOutput);
    }

    protected static void nineBlockReversibleCompactingRecipe(RecipeOutput pRecipeOutput, ItemLike pUncompacted, ItemLike pCompacted) {
        String pUncompactedName = pUncompacted.asItem().toString().split(":")[1];
        String pCompactedName = pCompacted.asItem().toString().split(":")[1];

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pCompacted, 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', pUncompacted)
                .unlockedBy("has_item", has(pUncompacted)).save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, pCompactedName));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pUncompacted, 9)
                .requires(pCompacted, 1)
                .unlockedBy("has_item", has(pCompacted)).save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, pUncompactedName + "_from_" + pCompactedName));
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
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, pPath));
    }
}
