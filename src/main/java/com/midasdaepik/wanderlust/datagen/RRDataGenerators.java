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

@EventBusSubscriber(modid = Wanderlust.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class RRDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent pEvent) {
        DataGenerator pGenerator = pEvent.getGenerator();
        PackOutput pPackOutput = pGenerator.getPackOutput();
        ExistingFileHelper pExistingFileHelper = pEvent.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> pLookupProvider = pEvent.getLookupProvider();

        pGenerator.addProvider(pEvent.includeServer(), new RRRecipeProvider(pPackOutput, pLookupProvider));

        BlockTagsProvider pBlockTagsProvider = new RRBlockTagProvider(pPackOutput, pLookupProvider, pExistingFileHelper);
        pGenerator.addProvider(pEvent.includeServer(), pBlockTagsProvider);
        pGenerator.addProvider(pEvent.includeServer(), new RRItemTagProvider(pPackOutput, pLookupProvider, pBlockTagsProvider.contentsGetter(), pExistingFileHelper));
        pGenerator.addProvider(pEvent.includeServer(), new RRDamageTypeTagProvider(pPackOutput, pLookupProvider, pExistingFileHelper));
        pGenerator.addProvider(pEvent.includeServer(), new RREntityTypeTagProvider(pPackOutput, pLookupProvider, pExistingFileHelper));

        pGenerator.addProvider(pEvent.includeClient(), new RRItemModelProvider(pPackOutput, pExistingFileHelper));
    }
}
