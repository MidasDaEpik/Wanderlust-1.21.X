package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Wanderlust.MOD_ID)
public class WLDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent pEvent) {
        DataGenerator pGenerator = pEvent.getGenerator();
        PackOutput pPackOutput = pGenerator.getPackOutput();
        ExistingFileHelper pExistingFileHelper = pEvent.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> pLookupProvider = pEvent.getLookupProvider();

        pGenerator.addProvider(pEvent.includeServer(), new WLRecipeProvider(pPackOutput, pLookupProvider));

        BlockTagsProvider pBlockTagsProvider = new WLBlockTagProvider(pPackOutput, pLookupProvider, pExistingFileHelper);
        pGenerator.addProvider(pEvent.includeServer(), pBlockTagsProvider);
        pGenerator.addProvider(pEvent.includeServer(), new WLItemTagProvider(pPackOutput, pLookupProvider, pBlockTagsProvider.contentsGetter(), pExistingFileHelper));
        pGenerator.addProvider(pEvent.includeServer(), new WLDamageTypeTagProvider(pPackOutput, pLookupProvider, pExistingFileHelper));
        pGenerator.addProvider(pEvent.includeServer(), new WLEnchantmentTagProvider(pPackOutput, pLookupProvider, pExistingFileHelper));
        pGenerator.addProvider(pEvent.includeServer(), new WLEntityTypeTagProvider(pPackOutput, pLookupProvider, pExistingFileHelper));

        pGenerator.addProvider(pEvent.includeClient(), new WLItemModelProvider(pPackOutput, pExistingFileHelper));
    }
}
