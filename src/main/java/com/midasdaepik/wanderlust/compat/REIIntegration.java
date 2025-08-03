package com.midasdaepik.wanderlust.compat;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

@REIPluginClient
public class REIIntegration implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry pDisplayRegistry) {
        addInfo(WLItems.DAGGER.get());

        addInfo(WLItems.CUTLASS.get());

        addInfo(WLItems.ELDER_SPINE.get());

        addInfo(WLItems.PIGLIN_WARAXE.get());

        addInfo(WLItems.ANCIENT_TABLET_IMBUEMENT.get());
        addInfo(WLItems.ANCIENT_TABLET_REINFORCEMENT.get());
        addInfo(WLItems.ANCIENT_TABLET_FUSION.get());

        addInfo(WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        addInfo(WLItems.WITHERBLADE.get());

        addInfo(WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        addInfo(WLItems.DRAGONBONE.get());
    }

    private static void addInfo(Item pItem) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(pItem),
                Component.translatable(BuiltInRegistries.ITEM.getKey(pItem).toString()),
                (text) -> {
                    text.add(Component.translatable("hint." + Wanderlust.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(pItem).getPath()));
                    return text;
                });
    }
}
