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

    public static final Holder<MobEffect> BULWARK = EFFECTS.register("bulwark",
            () -> new Bulwark(MobEffectCategory.BENEFICIAL,0x2552A5)
                    .addAttributeModifier(Attributes.MAX_ABSORPTION, ResourceLocation.fromNamespaceAndPath(MOD_ID,"bulwark"), 1f, AttributeModifier.Operation.ADD_VALUE)
    );

    public static final Holder<MobEffect> ECHO = EFFECTS.register("echo",
            () -> new Echo(MobEffectCategory.NEUTRAL,0x034150)
    );

    public static final Holder<MobEffect> FROSBITTEN = EFFECTS.register("frostbitten",
            () -> new Frostbitten(MobEffectCategory.HARMFUL,0xA5F8FD)
    );

    public static final Holder<MobEffect> PHANTASMAL = EFFECTS.register("phantasmal",
            () -> new Plunging(MobEffectCategory.NEUTRAL,0x94E6CE)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(MOD_ID,"phantasmal"), 0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final Holder<MobEffect> PLUNGING = EFFECTS.register("plunging",
            () -> new Plunging(MobEffectCategory.HARMFUL,0x943EA5)
    );

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
