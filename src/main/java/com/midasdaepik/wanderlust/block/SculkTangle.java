package com.midasdaepik.wanderlust.block;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLBlocks;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.IShearable;

import javax.annotation.Nullable;

public class SculkTangle extends Block implements SculkBehaviour, SimpleWaterloggedBlock, IShearable {
    public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 15);
    public static final BooleanProperty PERSISTENT;
    public static final BooleanProperty WATERLOGGED;

    public SculkTangle(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(CHARGE, 0).setValue(PERSISTENT, false).setValue(WATERLOGGED, false));
    }

    public static boolean isExceptionForConnection(BlockState state) {
        return true;
    }

    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return Shapes.empty();
    }

    public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos pos, RandomSource random, SculkSpreader spreader, boolean shouldConvertBlocks) {
        int i = cursor.getCharge();
        if (i != 0 && random.nextInt(spreader.chargeDecayRate()) == 0) {
            BlockPos blockpos = cursor.getPos();
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
        if (!pBlockState.getValue(PERSISTENT)) {
            int pCharge = pBlockState.getValue(CHARGE);
            if (pCharge > 0) {
                boolean pSurrounding = false;
                BlockPos pBlockPosIteration;
                BlockState pBlockStateIteration;

                for (Direction pDirection : Direction.allShuffled(pRandomSource)) {
                    pBlockPosIteration = pBlockPos.relative(pDirection);
                    pBlockStateIteration = pLevel.getBlockState(pBlockPosIteration);

                    if (pBlockStateIteration.is(WLTags.SCULK_TANGLE_REPLACABLE)) {
                        pSurrounding = true;
                        pLevel.setBlock(pBlockPosIteration, getDefaultBlockState(pLevel, pBlockPosIteration).setValue(CHARGE, Math.max(pCharge - pRandomSource.nextInt(1, 2), 0)), 3);

                        if (pRandomSource.nextInt(2) == 1) {
                            pLevel.scheduleTick(pBlockPosIteration, WLBlocks.SCULK_TANGLE.get(), pRandomSource.nextInt(20) + 1);
                        }
                    }
                }

                if (!pSurrounding) {
                    pLevel.removeBlock(pBlockPos, false);
                }

            } else {
                pLevel.removeBlock(pBlockPos, false);
            }
        }
    }

    public int getExpDrop(BlockState pBlockState, LevelAccessor pLevel, BlockPos pBlockPos, @Nullable BlockEntity pBlockEntity, @Nullable Entity pBreaker, ItemStack pTool) {
        return RandomSource.create().nextInt(6) == 1 ? 1 : 0;
    }

    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.isRainingAt(pos.above()) && random.nextInt(15) == 1) {
            BlockPos blockpos = pos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (!blockstate.canOcclude() || !blockstate.isFaceSturdy(level, blockpos, Direction.UP)) {
                ParticleUtils.spawnParticleBelow(level, pos, random, ParticleTypes.DRIPPING_WATER);
            }
        }

    }

    public static BlockState getDefaultBlockState(LevelAccessor pLevel, BlockPos pBlockPos) {
        return WLBlocks.SCULK_TANGLE.get().defaultBlockState().setValue(WATERLOGGED, pLevel.getFluidState(pBlockPos).getType() == Fluids.WATER);
    }

    public static BlockState getDefaultSpreadingBlockState(LevelAccessor pLevel, BlockPos pBlockPos) {
        return getDefaultBlockState(pLevel, pBlockPos).setValue(CHARGE, Mth.nextInt(RandomSource.create(), 7, 10));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGE, PERSISTENT, WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState pFluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(PERSISTENT, true).setValue(WATERLOGGED, pFluidstate.getType() == Fluids.WATER);
    }

    static {
        PERSISTENT = BlockStateProperties.PERSISTENT;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}
