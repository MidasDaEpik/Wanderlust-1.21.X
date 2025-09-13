package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CatalystCrystal extends Item {
    public CatalystCrystal(Properties pProperties) {
        super(pProperties
                .stacksTo(1)
                .rarity(WLEnumExtensions.RARITY_SCULK.getValue())
                .component(WLDataComponents.EXPERIENCE.get(), 0)
                .component(WLDataComponents.MAXIMUM_EXPERIENCE.get(), WLAttributeConfig.CONFIG.ItemCatalystCrystalMaxSouls.get())
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        if (pLevel instanceof ServerLevel) {
            BlockEntity pBlockEntity = pLevel.getBlockEntity(pContext.getClickedPos());
            if (pBlockEntity instanceof SculkCatalystBlockEntity pCatalyst) {
                Player pPlayer = pContext.getPlayer();
                ItemStack pItemStack = pContext.getItemInHand();
                int ItemExperience = pItemStack.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue();

                if (pPlayer.isCrouching()) {
                    if (ItemExperience > 0 && catalystDetection(pLevel, pContext, pCatalyst, ItemExperience)) {
                        pItemStack.set(WLDataComponents.EXPERIENCE, 0);

                        pPlayer.awardStat(Stats.ITEM_USED.get(this));

                        pLevel.playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1f, 1.2f,0);
                    }

                } else {
                    if (ItemExperience > 20) {
                        if (catalystDetection(pLevel, pContext, pCatalyst, 20)) {
                            pItemStack.set(WLDataComponents.EXPERIENCE, ItemExperience - 20);

                            pPlayer.awardStat(Stats.ITEM_USED.get(this));

                            pLevel.playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1f, 1.2f,0);
                        }

                    } else if (ItemExperience > 0) {
                        if (catalystDetection(pLevel, pContext, pCatalyst, ItemExperience)) {
                            pItemStack.set(WLDataComponents.EXPERIENCE, 0);

                            pPlayer.awardStat(Stats.ITEM_USED.get(this));

                            pLevel.playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1f, 1.2f,0);
                        }
                    }
                }

                pPlayer.getCooldowns().addCooldown(this, 10);

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private static boolean catalystDetection(Level pLevel, UseOnContext pContext, SculkCatalystBlockEntity pCatalyst, int pCharge) {
        BlockPos pBlockPos = pContext.getClickedPos();
        Boolean[] pDirection = new Boolean[]{false, false, false, false, false, false};

        int pIteratorA;
        int pIteratorB;
        BlockPos pIteratorC;
        BlockState pIteratorD;

        for (int Loop = 1; Loop <= 6; Loop++) {
            pIteratorA = Mth.nextInt(RandomSource.create(), 1, 7 - Loop);

            pIteratorB = 0;
            for (int Loop2 = 0; Loop2 <= 5; Loop2++) {
                if (!pDirection[Loop2]) {
                    pIteratorB += 1;
                }

                if (pIteratorB == pIteratorA) {
                    pIteratorB = Loop2 + 1;
                    break;
                }
            }
            pDirection[pIteratorB - 1] = true;

            switch (pIteratorB) {
                case 1 -> {
                    pIteratorC = pBlockPos.above();
                    pIteratorD = pLevel.getBlockState(pIteratorC);

                    if (pIteratorD.is(Blocks.SCULK) || pIteratorD.is(Blocks.SCULK_VEIN)
                            || (pIteratorD.is(BlockTags.REPLACEABLE) &&
                            (isSturdy(pLevel, pIteratorC, Direction.UP) ||
                                    isSturdy(pLevel, pIteratorC, Direction.NORTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.SOUTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.EAST) ||
                                    isSturdy(pLevel, pIteratorC, Direction.WEST)))) {
                        pCatalyst.getListener().getSculkSpreader().addCursors(pIteratorC, pCharge);
                        return true;
                    }
                }
                case 2 -> {
                    pIteratorC = pBlockPos.below();
                    pIteratorD = pLevel.getBlockState(pIteratorC);

                    if (pIteratorD.is(Blocks.SCULK) || pIteratorD.is(Blocks.SCULK_VEIN)
                            || (pIteratorD.is(BlockTags.REPLACEABLE) &&
                            (isSturdy(pLevel, pIteratorC, Direction.DOWN) ||
                                    isSturdy(pLevel, pIteratorC, Direction.NORTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.SOUTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.EAST) ||
                                    isSturdy(pLevel, pIteratorC, Direction.WEST)))) {
                        pCatalyst.getListener().getSculkSpreader().addCursors(pIteratorC, pCharge);
                        return true;
                    }
                }
                case 3 -> {
                    pIteratorC = pBlockPos.north();
                    pIteratorD = pLevel.getBlockState(pIteratorC);

                    if (pIteratorD.is(Blocks.SCULK) || pIteratorD.is(Blocks.SCULK_VEIN)
                            || (pIteratorD.is(BlockTags.REPLACEABLE) &&
                            (isSturdy(pLevel, pIteratorC, Direction.UP) ||
                                    isSturdy(pLevel, pIteratorC, Direction.DOWN) ||
                                    isSturdy(pLevel, pIteratorC, Direction.NORTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.EAST) ||
                                    isSturdy(pLevel, pIteratorC, Direction.WEST)))) {
                        pCatalyst.getListener().getSculkSpreader().addCursors(pIteratorC, pCharge);
                        return true;
                    }
                }
                case 4 -> {
                    pIteratorC = pBlockPos.south();
                    pIteratorD = pLevel.getBlockState(pIteratorC);

                    if (pIteratorD.is(Blocks.SCULK) || pIteratorD.is(Blocks.SCULK_VEIN)
                            || (pIteratorD.is(BlockTags.REPLACEABLE) &&
                            (isSturdy(pLevel, pIteratorC, Direction.UP) ||
                                    isSturdy(pLevel, pIteratorC, Direction.DOWN) ||
                                    isSturdy(pLevel, pIteratorC, Direction.SOUTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.EAST) ||
                                    isSturdy(pLevel, pIteratorC, Direction.WEST)))) {
                        pCatalyst.getListener().getSculkSpreader().addCursors(pIteratorC, pCharge);
                        return true;
                    }
                }
                case 5 -> {
                    pIteratorC = pBlockPos.east();
                    pIteratorD = pLevel.getBlockState(pIteratorC);

                    if (pIteratorD.is(Blocks.SCULK) || pIteratorD.is(Blocks.SCULK_VEIN)
                            || (pIteratorD.is(BlockTags.REPLACEABLE) &&
                            (isSturdy(pLevel, pIteratorC, Direction.UP) ||
                                    isSturdy(pLevel, pIteratorC, Direction.DOWN) ||
                                    isSturdy(pLevel, pIteratorC, Direction.NORTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.SOUTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.EAST)))) {
                        pCatalyst.getListener().getSculkSpreader().addCursors(pIteratorC, pCharge);
                        return true;
                    }
                }
                case 6 -> {
                    pIteratorC = pBlockPos.west();
                    pIteratorD = pLevel.getBlockState(pIteratorC);

                    if (pIteratorD.is(Blocks.SCULK) || pIteratorD.is(Blocks.SCULK_VEIN)
                            || (pIteratorD.is(BlockTags.REPLACEABLE) &&
                            (isSturdy(pLevel, pIteratorC, Direction.UP) ||
                                    isSturdy(pLevel, pIteratorC, Direction.DOWN) ||
                                    isSturdy(pLevel, pIteratorC, Direction.NORTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.SOUTH) ||
                                    isSturdy(pLevel, pIteratorC, Direction.WEST)))) {
                        pCatalyst.getListener().getSculkSpreader().addCursors(pIteratorC, pCharge);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSturdy(LevelAccessor pLevel, BlockPos pBlockPos, Direction pDirection) {
        BlockPos pBlockPos2 = pBlockPos.relative(pDirection);
        return pLevel.getBlockState(pBlockPos2).isFaceSturdy(pLevel, pBlockPos2, pDirection.getOpposite());
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.shift_desc_1"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.shift_desc_3"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.shift_desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.shift_desc_5"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.lore_desc_1", "§a" + pItemStack.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue(), "§a" + pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 0.0).intValue()));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
