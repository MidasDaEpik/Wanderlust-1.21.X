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

    public final ModConfigSpec.BooleanValue EquippingCooldownActive;
    public final ModConfigSpec.IntValue EquippingCooldownDuration;

    public final ModConfigSpec.IntValue CharybdisChargeCap;
    public final ModConfigSpec.IntValue CharybdisChargeOnHit;
    public final ModConfigSpec.IntValue CharybdisChargeDecayTimer;
    public final ModConfigSpec.IntValue CharybdisChargeMaelstromUse;

    public final ModConfigSpec.IntValue DragonChargeCap;
    public final ModConfigSpec.IntValue DragonChargeOnHit;
    public final ModConfigSpec.IntValue DragonChargeDecayTimer;

    public final ModConfigSpec.IntValue PyrosweepChargeCap;
    public final ModConfigSpec.IntValue PyrosweepChargeOnHit;
    public final ModConfigSpec.IntValue PyrosweepChargeDecayTimer;
    public final ModConfigSpec.IntValue PyrosweepChargeDashUse;
    public final ModConfigSpec.IntValue PyrosweepChargeShieldUse;

    public WLCommonConfig(ModConfigSpec.Builder builder) {
        builder.push("Misc");
        builder.comment("Tweaks made by this mod for balance.");

        builder.push("Equipping Cooldown");
        builder.comment("Cooldown when equipping an item to prevent quick-swapping armor just to use the ability.");
        EquippingCooldownActive = builder.define("Enabled?", true);
        EquippingCooldownDuration = builder.defineInRange("Cooldown Duration (in Ticks)", 100, 1, 32767);
        builder.pop();

        builder.push("Charybdis Charge");
        builder.comment("Stats of the Charybdis Charge Bar.");
        CharybdisChargeCap = builder.defineInRange("Charge Cap", 1400, 1, 32767);
        CharybdisChargeOnHit = builder.defineInRange("Charge gained on Hit", 70, 1, 32767);
        CharybdisChargeDecayTimer = builder.defineInRange("Charge drained by Decay Timer (Per Tick)", 2, 1, 32767);
        CharybdisChargeMaelstromUse = builder.defineInRange("Charge used by Maelstrom (Per Tick)", 5, 1, 32767);
        builder.pop();

        builder.push("Dragon Charge");
        builder.comment("Stats of the Dragon Charge Bar.");
        DragonChargeCap = builder.defineInRange("Charge Cap", 1800, 1, 32767);
        DragonChargeOnHit = builder.defineInRange("Charge gained on Hit", 120, 1, 32767);
        DragonChargeDecayTimer = builder.defineInRange("Charge drained by Decay Timer (Per Tick)", 6, 1, 32767);
        builder.pop();

        builder.push("Pyrosweep Charge");
        builder.comment("Stats of the Pyrosweep Charge Bar.");
        PyrosweepChargeCap = builder.defineInRange("Charge Cap", 160, 1, 32767);
        PyrosweepChargeOnHit = builder.defineInRange("Charge gained on Hit", 10, 1, 32767);
        PyrosweepChargeDecayTimer = builder.defineInRange("Charge drained by Decay Timer (Per Tick)", 1, 1, 32767);
        PyrosweepChargeDashUse = builder.defineInRange("Charge used by Dash", 60, 1, 32767);
        PyrosweepChargeShieldUse = builder.defineInRange("Charge used by Shield", 10, 1, 32767);
        builder.pop();

        builder.pop();
    }
}
