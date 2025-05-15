package com.midasdaepik.wanderlust.compat;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration pRegistration) {
        addInfo(pRegistration, WLItems.DAGGER.get());

        addInfo(pRegistration, WLItems.CUTLASS.get());

        addInfo(pRegistration, WLItems.ELDER_SPINE.get());

        addInfo(pRegistration, WLItems.PIGLIN_WARAXE.get());

        addInfo(pRegistration, WLItems.ANCIENT_TABLET_IMBUEMENT.get());
        addInfo(pRegistration, WLItems.ANCIENT_TABLET_REINFORCEMENT.get());
        addInfo(pRegistration, WLItems.ANCIENT_TABLET_FUSION.get());

        addInfo(pRegistration, WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        addInfo(pRegistration, WLItems.WITHERBLADE.get());
        addInfo(pRegistration, WLItems.REFINED_WITHERBLADE.get());

        addInfo(pRegistration, WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        addInfo(pRegistration, WLItems.DRAGONBONE.get());
    }

    public static void addInfo(IRecipeRegistration pRegistration, Item pItem) {
        pRegistration.addIngredientInfo(
                new ItemStack(pItem),
                VanillaTypes.ITEM_STACK,
                Component.translatable("hint." + Wanderlust.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(pItem).getPath()));
    }
}
