package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLEnchantments;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class WLEnchantmentTagProvider extends EnchantmentTagsProvider {
    public WLEnchantmentTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper pExistingFileHelper) {
        super(pOutput, pLookupProvider, Wanderlust.MOD_ID, pExistingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(WLTags.MASK_EXCLUSIVE)
                .add(WLEnchantments.BOLSTERED)
                .add(WLEnchantments.CONCEALMENT)
                .add(WLEnchantments.NAMELESS);

        tag(EnchantmentTags.NON_TREASURE)
                .add(WLEnchantments.BOLSTERED)
                .add(WLEnchantments.CONCEALMENT)
                .add(WLEnchantments.NAMELESS);

        tag(EnchantmentTags.TOOLTIP_ORDER)
                .add(WLEnchantments.BOLSTERED)
                .add(WLEnchantments.CONCEALMENT)
                .add(WLEnchantments.NAMELESS);
    }
}
