package com.midasdaepik.wanderlust.misc;

import com.midasdaepik.wanderlust.registries.WLTags;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;

public class HoardersBundleContents extends ContainerContents {
    public static final Codec<HoardersBundleContents> CODEC = ItemStack.CODEC.listOf().xmap(HoardersBundleContents::new, (p_331551_) -> p_331551_.items);
    public static final StreamCodec<RegistryFriendlyByteBuf, HoardersBundleContents> STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(HoardersBundleContents::new, (p_331649_) -> p_331649_.items);
    public static final HoardersBundleContents EMPTY = new HoardersBundleContents(List.of());

    HoardersBundleContents(List<ItemStack> items, Fraction weight) {
        super(items, weight);
    }

    public HoardersBundleContents(List<ItemStack> items) {
        super(items, HoardersBundleWeightProcessor.PROCESSOR);
    }

    @Override
    public int getStorageSize() {
        return 1024;
    }

    public static class HoardersBundleWeightProcessor extends ContainerContents.ContainerContentsWeightProcessor {
        public static final HoardersBundleWeightProcessor PROCESSOR = new HoardersBundleWeightProcessor();

        @Override
        public int getWeightModifier(ItemStack pItemStack) {
            return 16;
        }
    }

    public static final class Mutable extends ContainerContents.Mutable {
        public Mutable(ContainerContents contents) {
            super(contents, HoardersBundleWeightProcessor.PROCESSOR);
        }

        @Override
        public HoardersBundleContents toImmutable() {
            return new HoardersBundleContents(items);
        }

        @Override
        public boolean isItemValid(ItemStack pItemStack) {
            if (pItemStack.is(WLTags.HOARDERS_BUNDLE_BLACKLIST)) {
                return false;
            } else if (pItemStack.getMaxStackSize() == 1) {
                return false;
            }else if (items.isEmpty()) {
                return true;
            } else {
                ItemStack pStoredItem = items.get(0);
                if (pItemStack.is(pStoredItem.getItem())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
