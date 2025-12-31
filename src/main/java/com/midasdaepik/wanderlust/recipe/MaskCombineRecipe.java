package com.midasdaepik.wanderlust.recipe;

import com.midasdaepik.wanderlust.misc.MaskContents;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLRecipes;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MaskCombineRecipe extends CustomRecipe {
    public MaskCombineRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WLRecipes.MASK_COMBINE_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean matches(CraftingInput pInput, Level pLevel) {
        ItemStack pItemStackArmor = ItemStack.EMPTY;
        ItemStack pItemStackMask = ItemStack.EMPTY;

        for(int i = 0; i < pInput.size(); ++i) {
            ItemStack pItemStackIterated = pInput.getItem(i);
            if (!pItemStackIterated.isEmpty()) {
                if (pItemStackIterated.is(WLTags.MASK_ATTACHABLES)) {
                    if (!pItemStackArmor.isEmpty()) {
                        return false;
                    }
                    pItemStackArmor = pItemStackIterated.copy();

                } else if (pItemStackIterated.is(WLItems.MASK)) {
                    if (!pItemStackMask.isEmpty()) {
                        return false;
                    }
                    pItemStackMask = pItemStackIterated.copy();

                } else {
                    return false;
                }
            }
        }

        if (!pItemStackArmor.isEmpty() && !pItemStackMask.isEmpty()) {
            MaskContents pMaskContents = pItemStackArmor.get(WLDataComponents.MASK_SLOT);
            if (pMaskContents == null || pMaskContents.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pProvider) {
        ItemStack pItemStackArmor = ItemStack.EMPTY;
        ItemStack pItemStackMask = ItemStack.EMPTY;

        for(int i = 0; i < pInput.size(); ++i) {
            ItemStack pItemStackIterated = pInput.getItem(i);
            if (!pItemStackIterated.isEmpty()) {
                if (pItemStackIterated.is(WLTags.MASK_ATTACHABLES)) {
                    if (!pItemStackArmor.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    pItemStackArmor = pItemStackIterated.copy();

                } else if (pItemStackIterated.is(WLItems.MASK)) {
                    if (!pItemStackMask.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    pItemStackMask = pItemStackIterated.copy();

                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (!pItemStackArmor.isEmpty() && !pItemStackMask.isEmpty()) {
            MaskContents pMaskContents = pItemStackArmor.get(WLDataComponents.MASK_SLOT);
            if (pMaskContents == null || pMaskContents.isEmpty()) {
                pItemStackArmor.set(WLDataComponents.MASK_SLOT, new MaskContents(pItemStackMask));
                return pItemStackArmor;
            } else {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }
}
