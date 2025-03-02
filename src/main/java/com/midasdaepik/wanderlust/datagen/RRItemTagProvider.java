package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.RRItems;
import com.midasdaepik.wanderlust.registries.RRTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RRItemTagProvider extends ItemTagsProvider {
    public RRItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper pExistingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, Wanderlust.MOD_ID, pExistingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(RRTags.DUAL_WIELDED_WEAPONS)
                .add(RRItems.PIGLIN_WARAXE.get())
                .add(RRItems.PYROSWEEP.get())
                .add(RRItems.OBSIDIAN_BULWARK.get())
                .add(RRItems.SOULGORGE.get());

        tag(RRTags.HIDE_OFFHAND_WHILE_USING_ITEMS)
                .add(RRItems.LYRE_OF_ECHOES.get())
                .add(RRItems.WHISPERWIND.get());

        tag(ItemTags.BOW_ENCHANTABLE)
                .add(RRItems.WHISPERWIND.get());

        tag(ItemTags.CROSSBOW_ENCHANTABLE)
                .add(RRItems.DRAGONS_BREATH_ARBALEST.get());

        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(RRItems.SEARING_STAFF.get())
                .add(RRItems.LYRE_OF_ECHOES.get())
                .add(RRItems.HEXED_DICE.get())
                .add(RRItems.WHISPERWIND.get())
                .add(RRItems.DRAGONS_BREATH_ARBALEST.get());

        tag(ItemTags.AXES)
                .add(RRItems.PIGLIN_WARAXE.get());

        tag(ItemTags.CHEST_ARMOR)
                .add(RRItems.ELDER_CHESTPLATE.get());

        tag(ItemTags.DYEABLE)
                .add(RRItems.ELDER_CHESTPLATE.get());

        tag(ItemTags.SWORDS)
                .add(RRItems.CUTLASS.get())
                .add(RRItems.CHARYBDIS.get())
                .add(RRItems.FIRESTORM_KATANA.get())
                .add(RRItems.OBSIDIAN_BULWARK.get())
                .add(RRItems.WARPED_RAPIER.get())
                .add(RRItems.WITHERBLADE.get())
                .add(RRItems.MYCORIS.get())
                .add(RRItems.SOULGORGE.get())
                .add(RRItems.WARPTHISTLE.get())
                .add(RRItems.SCYLLA.get())
                .add(RRItems.DRAGONS_RAGE.get());

        tag(ItemTags.TRIM_TEMPLATES)
                .add(RRItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get())
                .add(RRItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());
    }
}
