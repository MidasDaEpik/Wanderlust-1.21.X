package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.Tags;
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
        tag(WLTags.MARKED_AFFECTS)
                .addTag(DamageTypeTags.IS_PROJECTILE);

        tag(WLTags.NO_COOLDOWN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.SOUL_BURN_NO_COOLDOWN);

        tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
                .add(WLDamageSource.BURN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.SOUL_BURN)
                .add(WLDamageSource.SOUL_BURN_NO_COOLDOWN)
                .add(WLDamageSource.WHIRLPOOL);

        tag(DamageTypeTags.BURNS_ARMOR_STANDS)
                .add(WLDamageSource.BURN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.IMMOLATION)
                .add(WLDamageSource.SOUL_BURN)
                .add(WLDamageSource.SOUL_BURN_NO_COOLDOWN);

        tag(DamageTypeTags.BYPASSES_ARMOR)
                .add(WLDamageSource.IMMOLATION);

        tag(DamageTypeTags.BYPASSES_COOLDOWN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.MAGIC)
                .add(WLDamageSource.SONIC_BOOM)
                .add(WLDamageSource.SOUL_BURN_NO_COOLDOWN);

        tag(DamageTypeTags.BYPASSES_SHIELD)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.SONIC_BOOM)
                .add(WLDamageSource.WHIRLPOOL);

        tag(DamageTypeTags.NO_IMPACT)
                .add(WLDamageSource.ECHO);

        tag(DamageTypeTags.NO_KNOCKBACK)
                .add(WLDamageSource.BURN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.IMMOLATION)
                .add(WLDamageSource.MAGIC)
                .add(WLDamageSource.SOUL_BURN)
                .add(WLDamageSource.SOUL_BURN_NO_COOLDOWN)
                .add(WLDamageSource.WHIRLPOOL);

        tag(DamageTypeTags.PANIC_CAUSES)
                .add(WLDamageSource.BURN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.IMMOLATION)
                .add(WLDamageSource.MAGIC)
                .add(WLDamageSource.SONIC_BOOM)
                .add(WLDamageSource.SOUL_BURN)
                .add(WLDamageSource.SOUL_BURN_NO_COOLDOWN)
                .add(WLDamageSource.WHIRLPOOL);

        tag(DamageTypeTags.WITCH_RESISTANT_TO)
                .add(WLDamageSource.IMMOLATION);

        //Universal Tags
        tag(Tags.DamageTypes.IS_PHYSICAL)
                .add(WLDamageSource.BURN)
                .add(WLDamageSource.BURN_NO_COOLDOWN)
                .add(WLDamageSource.SOUL_BURN)
                .add(WLDamageSource.SOUL_BURN_NO_COOLDOWN);

        tag(Tags.DamageTypes.IS_MAGIC)
                .add(WLDamageSource.ECHO)
                .add(WLDamageSource.IMMOLATION)
                .add(WLDamageSource.MAGIC)
                .add(WLDamageSource.SONIC_BOOM)
                .add(WLDamageSource.WHIRLPOOL);
    }
}