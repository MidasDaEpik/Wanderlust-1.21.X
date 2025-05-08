package com.midasdaepik.wanderlust.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class WLAttributeConfig {
    public static final WLAttributeConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    static {
        Pair<WLAttributeConfig, ModConfigSpec> pPair = new ModConfigSpec.Builder().configure(WLAttributeConfig::new);
        CONFIG = pPair.getLeft();
        CONFIG_SPEC = pPair.getRight();
    }

    public final ModConfigSpec.DoubleValue ItemBlazeReapAttackDamage;
    public final ModConfigSpec.DoubleValue ItemBlazeReapAttackSpeed;
    public final ModConfigSpec.IntValue ItemBlazeReapDurability;

    public final ModConfigSpec.IntValue ItemCatalystChaliceMaxExp;

    public final ModConfigSpec.DoubleValue ItemCharybdisAttackDamage;
    public final ModConfigSpec.DoubleValue ItemCharybdisAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemCharybdisSweepingDamageRatio;
    public final ModConfigSpec.DoubleValue ItemCharybdisSubmergedMiningSpeed;
    public final ModConfigSpec.IntValue ItemCharybdisDurability;

    public final ModConfigSpec.DoubleValue ItemCutlassAttackDamage;
    public final ModConfigSpec.DoubleValue ItemCutlassAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemCutlassSweepingDamageRatio;
    public final ModConfigSpec.IntValue ItemCutlassDurability;

    public final ModConfigSpec.DoubleValue ItemDragonsRageAttackDamage;
    public final ModConfigSpec.DoubleValue ItemDragonsRageAttackSpeed;
    public final ModConfigSpec.IntValue ItemDragonsRageDurability;

    public final ModConfigSpec.DoubleValue ItemElderChestplateArmor;
    public final ModConfigSpec.DoubleValue ItemElderChestplateArmorToughness;
    public final ModConfigSpec.DoubleValue ItemElderChestplateMiningSpeed;
    public final ModConfigSpec.DoubleValue ItemElderChestplateWaterMovementEfficiency;
    public final ModConfigSpec.IntValue ItemElderChestplateDurability;

    public final ModConfigSpec.DoubleValue ItemFirestormKatanaAttackDamage;
    public final ModConfigSpec.DoubleValue ItemFirestormKatanaAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemFirestormKatanaEntityInteractionRange;
    public final ModConfigSpec.IntValue ItemFirestormKatanaDurability;

    public final ModConfigSpec.DoubleValue ItemMoltenPickaxeAttackDamage;
    public final ModConfigSpec.DoubleValue ItemMoltenPickaxeAttackSpeed;
    public final ModConfigSpec.IntValue ItemMoltenPickaxeDurability;

    public final ModConfigSpec.DoubleValue ItemMycorisAttackDamage;
    public final ModConfigSpec.DoubleValue ItemMycorisAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemMycorisEntityInteractionRange;
    public final ModConfigSpec.IntValue ItemMycorisDurability;

    public final ModConfigSpec.DoubleValue ItemObsidianBulwarkAttackDamage;
    public final ModConfigSpec.DoubleValue ItemObsidianBulwarkAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemObsidianBulwarkArmor;
    public final ModConfigSpec.DoubleValue ItemObsidianBulwarkKnockbackResistance;
    public final ModConfigSpec.IntValue ItemObsidianBulwarkDurability;

    public final ModConfigSpec.DoubleValue ItemPhantomCloakArmor;
    public final ModConfigSpec.DoubleValue ItemPhantomCloakArmorToughness;
    public final ModConfigSpec.DoubleValue ItemPhantomCloakMovementSpeed;
    public final ModConfigSpec.DoubleValue ItemPhantomCloakSafeFallDistance;
    public final ModConfigSpec.IntValue ItemPhantomCloakDurability;

    public final ModConfigSpec.DoubleValue ItemPhantomHoodArmor;
    public final ModConfigSpec.DoubleValue ItemPhantomHoodArmorToughness;
    public final ModConfigSpec.DoubleValue ItemPhantomHoodMovementSpeed;
    public final ModConfigSpec.DoubleValue ItemPhantomHoodSafeFallDistance;
    public final ModConfigSpec.IntValue ItemPhantomHoodDurability;

    public final ModConfigSpec.DoubleValue ItemPiglinWaraxeAttackDamage;
    public final ModConfigSpec.DoubleValue ItemPiglinWaraxeAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemPiglinWaraxeKnockbackResistance;
    public final ModConfigSpec.IntValue ItemPiglinWaraxeDurability;

    public final ModConfigSpec.DoubleValue ItemPyrosweepAttackDamage;
    public final ModConfigSpec.DoubleValue ItemPyrosweepAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemPyrosweepBurnTime;
    public final ModConfigSpec.DoubleValue ItemPyrosweepStepHeight;
    public final ModConfigSpec.IntValue ItemPyrosweepDurability;

    public final ModConfigSpec.DoubleValue ItemRefinedWitherbladeAttackDamage;
    public final ModConfigSpec.DoubleValue ItemRefinedWitherbladeAttackSpeed;
    public final ModConfigSpec.IntValue ItemRefinedWitherbladeDurability;

    public final ModConfigSpec.DoubleValue ItemScyllaAttackDamage;
    public final ModConfigSpec.DoubleValue ItemScyllaAttackSpeed;
    public final ModConfigSpec.IntValue ItemScyllaDurability;

    public final ModConfigSpec.DoubleValue ItemSoulgorgeAttackDamage;
    public final ModConfigSpec.DoubleValue ItemSoulgorgeAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemSoulgorgeArmor;
    public final ModConfigSpec.DoubleValue ItemSoulgorgeKnockbackResistance;
    public final ModConfigSpec.IntValue ItemSoulgorgeDurability;

    public final ModConfigSpec.DoubleValue ItemWarpedRapierAttackDamage;
    public final ModConfigSpec.DoubleValue ItemWarpedRapierAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemWarpedRapierMovementSpeed;
    public final ModConfigSpec.IntValue ItemWarpedRapierDurability;

    public final ModConfigSpec.DoubleValue ItemWarpthistleAttackDamage;
    public final ModConfigSpec.DoubleValue ItemWarpthistleAttackSpeed;
    public final ModConfigSpec.DoubleValue ItemWarpthistleMovementSpeed;
    public final ModConfigSpec.IntValue ItemWarpthistleDurability;

    public final ModConfigSpec.DoubleValue ItemWhisperwindMovementSpeed;

    public final ModConfigSpec.DoubleValue ItemWitherbladeAttackDamage;
    public final ModConfigSpec.DoubleValue ItemWitherbladeAttackSpeed;
    public final ModConfigSpec.IntValue ItemWitherbladeDurability;

    public WLAttributeConfig(ModConfigSpec.Builder builder) {
        builder.push("Items");
        builder.comment("Changes to item attributes, requires a game restart to apply.");

        builder.push("Blaze Reap");
        ItemBlazeReapAttackDamage = builder.defineInRange("Blaze Reap Attack Damage", 7d, -32767d, 32767d);
        ItemBlazeReapAttackSpeed = builder.defineInRange("Blaze Reap Attack Speed", 1.2d, -32767d, 32767d);
        ItemBlazeReapDurability = builder.defineInRange("Blaze Reap Durability", 1780, 1, 2147483647);
        builder.pop();

        builder.push("Catalyst Chalice");
        ItemCatalystChaliceMaxExp = builder.comment("In Experience Points, 1395 Points is 30 Levels")
                .defineInRange("Catalyst Chalice Experience Capacity", 1395, 1, 2147483647);
        builder.pop();

        builder.push("Charybdis");
        ItemCharybdisAttackDamage = builder.defineInRange("Charybdis Attack Damage", 9d, -32767d, 32767d);
        ItemCharybdisAttackSpeed = builder.defineInRange("Charybdis Attack Speed", 1.2d, -32767d, 32767d);
        ItemCharybdisSweepingDamageRatio = builder.defineInRange("Charybdis Sweeping Damage Ratio (Add Value)", 0.4d, -32767d, 32767d);
        ItemCharybdisSubmergedMiningSpeed = builder.defineInRange("Charybdis Submerged Mining Speed (Add Multiplied Total)", 2d, -32767d, 32767d);
        ItemCharybdisDurability = builder.defineInRange("Charybdis Durability", 1796, 1, 2147483647);
        builder.pop();

        builder.push("Cutlass");
        ItemCutlassAttackDamage = builder.defineInRange("Cutlass Attack Damage", 5d, -32767d, 32767d);
        ItemCutlassAttackSpeed = builder.defineInRange("Cutlass Attack Speed", 2d, -32767d, 32767d);
        ItemCutlassSweepingDamageRatio = builder.defineInRange("Cutlass Sweeping Damage Ratio (Add Value)", 0.4d, -32767d, 32767d);
        ItemCutlassDurability = builder.defineInRange("Cutlass Durability", 800, 1, 2147483647);
        builder.pop();

        builder.push("Dragon's Rage");
        ItemDragonsRageAttackDamage = builder.defineInRange("Dragon's Rage Attack Damage", 7d, -32767d, 32767d);
        ItemDragonsRageAttackSpeed = builder.defineInRange("Dragon's Rage Attack Speed", 1.6d, -32767d, 32767d);
        ItemDragonsRageDurability = builder.defineInRange("Dragon's Rage Durability", 2235, 1, 2147483647);
        builder.pop();

        builder.push("Elder Chestplate");
        ItemElderChestplateArmor = builder.defineInRange("Elder Chestplate Armor", 6d, -32767d, 32767d);
        ItemElderChestplateArmorToughness = builder.defineInRange("Elder Chestplate Armor Toughness", 1.5d, -32767d, 32767d);
        ItemElderChestplateMiningSpeed = builder.defineInRange("Elder Chestplate Mining Speed (Add Value)", 6d, -32767d, 32767d);
        ItemElderChestplateWaterMovementEfficiency = builder.defineInRange("Elder Chestplate Water Movement Efficiency (Add Value)", 0.4d, -32767d, 32767d);
        ItemElderChestplateDurability = builder.defineInRange("Elder Chestplate Durability", 560, 1, 2147483647);
        builder.pop();

        builder.push("Firestorm Katana");
        ItemFirestormKatanaAttackDamage = builder.defineInRange("Firestorm Katana Attack Damage", 5d, -32767d, 32767d);
        ItemFirestormKatanaAttackSpeed = builder.defineInRange("Firestorm Katana Attack Speed", 1.4d, -32767d, 32767d);
        ItemFirestormKatanaEntityInteractionRange = builder.defineInRange("Firestorm Katana Entity Reach (Add Value)", 1d, -32767d, 32767d);
        ItemFirestormKatanaDurability = builder.defineInRange("Firestorm Katana Durability", 560, 1, 2147483647);
        builder.pop();

        builder.push("Molten Pickaxe");
        ItemMoltenPickaxeAttackDamage = builder.defineInRange("Molten Pickaxe Attack Damage", 5d, -32767d, 32767d);
        ItemMoltenPickaxeAttackSpeed = builder.defineInRange("Molten Pickaxe Attack Speed", 1.2d, -32767d, 32767d);
        ItemMoltenPickaxeDurability = builder.defineInRange("Molten Pickaxe Durability", 1040, 1, 2147483647);
        builder.pop();

        builder.push("Mycoris");
        ItemMycorisAttackDamage = builder.defineInRange("Mycoris Attack Damage", 8d, -32767d, 32767d);
        ItemMycorisAttackSpeed = builder.defineInRange("Mycoris Attack Speed", 1.4d, -32767d, 32767d);
        ItemMycorisEntityInteractionRange = builder.defineInRange("Mycoris Entity Reach (Add Value)", 1d, -32767d, 32767d);
        ItemMycorisDurability = builder.defineInRange("Mycoris Durability", 1777, 1, 2147483647);
        builder.pop();

        builder.push("Obsidian Bulwark");
        ItemObsidianBulwarkAttackDamage = builder.defineInRange("Obsidian Bulwark Attack Damage", 8d, -32767d, 32767d);
        ItemObsidianBulwarkAttackSpeed = builder.defineInRange("Obsidian Bulwark Attack Speed", 1.2d, -32767d, 32767d);
        ItemObsidianBulwarkArmor = builder.defineInRange("Obsidian Bulwark Armor (Add Value)", 4d, -32767d, 32767d);
        ItemObsidianBulwarkKnockbackResistance = builder.defineInRange("Obsidian Bulwark Knockback Resistance (Add Value)", 0.3d, -32767d, 32767d);
        ItemObsidianBulwarkDurability = builder.defineInRange("Obsidian Bulwark Durability", 563, 1, 2147483647);
        builder.pop();

        builder.push("Phantom Cloak");
        ItemPhantomCloakArmor = builder.defineInRange("Phantom Cloak Armor", 6d, -32767d, 32767d);
        ItemPhantomCloakArmorToughness = builder.defineInRange("Phantom Cloak Armor Toughness", 1.5d, -32767d, 32767d);
        ItemPhantomCloakMovementSpeed = builder.defineInRange("Phantom Cloak Movement Speed (Add Multiplied Total)", 0.2d, -32767d, 32767d);
        ItemPhantomCloakSafeFallDistance = builder.defineInRange("Phantom Cloak Safe Fall Distance (Add Value)", 5d, -32767d, 32767d);
        ItemPhantomCloakDurability = builder.defineInRange("Phantom Cloak Durability", 400, 1, 2147483647);
        builder.pop();

        builder.push("Phantom Hood");
        ItemPhantomHoodArmor = builder.defineInRange("Phantom Hood Armor", 2d, -32767d, 32767d);
        ItemPhantomHoodArmorToughness = builder.defineInRange("Phantom Hood Armor Toughness", 1d, -32767d, 32767d);
        ItemPhantomHoodMovementSpeed = builder.defineInRange("Phantom Hood Movement Speed (Add Multiplied Total)", 0.2d, -32767d, 32767d);
        ItemPhantomHoodSafeFallDistance = builder.defineInRange("Phantom Hood Safe Fall Distance (Add Value)", 5d, -32767d, 32767d);
        ItemPhantomHoodDurability = builder.defineInRange("Phantom Hood Durability", 275, 1, 2147483647);
        builder.pop();

        builder.push("Piglin Waraxe");
        ItemPiglinWaraxeAttackDamage = builder.defineInRange("Piglin Waraxe Attack Damage", 10d, -32767d, 32767d);
        ItemPiglinWaraxeAttackSpeed = builder.defineInRange("Piglin Waraxe Attack Speed", 1d, -32767d, 32767d);
        ItemPiglinWaraxeKnockbackResistance = builder.defineInRange("Piglin Waraxe Knockback Resistance (Add Value)", 0.4d, -32767d, 32767d);
        ItemPiglinWaraxeDurability = builder.defineInRange("Piglin Waraxe Durability", 760, 1, 2147483647);
        builder.pop();

        builder.push("Pyrosweep");
        ItemPyrosweepAttackDamage = builder.defineInRange("Pyrosweep Attack Damage", 8d, -32767d, 32767d);
        ItemPyrosweepAttackSpeed = builder.defineInRange("Pyrosweep Attack Speed", 1.2d, -32767d, 32767d);
        ItemPyrosweepBurnTime = builder.defineInRange("Pyrosweep Burn Time (Add Multiplied Total)", -0.5d, -32767d, 32767d);
        ItemPyrosweepStepHeight = builder.defineInRange("Pyrosweep Step Height (Add Value)", 0.5d, -32767d, 32767d);
        ItemPyrosweepDurability = builder.defineInRange("Pyrosweep Durability", 1270, 1, 2147483647);
        builder.pop();

        builder.push("Refined Witherblade");
        ItemRefinedWitherbladeAttackDamage = builder.defineInRange("Refined Witherblade Attack Damage", 6.5d, -32767d, 32767d);
        ItemRefinedWitherbladeAttackSpeed = builder.defineInRange("Refined Witherblade Attack Speed", 1.6d, -32767d, 32767d);
        ItemRefinedWitherbladeDurability = builder.defineInRange("Refined Witherblade Durability", 1207, 1, 2147483647);
        builder.pop();

        builder.push("Scylla");
        ItemScyllaAttackDamage = builder.defineInRange("Scylla Attack Damage", 6.5d, -32767d, 32767d);
        ItemScyllaAttackSpeed = builder.defineInRange("Scylla Attack Speed", 1.6d, -32767d, 32767d);
        ItemScyllaDurability = builder.defineInRange("Scylla Durability", 1688, 1, 2147483647);
        builder.pop();

        builder.push("Soulgorge");
        ItemSoulgorgeAttackDamage = builder.defineInRange("Soulgorge Attack Damage", 11d, -32767d, 32767d);
        ItemSoulgorgeAttackSpeed = builder.defineInRange("Soulgorge Attack Speed", 0.8d, -32767d, 32767d);
        ItemSoulgorgeArmor = builder.defineInRange("Soulgorge Armor (Add Value)", 6d, -32767d, 32767d);
        ItemSoulgorgeKnockbackResistance = builder.defineInRange("Soulgorge Knockback Resistance (Add Value)", 0.3d, -32767d, 32767d);
        ItemSoulgorgeDurability = builder.defineInRange("Soulgorge Durability", 1080, 1, 2147483647);
        builder.pop();

        builder.push("Warped Rapier");
        ItemWarpedRapierAttackDamage = builder.defineInRange("Warped Rapier Attack Damage", 5.5d, -32767d, 32767d);
        ItemWarpedRapierAttackSpeed = builder.defineInRange("Warped Rapier Attack Speed", 1.8d, -32767d, 32767d);
        ItemWarpedRapierMovementSpeed = builder.defineInRange("Warped Rapier Movement Speed (Add Multiplied Total)", 0.15d, -32767d, 32767d);
        ItemWarpedRapierDurability = builder.defineInRange("Warped Rapier Durability", 641, 1, 2147483647);
        builder.pop();

        builder.push("Warpthistle");
        ItemWarpthistleAttackDamage = builder.defineInRange("Warpthistle Attack Damage", 6.5d, -32767d, 32767d);
        ItemWarpthistleAttackSpeed = builder.defineInRange("Warpthistle Attack Speed", 1.8d, -32767d, 32767d);
        ItemWarpthistleMovementSpeed = builder.defineInRange("Warpthistle Movement Speed (Add Multiplied Total)", 0.25d, -32767d, 32767d);
        ItemWarpthistleDurability = builder.defineInRange("Warpthistle Durability", 2285, 1, 2147483647);
        builder.pop();

        builder.push("Whisperwind");
        ItemWhisperwindMovementSpeed = builder.defineInRange("Whisperwind Movement Speed (Add Multiplied Total)", 0.2d, -32767d, 32767d);
        builder.pop();

        builder.push("Witherblade");
        ItemWitherbladeAttackDamage = builder.defineInRange("Witherblade Attack Damage", 5.5d, -32767d, 32767d);
        ItemWitherbladeAttackSpeed = builder.defineInRange("Witherblade Attack Speed", 1.6d, -32767d, 32767d);
        ItemWitherbladeDurability = builder.defineInRange("Witherblade Durability", 604, 1, 2147483647);
        builder.pop();

        builder.pop();
    }
}
