package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.misc.MaskContents;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WLDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Wanderlust.MOD_ID);

    public static DeferredHolder<DataComponentType<?>, DataComponentType<String>> COSMETIC_TYPE = DATA_COMPONENT_TYPES.register("cosmetic_type",
            () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COSMETIC_MATERIAL = DATA_COMPONENT_TYPES.register("cosmetic_material",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> NO_GRAVITY = DATA_COMPONENT_TYPES.register("no_gravity",
            () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ITEM_TOGGLE_BOOL = DATA_COMPONENT_TYPES.register("item_toggle_bool",
            () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ITEM_TOGGLE_INT = DATA_COMPONENT_TYPES.register("item_toggle_int",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MASK_TYPE = DATA_COMPONENT_TYPES.register("mask_type",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<MaskContents>> MASK_SLOT = DATA_COMPONENT_TYPES.register("mask_slot",
        () -> DataComponentType.<MaskContents>builder().persistent(MaskContents.CODEC).networkSynchronized(MaskContents.STREAM_CODEC).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> EXPERIENCE = DATA_COMPONENT_TYPES.register("experience",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MAXIMUM_EXPERIENCE = DATA_COMPONENT_TYPES.register("maximum_experience",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build()
    );

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
