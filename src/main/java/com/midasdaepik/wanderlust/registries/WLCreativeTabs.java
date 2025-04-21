package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WLCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Wanderlust.MOD_ID);

    public static final Supplier<CreativeModeTab> REMNANT_RELICS = CREATIVE_MODE_TABS.register(Wanderlust.MOD_ID,
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(WLItems.MOD_ICON.get()))
                    .title(Component.translatable("creativetab.wanderlust.items"))
                    //.backgroundTexture(ResourceLocation.fromNamespaceAndPath(RemnantRelics.MOD_ID, "textures/gui/container/creative_inventory/background.png"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(WLItems.CUTLASS.get());

                        pOutput.accept(WLItems.ELDER_SPINE.get());

                        pOutput.accept(WLItems.CHARYBDIS.get());
                        pOutput.accept(WLItems.ELDER_CHESTPLATE.get());

                        pOutput.accept(WLItems.WHISPERWIND.get());

                        pOutput.accept(WLItems.PIGLIN_WARAXE.get());

                        pOutput.accept(WLItems.FIRESTORM_KATANA.get());
                        pOutput.accept(WLItems.SEARING_STAFF.get());

                        pOutput.accept(WLItems.OBSIDIAN_BULWARK.get());

                        pOutput.accept(WLItems.WARPED_RAPIER.get());

                        pOutput.accept(WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE.get());

                        pOutput.accept(WLItems.ANCIENT_TABLET_IMBUEMENT.get());
                        pOutput.accept(WLItems.ANCIENT_TABLET_REINFORCEMENT.get());
                        pOutput.accept(WLItems.ANCIENT_TABLET_FUSION.get());

                        pOutput.accept(WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get());

                        pOutput.accept(WLItems.WITHERBLADE.get());
                        pOutput.accept(WLItems.REFINED_WITHERBLADE.get());

                        pOutput.accept(WLItems.MYCORIS.get());
                        pOutput.accept(WLItems.PYROSWEEP.get());
                        pOutput.accept(WLItems.SOULGORGE.get());
                        pOutput.accept(WLItems.WARPTHISTLE.get());

                        pOutput.accept(WLItems.ECHO_GEM.get());

                        pOutput.accept(WLItems.CATALYST_CHALICE.get());
                        pOutput.accept(WLItems.SCYLLA.get());
                        pOutput.accept(WLItems.LYRE_OF_ECHOES.get());

                        pOutput.accept(WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());

                        pOutput.accept(WLItems.DRAGONBONE.get());

                        pOutput.accept(WLItems.DRAGONS_RAGE.get());
                        pOutput.accept(WLItems.DRAGONS_BREATH_ARBALEST.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
