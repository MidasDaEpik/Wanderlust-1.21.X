package com.midasdaepik.wanderlust.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class WLTweaksConfig {
    public static final WLTweaksConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    static {
        Pair<WLTweaksConfig, ModConfigSpec> pPair = new ModConfigSpec.Builder().configure(WLTweaksConfig::new);
        CONFIG = pPair.getLeft();
        CONFIG_SPEC = pPair.getRight();
    }

    public final ModConfigSpec.IntValue TridentInnateLoyalty;
    public final ModConfigSpec.BooleanValue TridentNoDespawn;
    public final ModConfigSpec.BooleanValue TridentVoidSaving;

    public final ModConfigSpec.BooleanValue PhantomDisappear;
    public final ModConfigSpec.BooleanValue PhantomPhantasmal;
    public final ModConfigSpec.BooleanValue PhantomSpawningReworkToggle;
    public final ModConfigSpec.BooleanValue PhantomDoNotTargetLowInsomnia;
    public final ModConfigSpec.BooleanValue PhantomInsomniaPreventsSleeping;

    public final ModConfigSpec.BooleanValue AttributeSwappingFix;

    public final ModConfigSpec.BooleanValue AttackSpeedInvulnerabilityFix;

    public final ModConfigSpec.BooleanValue EquippingCooldownActive;
    public final ModConfigSpec.IntValue EquippingCooldownDuration;
    public final ModConfigSpec.BooleanValue EquippingCooldownPreventChange;
    public final ModConfigSpec.BooleanValue EquippingCooldownAllArmor;

    public final ModConfigSpec.BooleanValue EnchantmentFrostWalkerWalkOnPowderSnow;

    public final ModConfigSpec.BooleanValue HasteMiningFatigueTweak;

    public final ModConfigSpec.BooleanValue StrengthWeaknessRebalance;
    public final ModConfigSpec.DoubleValue StrengthPercentModifier;
    public final ModConfigSpec.DoubleValue WeaknessPercentModifier;

    public WLTweaksConfig(ModConfigSpec.Builder builder) {
        builder.push("Items");
        builder.comment("Tweaks made by this mod to vanilla items to balance them with the mod's items.");

        builder.push("Trident");
        builder.comment("Adds a level of Loyalty to unenchanted Tridents so they are usable without having to enchant them first.");
        TridentInnateLoyalty = builder.defineInRange("Trident Innate Loyalty? (A value of 0 disables this feature)", 1, 0, 127);
        TridentNoDespawn = builder.define("Player thrown Tridents don't Despawn?", true);
        builder.comment("Makes Tridents return to the thrower when in the void.");
        TridentVoidSaving = builder.define("Trident Void Saving?", true);
        builder.pop();

        builder.pop();

        builder.push("World");
        builder.comment("Tweaks made by this mod to the world.");

        builder.push("Phantoms");
        builder.comment("Makes Phantoms disappear instead of dying during the day.");
        PhantomDisappear = builder.define("Phantom Disappear In Day?", true);
        builder.comment("Phantoms go invisible when flying back up to circle, they also take 2x damage when invisible.");
        PhantomPhantasmal = builder.define("Phantom Phantasmal when Retreating?", true);
        builder.comment("Changes Phantom spawning mechanics to be more of a punishment for explorers skipping the night rather than for those at base.");
        PhantomSpawningReworkToggle = builder.define("Phantom Spawning Rework Toggle", true);
        builder.comment("Phantoms do not target players at a low level of Insomnia unless hit first, they are also translucent (Requires Phantom Spawning Rework Toggle = true).");
        PhantomDoNotTargetLowInsomnia = builder.define("Phantom Do Not Target Low Insomnia?", true);
        builder.comment("High insomnia prevents sleeping (Requires Phantom Spawning Rework Toggle = true).");
        PhantomInsomniaPreventsSleeping = builder.define("High Insomnia Prevents Sleeping?", true);
        builder.pop();

        builder.pop();

        builder.push("Misc");
        builder.comment("Tweaks made by this mod for balance in vanilla features.");

        builder.push("Attribute Swapping Fix");
        builder.comment("Attribute swapping is a technique where a player switches between two items exactly one game tick before attacking to use the attributes of one on the other.");
        AttributeSwappingFix = builder.define("Fix Attribute Swapping?", false);
        builder.pop();

        builder.push("Attack Speed Invulnerability Fix");
        builder.comment("Reduces invulnerability ticks applied if the player's attack cooldown refreshes before the invulnerability runs out.");
        AttackSpeedInvulnerabilityFix = builder.define("Fix Attack Speed Invulnerability?", true);
        builder.pop();

        builder.push("Equipping Cooldown");
        builder.comment("Cooldown when equipping an item to prevent quick-swapping armor just to use the ability.");
        EquippingCooldownActive = builder.define("Enabled?", true);
        EquippingCooldownDuration = builder.defineInRange("Cooldown Duration (in Ticks)", 100, 1, 32767);
        EquippingCooldownPreventChange = builder.define("Prevents Armor on Cooldown from being Unequipped", false);
        EquippingCooldownAllArmor = builder.define("Instead of using a Tag, puts all Armor on cooldown when Equipped", false);
        builder.pop();

        builder.push("Enchantment Tweaks");
        EnchantmentFrostWalkerWalkOnPowderSnow = builder.define("Frost Walker can Walk on Powder Snow?", true);
        builder.pop();

        builder.push("Haste and Mining Fatigue Tweak");
        builder.comment("Removes the Attack Speed modifiers from Haste and Mining Fatigue.");
        HasteMiningFatigueTweak = builder.define("Haste and Mining Fatigue Tweak?", false);
        builder.pop();

        builder.push("Strength and Weakness Rebalance");
        builder.comment("Changes Strength and Weakness to become percentage modifiers to damage.");
        StrengthWeaknessRebalance = builder.define("Strength and Weakness Rebalance?", false);
        StrengthPercentModifier = builder.defineInRange("Strength Percent Modifier", 0.3d, -32767, 32767);
        WeaknessPercentModifier = builder.defineInRange("Weakness Percent Modifier", -0.3d, -32767, 32767);
        builder.pop();

        builder.pop();
    }
}
