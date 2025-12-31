package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.recipe.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WLRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Wanderlust.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Wanderlust.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MaskCombineRecipe>> MASK_COMBINE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("mask_combine", () -> new SimpleCraftingRecipeSerializer<>(MaskCombineRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MaskRecipe>> MASK_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("mask_crafting_shaped", MaskRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MaskSeparateRecipe>> MASK_SEPARATE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("mask_separate", () -> new SimpleCraftingRecipeSerializer<>(MaskSeparateRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NbtKeepingShapedRecipe>> NBT_KEEPING_SHAPED_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("nbt_keeping_crafting_shaped", NbtKeepingShapedRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SmithingCosmeticRecipe>> SMITHING_COSMETIC_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("smithing_cosmetic", SmithingCosmeticRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
