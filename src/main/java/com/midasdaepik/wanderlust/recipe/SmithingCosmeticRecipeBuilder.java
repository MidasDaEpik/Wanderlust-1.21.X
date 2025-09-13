package com.midasdaepik.wanderlust.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.AdvancementRewards.Builder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class SmithingCosmeticRecipeBuilder {
    private final RecipeCategory category;
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;

    final String cosmeticType;
    final int cosmeticMaterial;

    private final Map<String, Criterion<?>> criteria = new LinkedHashMap();

    public SmithingCosmeticRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition, String cosmeticType, int cosmeticMaterial) {
        this.category = category;
        this.template = template;
        this.base = base;
        this.addition = addition;

        this.cosmeticType = cosmeticType;
        this.cosmeticMaterial = cosmeticMaterial;
    }

    public static SmithingCosmeticRecipeBuilder smithingCosmetic(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, String cosmeticType, int cosmeticMaterial) {
        return new SmithingCosmeticRecipeBuilder(category, template, base, addition, cosmeticType, cosmeticMaterial);
    }

    public SmithingCosmeticRecipeBuilder unlocks(String key, Criterion<?> criterion) {
        this.criteria.put(key, criterion);
        return this;
    }

    public void save(RecipeOutput recipeOutput, ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        Advancement.Builder advancement$builder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(Builder.recipe(recipeId)).requirements(Strategy.OR);
        Map<String, Criterion<?>> var10000 = this.criteria;
        Objects.requireNonNull(advancement$builder);
        var10000.forEach(advancement$builder::addCriterion);
        SmithingCosmeticRecipe smithingcosmeticrecipe = new SmithingCosmeticRecipe(this.template, this.base, this.addition, this.cosmeticType, this.cosmeticMaterial);
        recipeOutput.accept(recipeId, smithingcosmeticrecipe, advancement$builder.build(recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation location) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(location));
        }
    }
}
