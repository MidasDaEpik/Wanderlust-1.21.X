package com.midasdaepik.wanderlust.block;

import com.midasdaepik.wanderlust.registries.WLBlocks;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class SculkHull extends Block implements SculkBehaviour, Fallable {
    public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 15);
    public static final IntegerProperty LIT = IntegerProperty.create("lit", 0, 2);

    public SculkHull(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(CHARGE, 0).setValue(LIT, 0));
    }

    public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos pos, RandomSource random, SculkSpreader spreader, boolean shouldConvertBlocks) {
        BlockPos blockpos = cursor.getPos();

        int i;
        if (level.getBlockState(blockpos).getValue(LIT) > 0) {
            i = cursor.getCharge() + level.getBlockState(blockpos).getValue(LIT) * 3;
            level.setBlock(blockpos, level.getBlockState(blockpos).setValue(LIT, 0), 3);
        } else {
            i = cursor.getCharge();
        }

        if (i != 0 && random.nextInt(spreader.chargeDecayRate()) == 0) {
            boolean flag = blockpos.closerThan(pos, spreader.noGrowthRadius());
            return random.nextInt(spreader.additionalDecayRate()) != 0 ? i : i - (flag ? 1 : getDecayPenalty(spreader, blockpos, pos, i));
        } else {
            return i;
        }
    }

    private static int getDecayPenalty(SculkSpreader spreader, BlockPos cursorPos, BlockPos rootPos, int charge) {
        int i = spreader.noGrowthRadius();
        float f = Mth.square((float)Math.sqrt(cursorPos.distSqr(rootPos)) - (float)i);
        int j = Mth.square(24 - i);
        float f1 = Math.min(1.0F, f / (float)j);
        return Math.max(1, (int)((float)charge * f1 * 0.5F));
    }

    public boolean canChangeBlockStateOnSpread() {
        return false;
    }

    protected void randomTick(BlockState pBlockState, ServerLevel pLevel, BlockPos pBlockPos, RandomSource pRandomSource) {
        if (pBlockState.getValue(CHARGE) > 0) {
            int pCharge = pBlockState.getValue(CHARGE);
            if (pCharge > 0) {
                AtomicBoolean pSurrounding = new AtomicBoolean(false);

                BlockPos.betweenClosedStream(pBlockPos.offset(1, 1, 1), pBlockPos.offset(-1, -1, -1))
                        .forEach(pBlockPosIteration -> {
                            BlockState pBlockStateIteration = pLevel.getBlockState(pBlockPosIteration);

                            int pDist = Math.abs(pBlockPosIteration.getX() - pBlockPos.getX()) + Math.abs(pBlockPosIteration.getZ() - pBlockPos.getZ());

                            if (pBlockStateIteration.is(WLTags.SCULK_HULL_REPLACABLE)) {
                                pSurrounding.set(true);
                                if (pRandomSource.nextInt(3) >= 1) {
                                    pLevel.setBlock(pBlockPosIteration, getDefaultBlockState(pLevel, pBlockPosIteration).setValue(CHARGE, Math.max(pCharge - (pDist <= 0 ? 1 : pRandomSource.nextInt(1, pDist + 1)), 0)), 3);
                                    clearSculkVein(pLevel, pBlockPosIteration);

                                    if (pRandomSource.nextInt(4) >= 1) {
                                        pLevel.scheduleTick(pBlockPosIteration, WLBlocks.SCULK_HULL.get(), pRandomSource.nextInt(20) + 1);
                                    }
                                }
                            } else if (pBlockStateIteration.is(WLTags.SCULK_TANGLE_REPLACABLE)) {
                                pSurrounding.set(true);
                                if (RandomSource.create().nextInt(2) == 1) {
                                    pLevel.setBlock(pBlockPosIteration, SculkTangle.getDefaultSpreadingBlockState(pLevel, pBlockPosIteration), 3);
                                }
                            }
                        });

                if (!pSurrounding.get()) {
                    pLevel.setBlock(pBlockPos, pBlockState.setValue(CHARGE, 0), 3);
                }
            }
        } else {
            int pLit = pBlockState.getValue(LIT);
            if (pLit < 2 && pRandomSource.nextInt(5) == 1) {
                boolean pAdjecentLitHigher = false;

                for (Direction pDirection : Direction.values()) {
                    BlockPos pBlockPosIteration = pBlockPos.relative(pDirection);
                    BlockState pBlockStateIteration = pLevel.getBlockState(pBlockPosIteration);

                    if (pBlockStateIteration.is(WLBlocks.SCULK_HULL) && pBlockStateIteration.getValue(LIT) == pLit + 1) {
                        pAdjecentLitHigher = true;
                        break;
                    }
                }

                if (!pAdjecentLitHigher) {
                    pLevel.setBlock(pBlockPos, pBlockState.setValue(LIT, pLit + 1), 3);
                }
            }

            if (FallingBlock.isFree(pLevel.getBlockState(pBlockPos.below())) && pBlockPos.getY() >= pLevel.getMinBuildHeight()) {
                FallingBlockEntity.fall(pLevel, pBlockPos, pBlockState);
            }
        }
    }

    public static void clearSculkVein(LevelAccessor pLevel, BlockPos pBlockPos) {
        for (Direction pDirection : Direction.values()) {
            BlockPos pBlockPosIteration = pBlockPos.relative(pDirection);
            BlockState pBlockStateIteration = pLevel.getBlockState(pBlockPosIteration);

            if (pBlockStateIteration.is(Blocks.SCULK_VEIN)) {
                pLevel.destroyBlock(pBlockPosIteration, false);
            }
        }
    }

    public int getExpDrop(BlockState pBlockState, LevelAccessor pLevel, BlockPos pBlockPos, @Nullable BlockEntity pBlockEntity, @Nullable Entity pBreaker, ItemStack pTool) {
        return 1;
    }

    public static BlockState getDefaultBlockState(LevelAccessor pLevel, BlockPos pBlockPos) {
        return WLBlocks.SCULK_HULL.get().defaultBlockState();
    }

    public static BlockState getDefaultSpreadingBlockState(LevelAccessor pLevel, BlockPos pBlockPos) {
        return getDefaultBlockState(pLevel, pBlockPos).setValue(CHARGE, Mth.nextInt(RandomSource.create(), 10, 13));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGE, LIT);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState();
    }
}
