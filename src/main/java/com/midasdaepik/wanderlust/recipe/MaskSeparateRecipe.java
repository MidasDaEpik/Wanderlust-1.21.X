package com.midasdaepik.wanderlust.recipe;

import com.midasdaepik.wanderlust.misc.MaskContents;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MaskSeparateRecipe extends CustomRecipe {
    public MaskSeparateRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WLRecipes.MASK_SEPARATE_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean matches(CraftingInput pInput, Level pLevel) {
        ItemStack pItemStackArmor = ItemStack.EMPTY;

        for(int i = 0; i < pInput.size(); ++i) {
            ItemStack pItemStackIterated = pInput.getItem(i);
            if (!pItemStackIterated.isEmpty()) {
                if (pItemStackIterated.get(WLDataComponents.MASK_SLOT) != null) {
                    if (!pItemStackArmor.isEmpty()) {
                        return false;
                    }
                    pItemStackArmor = pItemStackIterated.copy();

                } else {
                    return false;
                }
            }
        }

        if (!pItemStackArmor.isEmpty()) {
            if (pItemStackArmor.get(WLDataComponents.MASK_SLOT) != null) {
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

        for(int i = 0; i < pInput.size(); ++i) {
            ItemStack pItemStackIterated = pInput.getItem(i);
            if (!pItemStackIterated.isEmpty()) {
                if (pItemStackIterated.get(WLDataComponents.MASK_SLOT) != null) {
                    if (!pItemStackArmor.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    pItemStackArmor = pItemStackIterated.copy();

                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (!pItemStackArmor.isEmpty()) {
            if (pItemStackArmor.get(WLDataComponents.MASK_SLOT) != null) {
                pItemStackArmor.remove(WLDataComponents.MASK_SLOT);
                return pItemStackArmor;
            } else {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput pInput) {
        NonNullList<ItemStack> pList = NonNullList.withSize(pInput.size(), ItemStack.EMPTY);

        for(int i = 0; i < pList.size(); ++i) {
            ItemStack pItemStackIterated = pInput.getItem(i);
            if (pItemStackIterated.hasCraftingRemainingItem()) {
                pList.set(i, pItemStackIterated.getCraftingRemainingItem());
            }

            MaskContents pMaskContents = pItemStackIterated.get(WLDataComponents.MASK_SLOT);
            if (pMaskContents != null) {
                pList.set(i, pMaskContents.getMask());
            }
        }

        return pList;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight == 1;
    }
}
