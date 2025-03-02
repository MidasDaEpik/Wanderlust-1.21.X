package com.midasdaepik.wanderlust.compat;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.RRItems;
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
        addInfo(RRItems.CUTLASS.get());

        addInfo(RRItems.ELDER_SPINE.get());

        addInfo(RRItems.PIGLIN_WARAXE.get());

        addInfo(RRItems.FIRESTORM_KATANA.get());
        addInfo(RRItems.SEARING_STAFF.get());
        addInfo(RRItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE.get());

        addInfo(RRItems.ANCIENT_TABLET_IMBUEMENT.get());
        addInfo(RRItems.ANCIENT_TABLET_REINFORCEMENT.get());
        addInfo(RRItems.ANCIENT_TABLET_FUSION.get());

        addInfo(RRItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        addInfo(RRItems.WITHERBLADE.get());
        addInfo(RRItems.REFINED_WITHERBLADE.get());

        addInfo(RRItems.MYCORIS.get());
        addInfo(RRItems.PYROSWEEP.get());
        addInfo(RRItems.SOULGORGE.get());
        addInfo(RRItems.WARPTHISTLE.get());

        addInfo(RRItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        addInfo(RRItems.DRAGONBONE.get());
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
