package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.block.SculkHull;
import com.midasdaepik.wanderlust.block.SculkTangle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WLBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Wanderlust.MOD_ID);

    public static final DeferredBlock<Block> SCULK_TANGLE = registerBlock("sculk_tangle",
            () -> new SculkTangle(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.2F)
                    .randomTicks()
                    .sound(SoundType.SCULK)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isSuffocating(WLBlocks::never)
                    .isViewBlocking(WLBlocks::never)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(WLBlocks::never)
            ));

    public static final DeferredBlock<Block> SCULK_HULL = registerBlock("sculk_hull",
            () -> new SculkHull(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .randomTicks()
                    .sound(SoundType.SCULK)
            ));

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        WLItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
