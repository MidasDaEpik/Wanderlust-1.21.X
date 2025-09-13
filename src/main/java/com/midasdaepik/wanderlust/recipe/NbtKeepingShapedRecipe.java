package com.midasdaepik.wanderlust.recipe;

import com.midasdaepik.wanderlust.registries.WLRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class NbtKeepingShapedRecipe implements CraftingRecipe {
    public final ShapedRecipePattern pattern;
    final int nbtKeepingItemId;
    final ItemStack result;
    final String group;
    final CraftingBookCategory category;
    final boolean showNotification;

    public NbtKeepingShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, int nbtKeepingItemId, ItemStack result, boolean showNotification) {
        this.group = group;
        this.category = category;
        this.pattern = pattern;
        this.nbtKeepingItemId = nbtKeepingItemId;
        this.result = result;
        this.showNotification = showNotification;
    }

    public NbtKeepingShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, int nbtKeepingItemId, ItemStack result) {
        this(group, category, pattern, nbtKeepingItemId, result, true);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WLRecipes.NBT_KEEPING_SHAPED_RECIPE_SERIALIZER.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.pattern.ingredients();
    }

    @Override
    public boolean showNotification() {
        return this.showNotification;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= this.pattern.width() && pHeight >= this.pattern.height();
    }

    @Override
    public boolean matches(CraftingInput pInput, Level pLevel) {
        return this.pattern.matches(pInput);
    }

    @Override
    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pRegistries) {
        ItemStack pResult = this.getResultItem(pRegistries).copy();
        return pInput.getItem(this.nbtKeepingItemId).transmuteCopy(pResult.getItem(), pResult.getCount());
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter(p_151277_ -> !p_151277_.isEmpty()).anyMatch(Ingredient::hasNoItems);
    }

    public static class Serializer implements RecipeSerializer<NbtKeepingShapedRecipe> {
        @Override
        public MapCodec<NbtKeepingShapedRecipe> codec() {
            return CODEC;
        }

        public static final MapCodec<NbtKeepingShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340778_ -> p_340778_.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(p_311729_ -> p_311729_.group),
                                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_311732_ -> p_311732_.category),
                                ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.pattern),
                                Codec.INT.fieldOf("keep_nbt_id").forGetter(p_340779_ -> p_340779_.nbtKeepingItemId),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_311730_ -> p_311730_.result),
                                Codec.BOOL.optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(p_311731_ -> p_311731_.showNotification)
                        )
                        .apply(p_340778_, NbtKeepingShapedRecipe::new)
        );

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NbtKeepingShapedRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, NbtKeepingShapedRecipe> STREAM_CODEC = StreamCodec.of(
                NbtKeepingShapedRecipe.Serializer::toNetwork, NbtKeepingShapedRecipe.Serializer::fromNetwork
        );

        private static NbtKeepingShapedRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String s = buffer.readUtf();
            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            int nbtKeepingItemId = buffer.readInt();
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            boolean flag = buffer.readBoolean();
            return new NbtKeepingShapedRecipe(s, craftingbookcategory, shapedrecipepattern, nbtKeepingItemId, itemstack, flag);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, NbtKeepingShapedRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
            buffer.writeInt(recipe.nbtKeepingItemId);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.showNotification);
        }
    }
}
