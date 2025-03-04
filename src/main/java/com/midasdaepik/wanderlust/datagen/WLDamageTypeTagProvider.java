package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLDamageSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class WLDamageTypeTagProvider extends DamageTypeTagsProvider {
    static String MOD_ID = Wanderlust.MOD_ID;

    public WLDamageTypeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper pExistingFileHelper) {
        super(pOutput, pLookupProvider, MOD_ID, pExistingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
                .add(WLDamageSource.BURN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.WHIRLPOOL);

        tag(DamageTypeTags.BYPASSES_COOLDOWN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.MAGIC)
                .add(WLDamageSource.SONIC_BOOM);

        tag(DamageTypeTags.BYPASSES_SHIELD)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.SONIC_BOOM);

        tag(DamageTypeTags.NO_IMPACT)
                .add(WLDamageSource.ECHO);

        tag(DamageTypeTags.NO_KNOCKBACK)
                .add(WLDamageSource.BURN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.MAGIC)
                .add(WLDamageSource.WHIRLPOOL);
    }
}
