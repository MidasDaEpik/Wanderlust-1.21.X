package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.block.SculkHull;
import com.midasdaepik.wanderlust.block.SculkTangle;
import com.midasdaepik.wanderlust.registries.WLBlocks;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkVeinBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SculkVeinBlock.class)
public class SculkVeinBlockMixin {
    @WrapOperation(method = "onDischarged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1))
    private boolean onDischarged(BlockState pBlockState, Block pBlock, Operation<Boolean> pOriginal) {
        return pOriginal.call(pBlockState, pBlock) || pBlockState.is(WLTags.SCULK_SURFACE_BLOCKS);
    }

    @WrapOperation(method = "attemptPlaceSculk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean attemptPlaceSculk(LevelAccessor pLevel, BlockPos pBlockPos, BlockState pBlockState, int pFlags, Operation<Boolean> pOriginal) {
        BlockState pBlockStateQuery = pLevel.getBlockState(pBlockPos);
        if (pBlockStateQuery.is(WLTags.SCULK_HULL_REPLACABLE)) {
            boolean pResult = pOriginal.call(pLevel, pBlockPos, SculkHull.getDefaultSpreadingBlockState(pLevel, pBlockPos), pFlags);
            if (pResult) {
                SculkHull.clearSculkVein(pLevel, pBlockPos);
                if (RandomSource.create().nextInt(4) >= 1) {
                    pLevel.scheduleTick(pBlockPos, WLBlocks.SCULK_HULL.get(), RandomSource.create().nextInt(20) + 1);
                }
            }

            convertNearbyIntoTangle(pLevel, pBlockPos, pFlags);

            return pResult;

        } else if (pBlockStateQuery.is(WLTags.SCULK_TANGLE_REPLACABLE)) {
            boolean pResult = pOriginal.call(pLevel, pBlockPos, SculkTangle.getDefaultSpreadingBlockState(pLevel, pBlockPos), pFlags);
            if (pResult && RandomSource.create().nextInt(2) == 1) {
                pLevel.scheduleTick(pBlockPos, WLBlocks.SCULK_TANGLE.get(), RandomSource.create().nextInt(20) + 1);
            }

            convertNearbyIntoTangle(pLevel, pBlockPos, pFlags);

            return pResult;
        } else {
            return pOriginal.call(pLevel, pBlockPos, pBlockState, pFlags);
        }
    }

    @Unique
    private void convertNearbyIntoTangle(LevelAccessor pLevel, BlockPos pBlockPos, int pFlags) {
        int pRadius = 2;
        int pRadiusSquared = pRadius * pRadius;

        BlockPos.betweenClosedStream(pBlockPos.offset(pRadius, pRadius, pRadius), pBlockPos.offset(-pRadius, -pRadius, -pRadius))
                .forEach(pBlockPosIteration -> {
            int dx = pBlockPosIteration.getX() - pBlockPos.getX();
            int dy = pBlockPosIteration.getY() - pBlockPos.getY();
            int dz = pBlockPosIteration.getZ() - pBlockPos.getZ();

            if (dx * dx + dy * dy + dz * dz <= pRadiusSquared) {
                BlockState pBlockState = pLevel.getBlockState(pBlockPosIteration);
                if (pBlockState.is(WLTags.SCULK_TANGLE_REPLACABLE) && RandomSource.create().nextInt(2) == 1) {
                    pLevel.setBlock(pBlockPosIteration, SculkTangle.getDefaultSpreadingBlockState(pLevel, pBlockPosIteration), pFlags);

                    if (RandomSource.create().nextInt(2) == 1) {
                        pLevel.scheduleTick(pBlockPosIteration, WLBlocks.SCULK_TANGLE.get(), RandomSource.create().nextInt(40) + 1);
                    }
                }
            }
        });
    }

    @Mixin(targets = "net.minecraft.world.level.block.SculkVeinBlock$SculkVeinSpreaderConfig")
    public static class SculkVeinSpreaderMixin {
        @WrapOperation(method = "stateCanBeReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
        private boolean stateCanBeReplaced(BlockState pBlockState, Block pBlock, Operation<Boolean> pOriginal) {
            return pOriginal.call(pBlockState, pBlock) || pBlockState.is(WLTags.SCULK_SURFACE_BLOCKS);
        }

        @WrapOperation(method = "stateCanBeReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced()Z"))
        private boolean stateCanBeReplaced2(BlockState pBlockState, Operation<Boolean> pOriginal) {
            return pOriginal.call(pBlockState) || pBlockState.is(WLTags.SCULK_CAN_BE_REPLACED);
        }
    }
}