package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.effect.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WLEffects {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, MOD_ID);

    public static final Holder<MobEffect> PLUNGING = EFFECTS.register("plunging",
            () -> new Plunging(MobEffectCategory.HARMFUL,9715365)
    );

    public static final Holder<MobEffect> FROSBITTEN = EFFECTS.register("frostbitten",
            () -> new Frostbitten(MobEffectCategory.HARMFUL,10877181)
    );

    public static final Holder<MobEffect> ECHO = EFFECTS.register("echo",
            () -> new Echo(MobEffectCategory.NEUTRAL,-16563888)
    );

    public static final Holder<MobEffect> BULWARK = EFFECTS.register("bulwark",
            () -> new Bulwark(MobEffectCategory.BENEFICIAL,2445989)
                    .addAttributeModifier(Attributes.MAX_ABSORPTION, ResourceLocation.fromNamespaceAndPath(MOD_ID,"bulwark"), (double)1F, AttributeModifier.Operation.ADD_VALUE)
    );

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
