package com.midasdaepik.wanderlust.recipe;

import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.MaskContents;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLRecipes;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CatalystCrystalUpgradeRecipe extends CustomRecipe {
    public CatalystCrystalUpgradeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WLRecipes.CATALYST_CRYSTAL_UPGRADE_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean matches(CraftingInput pInput, Level pLevel) {
        ItemStack pItemStackCatalystCrystal = ItemStack.EMPTY;
        int pEchoShardCount = 0;

        for(int i = 0; i < pInput.size(); ++i) {
            ItemStack pItemStackIterated = pInput.getItem(i);
            if (!pItemStackIterated.isEmpty()) {
                if (pItemStackIterated.is(WLItems.CATALYST_CRYSTAL)) {
                    if (pItemStackCatalystCrystal.isEmpty()) {
                        pItemStackCatalystCrystal = pItemStackIterated.copy();
                    } else {
                        pEchoShardCount += pItemStackIterated.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM, 0.0).intValue() + 6;
                    }

                } else if (pItemStackIterated.is(Items.ECHO_SHARD)) {
                    pEchoShardCount += 1;

                } else {
                    return false;
                }
            }
        }

        if (!pItemStackCatalystCrystal.isEmpty() && pEchoShardCount > 0) {
            if (pItemStackCatalystCrystal.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM, 0.0).intValue() + pEchoShardCount <=
                    WLAttributeConfig.CONFIG.ItemCatalystCrystalMaxSoulsIncreasingItemCap.get()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pProvider) {
        ItemStack pItemStackCatalystCrystal = ItemStack.EMPTY;
        int pEchoShardCount = 0;
        int pSoulCount = 0;

        for(int i = 0; i < pInput.size(); ++i) {
            ItemStack pItemStackIterated = pInput.getItem(i);
            if (!pItemStackIterated.isEmpty()) {
                if (pItemStackIterated.is(WLItems.CATALYST_CRYSTAL)) {
                    if (pItemStackCatalystCrystal.isEmpty()) {
                        pItemStackCatalystCrystal = pItemStackIterated.copy();
                    } else {
                        pEchoShardCount += pItemStackIterated.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM, 0.0).intValue() + 6;
                        pSoulCount += pItemStackIterated.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue();
                    }

                } else if (pItemStackIterated.is(Items.ECHO_SHARD)) {
                    pEchoShardCount += 1;

                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (!pItemStackCatalystCrystal.isEmpty() && pEchoShardCount > 0) {
            if (pItemStackCatalystCrystal.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM, 0.0).intValue() + pEchoShardCount <=
                    WLAttributeConfig.CONFIG.ItemCatalystCrystalMaxSoulsIncreasingItemCap.get()) {
                pItemStackCatalystCrystal.set(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM,
                        pItemStackCatalystCrystal.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM, 0.0).intValue() + pEchoShardCount);
                if (pSoulCount > 0) {
                    pItemStackCatalystCrystal.set(WLDataComponents.EXPERIENCE,
                            pItemStackCatalystCrystal.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue() + pSoulCount);
                }
                return pItemStackCatalystCrystal;
            } else {
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(WLItems.CATALYST_CRYSTAL.get());
    }
}
