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

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_FIRESTORM_KATANA_CLOUD = SOUND_EVENTS.register("item_firestorm_katana_cloud",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_firestorm_katana_cloud"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_SEARING_STAFF_SUMMON = SOUND_EVENTS.register("item_searing_staff_summon",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_searing_staff_summon"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_OBSIDIAN_BULWARK_SHIELD = SOUND_EVENTS.register("item_obsidian_bulwark_shield",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_obsidian_bulwark_shield"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WARPED_RAPIER_TELEPORT = SOUND_EVENTS.register("item_warped_rapier_teleport",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_warped_rapier_teleport"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WITHERBLADE_WITHER = SOUND_EVENTS.register("item_witherblade_wither",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_witherblade_wither"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WITHERBLADE_CLOUD = SOUND_EVENTS.register("item_witherblade_cloud",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_witherblade_cloud"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WITHERBLADE_SHIELD = SOUND_EVENTS.register("item_witherblade_shield",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_witherblade_shield"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_WITHERBLADE_TELEPORT = SOUND_EVENTS.register("item_witherblade_teleport",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_witherblade_teleport"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_LYRE_OF_ECHOES_NOTE = SOUND_EVENTS.register("item_lyre_of_echoes_note",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_lyre_of_echoes_note"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_LYRE_OF_ECHOES_SONIC_BOOM = SOUND_EVENTS.register("item_lyre_of_echoes_sonic_boom",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_lyre_of_echoes_sonic_boom"))
    );

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
