package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RREntityTypeTagProvider extends EntityTypeTagsProvider {
    public RREntityTypeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper pExistingFileHelper) {
        super(pOutput, pLookupProvider, Wanderlust.MOD_ID, pExistingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(WLEntities.NO_DAMAGE_FIREBALL.get());

        tag(EntityTypeTags.REDIRECTABLE_PROJECTILE)
                .add(WLEntities.NO_DAMAGE_FIREBALL.get());
    }
}
