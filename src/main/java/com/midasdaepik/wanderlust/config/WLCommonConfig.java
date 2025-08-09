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

    public WLCommonConfig(ModConfigSpec.Builder builder) {
        builder.push("Misc");
        builder.comment("Tweaks made by this mod for balance.");

        builder.push("Equipping Cooldown");
        builder.comment("Cooldown when equipping an item to prevent quick-swapping armor just to use the ability.");
        EquippingCooldownActive = builder.define("Enabled?", true);
        EquippingCooldownDuration = builder.defineInRange("Cooldown Duration (in Ticks)", 100, 1, 32767);
        builder.pop();

        builder.pop();
    }
}
