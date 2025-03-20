package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class WLItemTagProvider extends ItemTagsProvider {
    public WLItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper pExistingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, Wanderlust.MOD_ID, pExistingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(WLTags.CRITLESS_WEAPONS)
                .add(WLItems.PYROSWEEP.get());

        tag(WLTags.TWO_HANDED_WEAPONS)
                .add(WLItems.PIGLIN_WARAXE.get())
                .add(WLItems.PYROSWEEP.get())
                .add(WLItems.OBSIDIAN_BULWARK.get())
                .add(WLItems.SOULGORGE.get());

        tag(WLTags.HIDE_OFFHAND_WHILE_USING_ITEMS)
                .add(WLItems.LYRE_OF_ECHOES.get())
                .add(WLItems.WHISPERWIND.get());

        tag(ItemTags.AXES)
                .add(WLItems.PIGLIN_WARAXE.get());

        tag(ItemTags.BOW_ENCHANTABLE)
                .add(WLItems.WHISPERWIND.get());

        tag(ItemTags.CHEST_ARMOR)
                .add(WLItems.ELDER_CHESTPLATE.get());

        tag(ItemTags.CLUSTER_MAX_HARVESTABLES)
                .add(WLItems.CHARYBDIS.get());

        tag(ItemTags.CROSSBOW_ENCHANTABLE)
                .add(WLItems.DRAGONS_BREATH_ARBALEST.get());

        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(WLItems.SEARING_STAFF.get())
                .add(WLItems.LYRE_OF_ECHOES.get())
                .add(WLItems.HEXED_DICE.get())
                .add(WLItems.WHISPERWIND.get())
                .add(WLItems.DRAGONS_BREATH_ARBALEST.get());

        tag(ItemTags.DYEABLE)
                .add(WLItems.ELDER_CHESTPLATE.get());

        tag(ItemTags.PICKAXES)
                .add(WLItems.CHARYBDIS.get());

        tag(ItemTags.SWORDS)
                .add(WLItems.CUTLASS.get())
                .add(WLItems.CHARYBDIS.get())
                .add(WLItems.FIRESTORM_KATANA.get())
                .add(WLItems.OBSIDIAN_BULWARK.get())
                .add(WLItems.WARPED_RAPIER.get())
                .add(WLItems.WITHERBLADE.get())
                .add(WLItems.MYCORIS.get())
                .add(WLItems.SOULGORGE.get())
                .add(WLItems.WARPTHISTLE.get())
                .add(WLItems.SCYLLA.get())
                .add(WLItems.DRAGONS_RAGE.get());

        tag(ItemTags.TRIM_TEMPLATES)
                .add(WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get())
                .add(WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());
    }
}
