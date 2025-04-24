package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WLSounds {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> EFFECT_ECHO_ACCUMULATE = SOUND_EVENTS.register("effect_echo_accumulate",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "effect_echo_accumulate"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> EFFECT_ECHO_RELEASE = SOUND_EVENTS.register("effect_echo_release",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "effect_echo_release"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_CHARYBDIS_ACTIVATE = SOUND_EVENTS.register("item_charybdis_activate",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_charybdis_activate"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_DRAGONS_RAGE_BREATH = SOUND_EVENTS.register("item_dragons_rage_breath",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_dragons_rage_breath"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_DRAGONS_RAGE_ROAR = SOUND_EVENTS.register("item_dragons_rage_roar",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_dragons_rage_roar"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_FIRESTORM_KATANA_CLOUD = SOUND_EVENTS.register("item_firestorm_katana_cloud",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_firestorm_katana_cloud"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_LYRE_OF_ECHOES_NOTE = SOUND_EVENTS.register("item_lyre_of_echoes_note",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_lyre_of_echoes_note"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_LYRE_OF_ECHOES_SONIC_BOOM = SOUND_EVENTS.register("item_lyre_of_echoes_sonic_boom",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_lyre_of_echoes_sonic_boom"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_MYCORIS_CLOUD = SOUND_EVENTS.register("item_mycoris_cloud",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_mycoris_cloud"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_OBSIDIAN_BULWARK_SHIELD = SOUND_EVENTS.register("item_obsidian_bulwark_shield",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_obsidian_bulwark_shield"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_PYROSWEEP_DASH = SOUND_EVENTS.register("item_pyrosweep_dash",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_pyrosweep_dash"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_PYROSWEEP_SHIELD = SOUND_EVENTS.register("item_pyrosweep_shield",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_pyrosweep_shield"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_SCYLLA_SPREAD = SOUND_EVENTS.register("item_scylla_spread",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_scylla_spread"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_SEARING_STAFF_SUMMON = SOUND_EVENTS.register("item_searing_staff_summon",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_searing_staff_summon"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_SOULGORGE_SHIELD = SOUND_EVENTS.register("item_soulgorge_shield",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_soulgorge_shield"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WARPED_RAPIER_TELEPORT = SOUND_EVENTS.register("item_warped_rapier_teleport",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_warped_rapier_teleport"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WARPTHISTLE_TELEPORT = SOUND_EVENTS.register("item_warpthistle_teleport",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_warpthistle_teleport"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WHISPERWIND_SHOOT = SOUND_EVENTS.register("item_whisperwind_shoot",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_whisperwind_shoot"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WITHERBLADE_WITHER = SOUND_EVENTS.register("item_witherblade_wither",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_witherblade_wither"))
    );

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
