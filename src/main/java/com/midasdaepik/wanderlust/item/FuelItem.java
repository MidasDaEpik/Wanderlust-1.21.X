package com.midasdaepik.wanderlust.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class FuelItem extends Item {
    private int BurnTime = 0;

    public FuelItem(Properties pProperties, int BurnTime) {
        super(pProperties);
        this.BurnTime = BurnTime;
    }

    @Override
    public int getBurnTime(ItemStack pItemStack, @Nullable RecipeType<?> recipeType) {
        return this.BurnTime;
    }
}
