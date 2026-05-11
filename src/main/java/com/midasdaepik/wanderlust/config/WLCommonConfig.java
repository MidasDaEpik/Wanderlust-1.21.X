package com.midasdaepik.wanderlust.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class WLCommonConfig {
    public static final WLCommonConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    static {
        Pair<WLCommonConfig, ModConfigSpec> pPair = new ModConfigSpec.Builder().configure(WLCommonConfig::new);
        CONFIG = pPair.getLeft();
        CONFIG_SPEC = pPair.getRight();
    }

    public final ModConfigSpec.IntValue ItemPhantomArmorEffectDuration;
    public final ModConfigSpec.IntValue ItemPhantomArmorEntityEffectDuration;
    public final ModConfigSpec.IntValue ItemPhantomArmorBaseCooldown;
    public final ModConfigSpec.IntValue ItemPhantomArmorHoodCooldownDecrease;
    public final ModConfigSpec.IntValue ItemPhantomArmorTunicCooldownDecrease;
    public final ModConfigSpec.IntValue ItemPhantomArmorLeggingsCooldownDecrease;
    public final ModConfigSpec.IntValue ItemPhantomArmorBootsCooldownDecrease;
    public final ModConfigSpec.IntValue ItemPhantomArmorHoodDurationIncrease;
    public final ModConfigSpec.IntValue ItemPhantomArmorCloakDurationIncrease;
    public final ModConfigSpec.IntValue ItemPhantomArmorLeggingsDurationIncrease;
    public final ModConfigSpec.IntValue ItemPhantomArmorBootsDurationIncrease;

    public final ModConfigSpec.IntValue ItemSonicArpeggioBaseDamage;
    public final ModConfigSpec.IntValue ItemSonicArpeggioChargeDamage;

    public final ModConfigSpec.IntValue CharybdisChargeCap;
    public final ModConfigSpec.IntValue CharybdisChargeDecayTimer;
    public final ModConfigSpec.IntValue CharybdisChargeOnHit;
    public final ModConfigSpec.IntValue CharybdisChargeMaelstromUse;

    public final ModConfigSpec.IntValue DragonChargeCap;
    public final ModConfigSpec.IntValue DragonChargeDecayTimer;
    public final ModConfigSpec.IntValue DragonChargeOnHit;
    public final ModConfigSpec.IntValue DragonChargeOnRangedHit;
    public final ModConfigSpec.IntValue DragonChargeOnRangedHitConsecutive;
    public final ModConfigSpec.IntValue DragonChargeSwordUse;
    public final ModConfigSpec.IntValue DragonChargeArbalestUse;

    public final ModConfigSpec.IntValue PyrosweepChargeCap;
    public final ModConfigSpec.IntValue PyrosweepChargeDecayTimer;
    public final ModConfigSpec.IntValue PyrosweepChargeOnHit;
    public final ModConfigSpec.IntValue PyrosweepChargeFireWitherImmunity;
    public final ModConfigSpec.IntValue PyrosweepChargeDashUse;

    public final ModConfigSpec.BooleanValue EquippingCooldownActive;
    public final ModConfigSpec.IntValue EquippingCooldownDuration;
    public final ModConfigSpec.BooleanValue EquippingCooldownPreventChange;
    public final ModConfigSpec.BooleanValue EquippingCooldownAllArmor;

    public WLCommonConfig(ModConfigSpec.Builder builder) {
        builder.push("Abilities");
        builder.comment("Stats for item abilities.");

        builder.push("Sonic Arpeggio");
        builder.comment("Ability Damage is based on the Base Damage + Charge Damage * How Close to Fully Charged the Weapon is (0-1)");
        ItemSonicArpeggioBaseDamage = builder.defineInRange("Base Damage", 10, 1, 32767);
        ItemSonicArpeggioChargeDamage = builder.defineInRange("Charge Damage", 30, 1, 32767);
        builder.pop();

        builder.push("Phantom Armor");
        ItemPhantomArmorEffectDuration = builder.defineInRange("Phantasmal Effect Duration", 80, 1, 32767);
        ItemPhantomArmorEntityEffectDuration = builder.defineInRange("Phantasmal Effect Entity Duration (For Non-Players)", 20, 1, 32767);
        builder.comment("Cooldown of the Phantom Armor Ability, Time in Ticks (1 Tick = 20 Seconds)");
        ItemPhantomArmorBaseCooldown = builder.defineInRange("Base Cooldown", 1800, 1, 32767);
        ItemPhantomArmorHoodCooldownDecrease = builder.defineInRange("Hood Cooldown Decrease", 180, 1, 32767);
        ItemPhantomArmorTunicCooldownDecrease = builder.defineInRange("Tunic Cooldown Decrease", 320, 1, 32767);
        ItemPhantomArmorLeggingsCooldownDecrease = builder.defineInRange("Leggings Cooldown Decrease", 320, 1, 32767);
        ItemPhantomArmorBootsCooldownDecrease = builder.defineInRange("Boots Cooldown Decrease", 180, 1, 32767);
        builder.comment("Duration of the Phantom Cloak Alt Ability, Time in Ticks (1 Tick = 20 Seconds)");
        ItemPhantomArmorHoodDurationIncrease = builder.defineInRange("Hood Duration Increase", 500, 1, 32767);
        ItemPhantomArmorCloakDurationIncrease = builder.defineInRange("Cloak Duration Increase", 1100, 1, 32767);
        ItemPhantomArmorLeggingsDurationIncrease = builder.defineInRange("Leggings Duration Increase", 900, 1, 32767);
        ItemPhantomArmorBootsDurationIncrease = builder.defineInRange("Boots Duration Increase", 500, 1, 32767);

        builder.pop();

        builder.pop();

        builder.push("Charge Bars");
        builder.comment("Stats of the Charge Bars some items rely on.");

        builder.push("Charybdis Charge");
        builder.comment("Stats of the Charybdis Charge Bar.");
        CharybdisChargeCap = builder.defineInRange("Charge Cap", 1400, 1, 32767);
        CharybdisChargeDecayTimer = builder.defineInRange("Charge drained by Decay Timer (Per Tick)", 2, 1, 32767);
        CharybdisChargeOnHit = builder.defineInRange("Charge gained on Hit", 70, 1, 32767);
        CharybdisChargeMaelstromUse = builder.defineInRange("Charge used by Maelstrom (Per Tick)", 5, 1, 32767);
        builder.pop();

        builder.push("Dragon Charge");
        builder.comment("Stats of the Dragon Charge Bar.");
        DragonChargeCap = builder.defineInRange("Charge Cap", 3200, 1, 32767);
        DragonChargeDecayTimer = builder.defineInRange("Charge drained by Decay Timer (Per Tick)", 5, 1, 32767);
        DragonChargeOnHit = builder.defineInRange("Charge gained on Hit", 120, 1, 32767);
        DragonChargeOnRangedHit = builder.defineInRange("Charge gained on Ranged Hit", 100, 1, 32767);
        DragonChargeOnRangedHitConsecutive = builder.defineInRange("Charge gained on Ranged Hit (After First)", 50, 1, 32767);
        DragonChargeSwordUse = builder.defineInRange("Charge used by Sword Ability", 1200, 1, 32767);
        DragonChargeArbalestUse = builder.defineInRange("Charge used by Arbalest Ability", 600, 1, 32767);
        builder.pop();

        builder.push("Pyrosweep Charge");
        builder.comment("Stats of the Pyrosweep Charge Bar.");
        PyrosweepChargeCap = builder.defineInRange("Charge Cap", 160, 1, 32767);
        PyrosweepChargeDecayTimer = builder.defineInRange("Charge drained by Decay Timer (Per Tick)", 1, 1, 32767);
        PyrosweepChargeOnHit = builder.defineInRange("Charge gained on Hit", 10, 1, 32767);
        PyrosweepChargeFireWitherImmunity = builder.defineInRange("Charge required for Fire and Wither immunity", 80, 1, 32767);
        PyrosweepChargeDashUse = builder.defineInRange("Charge used by Dash", 60, 1, 32767);
        builder.pop();

        builder.pop();

        builder.push("Misc");
        builder.comment("Tweaks made by this mod for balance.");

        builder.push("Equipping Cooldown");
        builder.comment("Cooldown when equipping an item to prevent quick-swapping armor just to use the ability.");
        EquippingCooldownActive = builder.define("Enabled?", true);
        EquippingCooldownDuration = builder.defineInRange("Cooldown Duration (in Ticks)", 100, 1, 32767);
        EquippingCooldownPreventChange = builder.define("Prevents Armor on Cooldown from being Unequipped", false);
        EquippingCooldownAllArmor = builder.define("Instead of using a Tag, puts all Armor on cooldown when Equipped", false);
        builder.pop();

        builder.pop();
    }
}
