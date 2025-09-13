package com.midasdaepik.wanderlust.recipe;

import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;

public class SmithingCosmeticRecipe implements SmithingRecipe {
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;

    final String cosmeticType;
    final int cosmeticMaterial;

    public SmithingCosmeticRecipe(Ingredient template, Ingredient base, Ingredient addition, String cosmeticType, int cosmeticMaterial) {
        this.template = template;
        this.base = base;
        this.addition = addition;

        this.cosmeticType = cosmeticType;
        this.cosmeticMaterial = cosmeticMaterial;
    }

    public boolean matches(SmithingRecipeInput input, Level level) {
        return this.template.test(input.template()) && this.base.test(input.base()) && this.addition.test(input.addition());
    }

    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack pItemstack = input.base().copyWithCount(1);
        pItemstack.set(WLDataComponents.COSMETIC_TYPE, this.cosmeticType);
        pItemstack.set(WLDataComponents.COSMETIC_MATERIAL, this.cosmeticMaterial);

        return pItemstack;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        ItemStack pItemstack = new ItemStack(Items.IRON_HELMET);
        pItemstack.set(WLDataComponents.COSMETIC_TYPE, this.cosmeticType);
        pItemstack.set(WLDataComponents.COSMETIC_MATERIAL, this.cosmeticMaterial);

        return pItemstack;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return this.template.test(stack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return this.base.test(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WLRecipes.SMITHING_COSMETIC_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::hasNoItems);
    }

    public static class Serializer implements RecipeSerializer<SmithingCosmeticRecipe> {
        private static final MapCodec<SmithingCosmeticRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_301227_ -> p_301227_.group(
                                Ingredient.CODEC.fieldOf("template").forGetter(p_301070_ -> p_301070_.template),
                                Ingredient.CODEC.fieldOf("base").forGetter(p_300969_ -> p_300969_.base),
                                Ingredient.CODEC.fieldOf("addition").forGetter(p_300977_ -> p_300977_.addition),
                                Codec.STRING.optionalFieldOf("cosmetic_type", "").forGetter(p_311731_ -> p_311731_.cosmeticType),
                                Codec.INT.fieldOf("cosmetic_material").forGetter(p_340730_ -> p_340730_.cosmeticMaterial)
                        )
                        .apply(p_301227_, SmithingCosmeticRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, SmithingCosmeticRecipe> STREAM_CODEC = StreamCodec.of(
                SmithingCosmeticRecipe.Serializer::toNetwork, SmithingCosmeticRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SmithingCosmeticRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmithingCosmeticRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SmithingCosmeticRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            String type = buffer.readUtf();
            int material = buffer.readInt();
            return new SmithingCosmeticRecipe(ingredient, ingredient1, ingredient2, type, material);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SmithingCosmeticRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.template);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.base);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.addition);
            buffer.writeUtf(recipe.cosmeticType);
            buffer.writeInt(recipe.cosmeticMaterial);
        }
    }
}
