package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class WLEnchantmentEffects {
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_EFFECT_COMPONENTS =
            DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Wanderlust.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> NAMELESS =
            register("nameless", b -> b.persistent(Unit.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> CONCEALMENT =
            register("concealment", b -> b.persistent(Unit.CODEC));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String pName, UnaryOperator<DataComponentType.Builder<T>> pOperator) {
        return ENCHANTMENT_EFFECT_COMPONENTS.register(pName, () -> pOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        ENCHANTMENT_EFFECT_COMPONENTS.register(eventBus);
    }
}
