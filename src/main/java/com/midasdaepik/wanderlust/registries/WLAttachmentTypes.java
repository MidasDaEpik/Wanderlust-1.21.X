package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class WLAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Wanderlust.MOD_ID);

    //Living Entity
    public static final Supplier<AttachmentType<Float>> ECHO_STORED_DAMAGE = ATTACHMENT_TYPES.register(
            "echo_stored_damage", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).build()
    );

    //Arrow
    public static final Supplier<AttachmentType<Integer>> SPECIAL_ARROW_TYPE = ATTACHMENT_TYPES.register(
            "special_arrow_type", () -> AttachmentType.builder(() -> -1).serialize(Codec.INT).build()
    );

    //Player
    public static final Supplier<AttachmentType<Integer>> TIME_SINCE_LAST_ATTACK = ATTACHMENT_TYPES.register(
            "time_since_last_attack", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> TIME_SINCE_LAST_DAMAGE = ATTACHMENT_TYPES.register(
            "time_since_last_damage", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    //Item Specific
    public static final Supplier<AttachmentType<Integer>> BLAZE_REAP_CHARGE = ATTACHMENT_TYPES.register(
            "blaze_reap_charge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> CHARYBDIS_CHARGE = ATTACHMENT_TYPES.register(
            "charybdis_charge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> DRAGON_CHARGE = ATTACHMENT_TYPES.register(
            "dragon_charge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> PYROSWEEP_DASH = ATTACHMENT_TYPES.register(
            "pyrosweep_dash", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> PYROSWEEP_CHARGE = ATTACHMENT_TYPES.register(
            "pyrosweep_charge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
