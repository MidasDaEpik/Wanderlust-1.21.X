package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class WLBlockTagProvider extends BlockTagsProvider {
    public WLBlockTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper pExistingFileHelper) {
        super(pOutput, pLookupProvider, Wanderlust.MOD_ID, pExistingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
    }
}
