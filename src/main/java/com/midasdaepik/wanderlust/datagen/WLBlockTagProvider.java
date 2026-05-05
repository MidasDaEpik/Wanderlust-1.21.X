package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLBlocks;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
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
        tag(WLTags.SCULK_BLOCKS)
                .add(Blocks.CALIBRATED_SCULK_SENSOR)
                .add(Blocks.SCULK_CATALYST)
                .add(Blocks.SCULK_SENSOR)
                .add(Blocks.SCULK_SHRIEKER)
                .add(Blocks.SCULK_VEIN)
                .addTag(WLTags.SCULK_SURFACE_BLOCKS);
        tag(WLTags.SCULK_SURFACE_BLOCKS)
                .add(Blocks.SCULK)
                .add(WLBlocks.SCULK_HULL.get())
                .add(WLBlocks.SCULK_TANGLE.get());
        tag(WLTags.SCULK_CAN_BE_REPLACED)
                .addTag(BlockTags.REPLACEABLE)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.SAPLINGS)
                .add(Blocks.BROWN_MUSHROOM)
                .add(Blocks.RED_MUSHROOM)
                .add(Blocks.CRIMSON_FUNGUS)
                .add(Blocks.WARPED_FUNGUS)
                .add(Blocks.MOSS_CARPET)
                .addOptional(ResourceLocation.parse("wetland_whimsy:aria_spores"))
                .addOptional(ResourceLocation.parse("wetland_whimsy:aria_mushroom"))
                .addOptional(ResourceLocation.parse("wetland_whimsy:fellcap_mushroom"));
        tag(WLTags.SCULK_HULL_REPLACABLE)
                .addTag(BlockTags.LOGS)
                .add(Blocks.MUSHROOM_STEM);
        tag(WLTags.SCULK_TANGLE_REPLACABLE)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.WART_BLOCKS)
                .add(Blocks.RED_MUSHROOM_BLOCK)
                .add(Blocks.BROWN_MUSHROOM_BLOCK)
                .addOptional(ResourceLocation.parse("wetland_whimsy:aria_mushroom_block"))
                .addOptional(ResourceLocation.parse("wetland_whimsy:fellcap_mushroom_block"));

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(WLBlocks.SCULK_HULL.get());
        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(WLBlocks.SCULK_HULL.get())
                .add(WLBlocks.SCULK_TANGLE.get());
        tag(BlockTags.SCULK_REPLACEABLE)
                .addTag(WLTags.SCULK_HULL_REPLACABLE)
                .addTag(WLTags.SCULK_TANGLE_REPLACABLE);
    }
}
