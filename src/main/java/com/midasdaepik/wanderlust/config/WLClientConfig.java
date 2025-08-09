package com.midasdaepik.wanderlust.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class WLClientConfig {
    public static final WLClientConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    static {
        Pair<WLClientConfig, ModConfigSpec> pPair = new ModConfigSpec.Builder().configure(WLClientConfig::new);
        CONFIG = pPair.getLeft();
        CONFIG_SPEC = pPair.getRight();
    }

    public final ModConfigSpec.IntValue ChargeBarRendering;

    public WLClientConfig(ModConfigSpec.Builder builder) {
        builder.push("Client");
        builder.comment("Changes how this mod displays on each player's client.");

        builder.push("Charge Bar Rendering");
        builder.comment("Changes how weapon charge bars display on the client.");
        builder.comment("[0] - Displays as an Icon above the Experience Bar");
        builder.comment("[1] - Displays as a Bar above the Crosshair | Low Saturation Bar");
        builder.comment("[2] - Displays as a Bar above the Crosshair | High Saturation Bar");
        ChargeBarRendering = builder.defineInRange("Charge Bar Rendering", 0, 0, 2);
        builder.pop();

        builder.pop();
    }
}
