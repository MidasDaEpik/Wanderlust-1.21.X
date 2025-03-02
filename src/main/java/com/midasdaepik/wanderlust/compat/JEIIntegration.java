package com.midasdaepik.wanderlust.compat;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.RRItems;
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
        addInfo(pRegistration, RRItems.CUTLASS.get());

        addInfo(pRegistration, RRItems.ELDER_SPINE.get());

        addInfo(pRegistration, RRItems.PIGLIN_WARAXE.get());

        addInfo(pRegistration, RRItems.FIRESTORM_KATANA.get());
        addInfo(pRegistration, RRItems.SEARING_STAFF.get());
        addInfo(pRegistration, RRItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE.get());

        addInfo(pRegistration, RRItems.ANCIENT_TABLET_IMBUEMENT.get());
        addInfo(pRegistration, RRItems.ANCIENT_TABLET_REINFORCEMENT.get());
        addInfo(pRegistration, RRItems.ANCIENT_TABLET_FUSION.get());

        addInfo(pRegistration, RRItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        addInfo(pRegistration, RRItems.WITHERBLADE.get());
        addInfo(pRegistration, RRItems.REFINED_WITHERBLADE.get());

        addInfo(pRegistration, RRItems.MYCORIS.get());
        addInfo(pRegistration, RRItems.PYROSWEEP.get());
        addInfo(pRegistration, RRItems.SOULGORGE.get());
        addInfo(pRegistration, RRItems.WARPTHISTLE.get());

        addInfo(pRegistration, RRItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        addInfo(pRegistration, RRItems.DRAGONBONE.get());
    }

    public static void addInfo(IRecipeRegistration pRegistration, Item pItem) {
        pRegistration.addIngredientInfo(
                new ItemStack(pItem),
                VanillaTypes.ITEM_STACK,
                Component.translatable("hint." + Wanderlust.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(pItem).getPath()));
    }
}
