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

    public final ModConfigSpec.BooleanValue ItemTridentChangesEnabled;
    public final ModConfigSpec.DoubleValue ItemTridentAttackDamage;
    public final ModConfigSpec.DoubleValue ItemTridentAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemTridentEntityInteractionRange;
    public final ModConfigSpec.DoubleValue ItemTridentAttackKnockback;

    public final ModConfigSpec.BooleanValue ItemMaceChangesEnabled;
    public final ModConfigSpec.DoubleValue ItemMaceAttackDamage;
    public final ModConfigSpec.DoubleValue ItemMaceAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemMaceFallDamageMultiplier;

    public WLCommonConfig(ModConfigSpec.Builder builder) {
        builder.push("Items");
        builder.comment("Changes to item attributes, requires a game restart to apply.");

        builder.push("Trident");
        builder.comment("Rebalances the trident giving it extra damage, reach and knockback.");
        builder.comment("May conflict with other mods that modify the trident's attributes, disable if so.");
        ItemTridentChangesEnabled = builder.define("Trident Changes Enabled?", true);
        builder.comment("Vanilla Trident Attributes: 9 Attack Damage, 1.1 Attack Speed");
        ItemTridentAttackDamage = builder.defineInRange("Trident Attack Damage", 10d, -32767d, 32767d);
        ItemTridentAttackSpeed = builder.defineInRange("Trident Attack Speed", 1.1d, -32767d, 32767d);
        ItemTridentEntityInteractionRange = builder.defineInRange("Trident Entity Reach (Add Value)", 1.5d, -32767d, 32767d);
        ItemTridentAttackKnockback = builder.defineInRange("Trident Attack Knockback (Add Value)", 1.0d, -32767d, 32767d);
        builder.pop();

        builder.push("Mace");
        builder.comment("Rebalances the trident giving it extra reach and attack speed.");
        builder.comment("May conflict with other mods that modify the mace's attributes, disable if so.");
        ItemMaceChangesEnabled = builder.define("Mace Changes Enabled?", true);
        builder.comment("Vanilla Mace Attributes: 6 Attack Damage, 0.6 Attack Speed");
        ItemMaceAttackDamage = builder.defineInRange("Mace Attack Damage", 6d, -32767d, 32767d);
        ItemMaceAttackSpeed = builder.defineInRange("Mace Attack Speed", 0.6d, -32767d, 32767d);
        ItemMaceFallDamageMultiplier = builder.defineInRange("Mace Fall Damage Multiplier (Add Multiplied Total)", 0.75d, -32767d, 32767d);
        builder.pop();

        builder.pop();
    }
}
