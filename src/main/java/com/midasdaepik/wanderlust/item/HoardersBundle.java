package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.misc.HoardersBundleContents;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;
import java.util.Optional;

public class HoardersBundle extends Item {
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public HoardersBundle(Properties pProperties) {
        super(pProperties.stacksTo(1).rarity(WLEnumExtensions.RARITY_UMBRA.getValue()));
    }

    @Override
    public boolean canFitInsideContainerItems(ItemStack pItemStack) {
        return false;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pBundle, Slot pSlot, ClickAction pClickAction, Player pPlayer) {
        if (pBundle.getCount() == 1 && pClickAction == ClickAction.SECONDARY) {
            HoardersBundleContents pBundleContents = getContents(pBundle);
            if (pBundleContents == null) {
                return false;
            } else {
                ItemStack pItemstack = pSlot.getItem();
                HoardersBundleContents.Mutable pBundleContentsMutable = new HoardersBundleContents.Mutable(pBundleContents);
                if (pItemstack.isEmpty()) {
                    this.playRemoveOneSound(pPlayer);
                    ItemStack pItemStackTryRemove = pBundleContentsMutable.removeOne();
                    if (pItemStackTryRemove != null) {
                        ItemStack pRemoved = pSlot.safeInsert(pItemStackTryRemove);
                        pBundleContentsMutable.tryInsert(pRemoved);
                    }
                } else if (pItemstack.canFitInsideContainerItems()) {
                    int i = pBundleContentsMutable.tryTransfer(pSlot, pPlayer);
                    if (i > 0) {
                        this.playInsertSound(pPlayer);
                    }
                }
                setContents(pBundle, pBundleContentsMutable.toImmutable());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pBundle, ItemStack pItemStack, Slot pSlot, ClickAction pClickAction, Player pPlayer, SlotAccess pSlotAccess) {
        if (pBundle.getCount() != 1) {
            return false;
        } else if (pClickAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            HoardersBundleContents pBundleContents = getContents(pBundle);
            if (pBundleContents == null) {
                return false;
            } else {
                HoardersBundleContents.Mutable pBundleContentsMutable = new HoardersBundleContents.Mutable(pBundleContents);
                if (pItemStack.isEmpty()) {
                    ItemStack pRemoved = pBundleContentsMutable.removeOne();
                    if (pRemoved != null) {
                        this.playRemoveOneSound(pPlayer);
                        pSlotAccess.set(pRemoved);
                    }
                } else {
                    int i = pBundleContentsMutable.tryInsert(pItemStack);
                    if (i > 0) {
                        this.playInsertSound(pPlayer);
                    }
                }
                setContents(pBundle, pBundleContentsMutable.toImmutable());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pInteractionHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pInteractionHand);
        if (dropContents(itemstack, pPlayer)) {
            this.playDropContentsSound(pPlayer);
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public static float getFullnessDisplay(ItemStack pItemStack) {
        HoardersBundleContents pBundleContents = getContents(pItemStack);
        return pBundleContents.weight().floatValue();
    }

    @Override
    public boolean isBarVisible(ItemStack pItemStack) {
        HoardersBundleContents pBundleContents = getContents(pItemStack);
        return pBundleContents.weight().compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getBarWidth(ItemStack pItemStack) {
        HoardersBundleContents pBundleContents = getContents(pItemStack);
        return Math.min(1 + Mth.mulAndTruncate(pBundleContents.weight(), 12), 13);
    }

    @Override
    public int getBarColor(ItemStack pItemStack) {
        return BAR_COLOR;
    }

    private static boolean dropContents(ItemStack pItemStack, Player pPlayer) {
        HoardersBundleContents pBundleContents = getContents(pItemStack);
        if (!pBundleContents.isEmpty()) {
            setContents(pItemStack, emptyContents());
            if (pPlayer instanceof ServerPlayer) {
                pBundleContents.itemsCopy().forEach((pItem) -> pPlayer.drop(pItem, true));
            }

            return true;
        } else {
            return false;
        }
    }

    public static HoardersBundleContents getContents(ItemStack pItemStack) {
        return pItemStack.getOrDefault(WLDataComponents.HOARDERS_BUNDLE_CONTENTS, HoardersBundleContents.EMPTY);
    }

    public static HoardersBundleContents emptyContents() {
        return HoardersBundleContents.EMPTY;
    }

    public static void setContents(ItemStack pItemStack, HoardersBundleContents pPouchContents) {
        pItemStack.set(WLDataComponents.HOARDERS_BUNDLE_CONTENTS, pPouchContents);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pItemStack) {
        return !pItemStack.has(DataComponents.HIDE_TOOLTIP) && !pItemStack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) ?
                Optional.of(getContents(pItemStack)) :
                Optional.empty();
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        HoardersBundleContents pBundleContents = getContents(pItemStack);
        if (pBundleContents != null) {
            int pMax = pBundleContents.getStorageSize();
            int pCapacity = Mth.mulAndTruncate(pBundleContents.weight(), pMax);
            pTooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", pCapacity, pMax).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void onDestroyed(ItemEntity pItemEntity) {
        HoardersBundleContents pBundleContents = getContents(pItemEntity.getItem());
        if (pBundleContents != null) {
            setContents(pItemEntity.getItem(), emptyContents());
            ItemUtils.onContainerDestroyed(pItemEntity, pBundleContents.itemsCopy());
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
