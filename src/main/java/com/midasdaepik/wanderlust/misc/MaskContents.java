package com.midasdaepik.wanderlust.misc;

import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record MaskContents(ItemStack pMask) {
    public static final Codec<MaskContents> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, MaskContents> STREAM_CODEC;

    public MaskContents(ItemStack pMask) {
        this.pMask = pMask;
    }

    public ItemStack getMask() {
        return pMask;
    }

    public ItemStack getMaskCopy() {
        return pMask.copy();
    }

    public int getMaskId() {
        if (pMask.is(WLItems.MASK)) {
            return pMask.getOrDefault(WLDataComponents.MASK_TYPE, 0);
        } else {
            return -1;
        }
    }

    public boolean isEmpty() {
        return pMask.isEmpty();
    }

    static {
        CODEC = ItemStack.CODEC.xmap(MaskContents::new, (p_331551_) -> p_331551_.pMask);
        STREAM_CODEC = ItemStack.STREAM_CODEC.map(MaskContents::new, (p_331649_) -> p_331649_.pMask);
    }
}
