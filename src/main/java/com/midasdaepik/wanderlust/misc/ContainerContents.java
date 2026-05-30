package com.midasdaepik.wanderlust.misc;

import com.google.common.collect.Lists;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ContainerContents implements TooltipComponent {
    final List<ItemStack> items;
    final Fraction weight;

    ContainerContents(List<ItemStack> items, Fraction weight) {
        this.items = items;
        this.weight = weight;
    }

    public ContainerContents(List<ItemStack> pItems, ContainerContentsWeightProcessor pProcessor) {
        this(pItems, pProcessor.computeContentWeight(pItems));
    }

    public int getStorageSize() {
        return 64;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.transform(this.items, ItemStack::copy);
    }

    public ItemStack getItemUnsafe(int index) {
        return this.items.get(index);
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public int size() {
        return this.items.size();
    }

    public Fraction weight() {
        return this.weight;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public boolean equals(Object pOther) {
        if (this == pOther) {
            return true;
        } else {
            boolean var10000;
            if (pOther instanceof ContainerContents pOtherContents) {
                var10000 = this.weight.equals(pOtherContents.weight) && ItemStack.listMatches(this.items, pOtherContents.items);
            } else {
                var10000 = false;
            }
            return var10000;
        }
    }

    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }

    public String toString() {
        return "BundleContents" + String.valueOf(this.items);
    }

    public static abstract class ContainerContentsWeightProcessor {
        protected static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);

        public Fraction computeContentWeight(List<ItemStack> content) {
            Fraction fraction = Fraction.ZERO;

            for(ItemStack itemstack : content) {
                fraction = fraction.add(getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)));
            }

            return fraction;
        }

        public Fraction getWeight(ItemStack pItemStack) {
            var pBundleContents = pItemStack.get(WLDataComponents.HOARDERS_BUNDLE_CONTENTS);
            if (pBundleContents != null) {
                return BUNDLE_IN_BUNDLE_WEIGHT.add(pBundleContents.weight());
            } else {
                return Fraction.getFraction(1, pItemStack.getMaxStackSize() * getWeightModifier(pItemStack));
            }
        }

        public int getWeightModifier(ItemStack pItemStack) {
            return 1;
        }
    }

    public static abstract class Mutable {
        protected final ContainerContentsWeightProcessor processor;
        protected final List<ItemStack> items;
        protected Fraction weight;

        public Mutable(ContainerContents contents, ContainerContentsWeightProcessor processor) {
            this.processor = processor;
            this.items = new ArrayList(contents.items);
            this.weight = contents.weight;
        }

        public abstract ContainerContents toImmutable();

        public ContainerContents.Mutable clearItems() {
            this.items.clear();
            this.weight = Fraction.ZERO;
            return this;
        }

        public boolean isItemValid(ItemStack pItemStack) {
            return true;
        }

        private int findStackIndex(ItemStack stack) {
            if (stack.isStackable()) {
                for (int i = 0; i < items.size(); ++i) {
                    ItemStack pCompare = items.get(i);
                    if (pCompare.getCount() >= pCompare.getMaxStackSize()) {
                        continue;
                    }
                    if (ItemStack.isSameItemSameComponents(pCompare, stack)) {
                        return i;
                    }
                }
            }
            return -1;
        }

        private int getMaxAmountToAdd(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.weight);
            return Math.max(fraction.divideBy(processor.getWeight(stack)).intValue(), 0);
        }

        public int tryTransfer(Slot pSlot, Player pPlayer) {
            ItemStack pItemstack = pSlot.getItem();
            int i = getMaxAmountToAdd(pItemstack);
            int pCount = tryInsert(pItemstack);
            if (pCount > 0) {
                pSlot.safeTake(pItemstack.getCount(), i, pPlayer);
            }
            return pCount;
        }

        public int tryInsert(ItemStack pItemStack) {
            if (!pItemStack.isEmpty() && pItemStack.canFitInsideContainerItems() && isItemValid(pItemStack)) {
                int i = Math.min(pItemStack.getCount(), getMaxAmountToAdd(pItemStack));
                if (i == 0) {
                    return 0;
                } else {
                    weight = weight.add(processor.getWeight(pItemStack).multiplyBy(Fraction.getFraction(i, 1)));
                    int j = findStackIndex(pItemStack);
                    if (j != -1) {
                        ItemStack pItemStackIterator = items.remove(j);
                        int pTransferSize = Math.min(i, pItemStack.getMaxStackSize() - pItemStackIterator.getCount());

                        pItemStack.shrink(pTransferSize);
                        items.addFirst(pItemStackIterator.copyWithCount(pItemStackIterator.getCount() + pTransferSize));

                        if (!pItemStack.isEmpty()) {
                            int pRemainder = pItemStack.getCount();
                            weight = weight.add(processor.getWeight(pItemStack).multiplyBy(Fraction.getFraction(pRemainder, 1)));

                            pItemStack.shrink(pRemainder);
                            items.addFirst(pItemStackIterator.copyWithCount(pRemainder));
                        }
                    } else {
                        items.addFirst(pItemStack.split(i));
                    }
                    return i;
                }
            } else {
                return 0;
            }
        }

        @Nullable
        public ItemStack removeOne() {
            if (items.isEmpty()) {
                return null;
            } else {
                ItemStack itemstack = items.removeFirst().copy();
                weight = weight.subtract(processor.getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)));
                return itemstack;
            }
        }
    }
}
