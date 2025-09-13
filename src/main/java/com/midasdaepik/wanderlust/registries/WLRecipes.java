package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.recipe.NbtKeepingShapedRecipe;
import com.midasdaepik.wanderlust.recipe.SmithingCosmeticRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WLRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Wanderlust.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Wanderlust.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NbtKeepingShapedRecipe>> NBT_KEEPING_SHAPED_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("nbt_keeping_crafting_shaped", NbtKeepingShapedRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<NbtKeepingShapedRecipe>> NBT_KEEPING_SHAPED_RECIPE_TYPE =
            RECIPE_TYPES.register("nbt_keeping_crafting_shaped", () -> new RecipeType<NbtKeepingShapedRecipe>() {
                @Override
                public String toString() {
                    return "nbt_keeping_crafting_shaped";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SmithingCosmeticRecipe>> SMITHING_COSMETIC_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("smithing_cosmetic", SmithingCosmeticRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<SmithingCosmeticRecipe>> SMITHING_COSMETIC_RECIPE_TYPE =
            RECIPE_TYPES.register("smithing_cosmetic", () -> new RecipeType<SmithingCosmeticRecipe>() {
                @Override
                public String toString() {
                    return "smithing_cosmetic";
                }
            });

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
