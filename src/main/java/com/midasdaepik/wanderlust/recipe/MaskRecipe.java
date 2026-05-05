package com.midasdaepik.wanderlust.recipe;

import com.midasdaepik.wanderlust.item.Mask;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLItems;
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

public class MaskRecipe implements CraftingRecipe {
    public final ShapedRecipePattern pattern;
    final String maskName;
    final String group;
    final CraftingBookCategory category;
    final boolean showNotification;

    public MaskRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, String maskName, boolean showNotification) {
        this.group = group;
        this.category = category;
        this.pattern = pattern;
        this.maskName = maskName;
        this.showNotification = showNotification;
    }

    public MaskRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, String maskName) {
        this(group, category, pattern, maskName, true);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WLRecipes.MASK_RECIPE_SERIALIZER.get();
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
        ItemStack pItemStack = new ItemStack(WLItems.MASK.get());
        pItemStack.set(WLDataComponents.MASK_TYPE, Mask.MaskType.fromName(this.maskName));
        return pItemStack;
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
        int pMaskSlot = -1;
        for (int pNum = 0; pNum <= pInput.size() - 1; pNum++) {
            if (pInput.getItem(pNum).getItem() == WLItems.MASK.get()) {
                pMaskSlot = pNum;
            }
        }
        if (pMaskSlot != -1) {
            pResult = pInput.getItem(pMaskSlot).copy();
            pResult.set(WLDataComponents.MASK_TYPE, Mask.MaskType.fromName(this.maskName));
        }
        return pResult;
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

    public static class Serializer implements RecipeSerializer<MaskRecipe> {
        @Override
        public MapCodec<MaskRecipe> codec() {
            return CODEC;
        }

        public static final MapCodec<MaskRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340778_ -> p_340778_.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(p_311729_ -> p_311729_.group),
                                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_311732_ -> p_311732_.category),
                                ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.pattern),
                                Codec.STRING.fieldOf("maskName").forGetter(p_340779_ -> p_340779_.maskName),
                                Codec.BOOL.optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(p_311731_ -> p_311731_.showNotification)
                        )
                        .apply(p_340778_, MaskRecipe::new)
        );

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MaskRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, MaskRecipe> STREAM_CODEC = StreamCodec.of(
                MaskRecipe.Serializer::toNetwork, MaskRecipe.Serializer::fromNetwork
        );

        private static MaskRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String s = buffer.readUtf();
            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            String maskName = buffer.readUtf();
            boolean flag = buffer.readBoolean();
            return new MaskRecipe(s, craftingbookcategory, shapedrecipepattern, maskName, flag);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, MaskRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
            buffer.writeUtf(recipe.maskName);
            buffer.writeBoolean(recipe.showNotification);
        }
    }
}
