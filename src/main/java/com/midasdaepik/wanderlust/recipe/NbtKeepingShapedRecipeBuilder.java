package com.midasdaepik.wanderlust.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.AdvancementRewards.Builder;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

public class NbtKeepingShapedRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final ItemStack resultStack;
    private final List<String> rows;
    private final Map<Character, Ingredient> key;
    private final Map<String, Criterion<?>> criteria;
    @Nullable
    private String group;
    private boolean showNotification;
    private final int nbtKeepingItemId;

    public NbtKeepingShapedRecipeBuilder(RecipeCategory category, ItemLike result, int nbtKeepingItemId, int count) {
        this(category, new ItemStack(result, count), nbtKeepingItemId);
    }

    public NbtKeepingShapedRecipeBuilder(RecipeCategory p_249996_, ItemStack result, int nbtKeepingItemId) {
        this.rows = Lists.newArrayList();
        this.key = Maps.newLinkedHashMap();
        this.criteria = new LinkedHashMap();
        this.showNotification = true;
        this.category = p_249996_;
        this.result = result.getItem();
        this.count = result.getCount();
        this.resultStack = result;
        this.nbtKeepingItemId = nbtKeepingItemId;
    }

    public static NbtKeepingShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result, int nbtKeepingItemId) {
        return shaped(category, result, nbtKeepingItemId, 1);
    }

    public static NbtKeepingShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result, int nbtKeepingItemId, int count) {
        return new NbtKeepingShapedRecipeBuilder(category, result, nbtKeepingItemId, count);
    }

    public static NbtKeepingShapedRecipeBuilder shaped(RecipeCategory p_251325_, ItemStack result, int nbtKeepingItemId) {
        return new NbtKeepingShapedRecipeBuilder(p_251325_, result, nbtKeepingItemId);
    }

    public NbtKeepingShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        return this.define(symbol, Ingredient.of(tag));
    }

    public NbtKeepingShapedRecipeBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(new ItemLike[]{item}));
    }

    public NbtKeepingShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
    }

    public NbtKeepingShapedRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != ((String)this.rows.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pattern);
            return this;
        }
    }

    public NbtKeepingShapedRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public NbtKeepingShapedRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    public NbtKeepingShapedRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        ShapedRecipePattern shapedrecipepattern = this.ensureValid(id);
        Advancement.Builder advancement$builder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(Builder.recipe(id)).requirements(Strategy.OR);
        Map<String, Criterion<?>> criteria = this.criteria;
        Objects.requireNonNull(advancement$builder);
        criteria.forEach(advancement$builder::addCriterion);
        NbtKeepingShapedRecipe shapedrecipe = new NbtKeepingShapedRecipe(Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), shapedrecipepattern, 4, this.resultStack, this.showNotification);
        recipeOutput.accept(id, shapedrecipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private ShapedRecipePattern ensureValid(ResourceLocation loaction) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(loaction));
        } else {
            return ShapedRecipePattern.of(this.key, this.rows);
        }
    }
}