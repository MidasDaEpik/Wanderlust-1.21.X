package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RRCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Wanderlust.MOD_ID);

    public static final Supplier<CreativeModeTab> REMNANT_RELICS = CREATIVE_MODE_TABS.register(Wanderlust.MOD_ID,
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(RRItems.MOD_ICON.get()))
                    .title(Component.translatable("creativetab.wanderlust.items"))
                    //.backgroundTexture(ResourceLocation.fromNamespaceAndPath(RemnantRelics.MOD_ID, "textures/gui/container/creative_inventory/background.png"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(RRItems.CUTLASS.get());

                        pOutput.accept(RRItems.ELDER_SPINE.get());

                        pOutput.accept(RRItems.CHARYBDIS.get());
                        pOutput.accept(RRItems.ELDER_CHESTPLATE.get());

                        pOutput.accept(RRItems.WHISPERWIND.get());

                        pOutput.accept(RRItems.PIGLIN_WARAXE.get());

                        pOutput.accept(RRItems.FIRESTORM_KATANA.get());
                        pOutput.accept(RRItems.SEARING_STAFF.get());

                        pOutput.accept(RRItems.OBSIDIAN_BULWARK.get());

                        pOutput.accept(RRItems.WARPED_RAPIER.get());

                        pOutput.accept(RRItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE.get());

                        pOutput.accept(RRItems.ANCIENT_TABLET_IMBUEMENT.get());
                        pOutput.accept(RRItems.ANCIENT_TABLET_REINFORCEMENT.get());
                        pOutput.accept(RRItems.ANCIENT_TABLET_FUSION.get());

                        pOutput.accept(RRItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get());

                        pOutput.accept(RRItems.WITHERBLADE.get());
                        pOutput.accept(RRItems.REFINED_WITHERBLADE.get());

                        pOutput.accept(RRItems.MYCORIS.get());
                        pOutput.accept(RRItems.PYROSWEEP.get());
                        pOutput.accept(RRItems.SOULGORGE.get());
                        pOutput.accept(RRItems.WARPTHISTLE.get());

                        pOutput.accept(RRItems.ECHO_GEM.get());

                        pOutput.accept(RRItems.CATALYST_CHALICE.get());
                        pOutput.accept(RRItems.SCYLLA.get());
                        pOutput.accept(RRItems.LYRE_OF_ECHOES.get());

                        pOutput.accept(RRItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());

                        pOutput.accept(RRItems.DRAGONBONE.get());

                        pOutput.accept(RRItems.DRAGONS_RAGE.get());
                        pOutput.accept(RRItems.DRAGONS_BREATH_ARBALEST.get());

                        pOutput.accept(RRItems.HEXED_DICE.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
