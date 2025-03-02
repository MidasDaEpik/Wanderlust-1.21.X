package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RRDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Wanderlust.MOD_ID);

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> NO_GRAVITY = DATA_COMPONENT_TYPES.register("no_gravity",
            () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
    );

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ITEM_TOGGLE = DATA_COMPONENT_TYPES.register("item_toggle",
            () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
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
