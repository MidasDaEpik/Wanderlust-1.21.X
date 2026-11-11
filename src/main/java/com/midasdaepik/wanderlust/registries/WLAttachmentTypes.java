package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class WLAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Wanderlust.MOD_ID);

    //Living Entity
    public static final Supplier<AttachmentType<Float>> ECHO_STORED_DAMAGE = ATTACHMENT_TYPES.register(
            "echo_stored_damage", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).build()
    );

    //Effect Sync
    public static final Supplier<AttachmentType<Boolean>> DRAGON_WINGS_STATUS = ATTACHMENT_TYPES.register(
            "dragon_wings_timer", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build()
    );

    public static final Supplier<AttachmentType<Boolean>> PHANTASMAL_STATUS = ATTACHMENT_TYPES.register(
            "phantasmal_timer", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build()
    );

    public static final Supplier<AttachmentType<Integer>> SOUL_WISPS_STATUS = ATTACHMENT_TYPES.register(
            "soul_wisps_status", () -> AttachmentType.builder(() -> -1).serialize(Codec.INT).sync(ByteBufCodecs.INT).build()
    );

    //Arrow
    public static final Supplier<AttachmentType<Integer>> SPECIAL_ARROW_WEAPON_TYPE = ATTACHMENT_TYPES.register(
            "special_arrow_weapon_type", () -> AttachmentType.builder(() -> -1).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> SPECIAL_ARROW_MODIFIER_TYPE = ATTACHMENT_TYPES.register(
            "special_arrow_modifier_type", () -> AttachmentType.builder(() -> -1).serialize(Codec.INT).sync(ByteBufCodecs.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> HOMING_ARROW_TARGET = ATTACHMENT_TYPES.register(
            "homing_arrow_target", () -> AttachmentType.builder(() -> -1).serialize(Codec.INT).sync(ByteBufCodecs.INT).build()
    );

    //Phantom
    public static final Supplier<AttachmentType<UUID>> PHANTOM_ANGRY_AT = ATTACHMENT_TYPES.register(
            "phantom_angry_at", () -> AttachmentType.builder(UUID::randomUUID).serialize(UUIDUtil.CODEC).build()
    );

    //Player
    public static final Supplier<AttachmentType<Integer>> TIME_SINCE_DAMAGE_DEALT = ATTACHMENT_TYPES.register(
            "time_since_last_attack", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> TIME_SINCE_DAMAGE_TAKEN = ATTACHMENT_TYPES.register(
            "time_since_last_damage", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> INSOMNIA = ATTACHMENT_TYPES.register(
            "insomnia", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Codec<Vec3> VEC3_CODEC =
            RecordCodecBuilder.create(pInstance -> pInstance.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(Vec3::x),
                    Codec.DOUBLE.fieldOf("y").forGetter(Vec3::y),
                    Codec.DOUBLE.fieldOf("z").forGetter(Vec3::z)
            ).apply(pInstance, Vec3::new));

    public static final Supplier<AttachmentType<Vec3>> LAST_SLEPT_LOCATION = ATTACHMENT_TYPES.register(
            "last_slept_location", () -> AttachmentType.builder(() -> new Vec3(0, 0, 0)).serialize(VEC3_CODEC).build()
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

    public static final Supplier<AttachmentType<Integer>> PHANTOM_HOVER = ATTACHMENT_TYPES.register(
            "phantom_hover", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Boolean>> PHANTOM_HOVER_TOGGLE = ATTACHMENT_TYPES.register(
            "phantom_hover_toggle", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build()
    );

    public static final Supplier<AttachmentType<Integer>> PYROSWEEP_DASH = ATTACHMENT_TYPES.register(
            "pyrosweep_dash", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> PYROSWEEP_CHARGE = ATTACHMENT_TYPES.register(
            "pyrosweep_charge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> RULER_OF_NOTHING_STILL = ATTACHMENT_TYPES.register(
            "ruler_of_nothing_still", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    //Katana
    public static final Supplier<AttachmentType<Integer>> KATANA_COMBO = ATTACHMENT_TYPES.register(
            "katana_combo", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> KATANA_COMBO_DECAY = ATTACHMENT_TYPES.register(
            "katana_combo_decay", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Boolean>> KATANA_ABILITY_ACTIVE = ATTACHMENT_TYPES.register(
            "katana_ability_active", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build()
    );

    public static final Supplier<AttachmentType<Integer>> KATANA_ABILITY_CHARGE_LEVEL = ATTACHMENT_TYPES.register(
            "katana_ability_charge_level", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> KATANA_ABILITY_AVAILABLE_TIMER = ATTACHMENT_TYPES.register(
            "katana_ability_available_timer", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<String>> KATANA_ABILITY_WEAPON = ATTACHMENT_TYPES.register(
            "katana_ability_available_weapon", () -> AttachmentType.builder(() -> "").serialize(Codec.STRING).sync(ByteBufCodecs.STRING_UTF8).build()
    );

    public static final Supplier<AttachmentType<Integer>> KATANA_ABILITY_POST_TIMER = ATTACHMENT_TYPES.register(
            "katana_ability_post_timer", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.INT).build()
    );

    public static final Supplier<AttachmentType<Integer>> KITSUNEBI_TARGET = ATTACHMENT_TYPES.register(
            "kitsunebi_target", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
