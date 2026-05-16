package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
                .component(WLDataComponents.MAXIMUM_EXPERIENCE.get(), WLAttributeConfig.CONFIG.ItemCatalystCrystalMaxSoulsIncreasePerEchoShard.get() * WLAttributeConfig.CONFIG.ItemCatalystCrystalBaseEchoShardCount.get())
                .component(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASE.get(), WLAttributeConfig.CONFIG.ItemCatalystCrystalMaxSoulsIncreasePerEchoShard.get())
                .component(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM.get(), 0)
                .component(WLDataComponents.ITEM_TOGGLE_INT.get(), WLAttributeConfig.CONFIG.ItemCatalystCrystalBaseEchoShardCount.get())
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

                if (ItemExperience > 0 && pPlayer != null) {
                    int ExperienceUsage;

                    if (pPlayer.isCrouching()) {
                        ExperienceUsage = Math.min(
                                pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 0) + pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM, 0) * pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASE, 250),
                                pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 1) * pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASE, 250));
                    } else {
                        ExperienceUsage = 20;
                    }

                    if (ExperienceUsage > ItemExperience) {
                        ExperienceUsage = ItemExperience;
                    }

                    if (catalystDetection(pLevel, pContext, pCatalyst, ExperienceUsage)) {
                        pItemStack.set(WLDataComponents.EXPERIENCE, ItemExperience - ExperienceUsage);

                        pPlayer.awardStat(Stats.ITEM_USED.get(this));

                        pLevel.playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1f, 1.2f,0);
                    }

                    pPlayer.getCooldowns().addCooldown(this, 10);

                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.FAIL;
        }
    }

    private static boolean catalystDetection(Level pLevel, UseOnContext pContext, SculkCatalystBlockEntity pCatalyst, int pCharge) {
        BlockPos pBlockPos = pContext.getClickedPos();

        BlockPos pBlockPosIteration;
        BlockState pBlockStateIteration;

        for (Direction pDirection1 : Direction.allShuffled(RandomSource.create())) {
            pBlockPosIteration = pBlockPos.relative(pDirection1);
            pBlockStateIteration = pLevel.getBlockState(pBlockPosIteration);

            boolean pSelfSpace = false;
            for (Direction pDirection2 : Direction.values()) {
                if (pDirection2 != pDirection1.getOpposite() && isSelfSturdy(pLevel, pBlockPosIteration, pBlockStateIteration, pDirection2)) {
                    pSelfSpace = true;
                    break;
                }
            }

            boolean pTargetSpace = false;
            if (pBlockStateIteration.is(WLTags.SCULK_CAN_BE_REPLACED)) {
                for (Direction pDirection2 : Direction.values()) {
                    if (pDirection2 != pDirection1.getOpposite() && isTargetSturdy(pLevel, pBlockPosIteration, pDirection2)) {
                        pTargetSpace = true;
                        break;
                    }
                }
            }

            if (pSelfSpace || pTargetSpace) {
                pCatalyst.getListener().getSculkSpreader().addCursors(pBlockPosIteration, pCharge);
                return true;
            }
        }

        return false;
    }

    private static boolean isSelfSturdy(LevelAccessor pLevel, BlockPos pBlockPos, BlockState pBlockState, Direction pDirection) {
        return pLevel.getBlockState(pBlockPos.relative(pDirection)).is(WLTags.SCULK_SURFACE_BLOCKS) &&
                pBlockState.isFaceSturdy(pLevel, pBlockPos, pDirection);
    }

    private static boolean isTargetSturdy(LevelAccessor pLevel, BlockPos pBlockPos, Direction pDirection) {
        BlockPos pBlockPos2 = pBlockPos.relative(pDirection);
        return pLevel.getBlockState(pBlockPos2).isFaceSturdy(pLevel, pBlockPos2, pDirection.getOpposite());
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pItemStack, ItemStack pOtherItemStack, Slot pSlot, ClickAction pClickAction, Player pPlayer, SlotAccess pSlotAccess) {
        if (pClickAction == ClickAction.SECONDARY && pOtherItemStack.isEmpty()) {
            int pMaxCapacityIncrease = pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASE, 250);
            int pMaxCapacity = pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 0) + pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM, 0) * pMaxCapacityIncrease;
            int pHighestValue = (int) Math.ceil((double) pMaxCapacity / pMaxCapacityIncrease);

            int pCurrentValue = pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 1);
            int pNewValue = pCurrentValue + 1;
            if (pNewValue > pHighestValue) {
                pNewValue = 1;
            }

            if (pNewValue == pCurrentValue) {
                pPlayer.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.6f, 1f);
            } else {
                pPlayer.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.8f, 1f);
                pItemStack.set(WLDataComponents.ITEM_TOGGLE_INT, pNewValue);
            }

            return true;
        } else {
            return false;
        }
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
            pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.shift_desc_6"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.empty());
        int pMaxExperienceCapacity = pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 0) + pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASING_ITEM, 0) * pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASE, 250);
        pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.lore_desc_1",
                "§a" + pItemStack.getOrDefault(WLDataComponents.EXPERIENCE, 0),
                "§a" + pMaxExperienceCapacity
        ));
        pTooltipComponents.add(Component.translatable("item.wanderlust.catalyst_crystal.lore_desc_2",
                "§a" + Math.min(pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 1) * pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE_INCREASE, 250), pMaxExperienceCapacity)
        ));
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
