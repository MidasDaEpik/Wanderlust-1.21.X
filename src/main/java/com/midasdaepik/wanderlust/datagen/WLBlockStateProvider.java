package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class WLBlockStateProvider extends BlockStateProvider {
    public WLBlockStateProvider(PackOutput pOutput, ExistingFileHelper pExFileHelper) {
        super(pOutput, Wanderlust.MOD_ID, pExFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItemWithRenderType(WLBlocks.SCULK_TANGLE, "minecraft:block/leaves", "minecraft:cutout");
    }

    private void blockWithItem(DeferredBlock<?> pBlock) {
        simpleBlockWithItem(pBlock.get(), cubeAll(pBlock.get()));
    }

    private void blockWithItemWithRenderType(DeferredBlock<?> pBlock, String pParent, String pRenderType) {
        simpleBlockWithItem(pBlock.get(), models().singleTexture(
                BuiltInRegistries.BLOCK.getKey(pBlock.get()).getPath(),
                ResourceLocation.parse(pParent),
                "all",
                blockTexture(pBlock.get())
        ).renderType(pRenderType));
    }
}
