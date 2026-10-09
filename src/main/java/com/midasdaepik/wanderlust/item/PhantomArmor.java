package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.registries.WLArmorMaterials;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

public class PhantomArmor extends ArmorItem {
    public enum PhantomPiece {
        HOOD(Type.HELMET, EquipmentSlot.HEAD),
        TUNIC(Type.CHESTPLATE, EquipmentSlot.CHEST),
        LEGGINGS(Type.LEGGINGS, EquipmentSlot.LEGS),
        BOOTS(Type.BOOTS, EquipmentSlot.FEET),
        CLOAK(Type.CHESTPLATE, EquipmentSlot.CHEST);

        public final ArmorItem.Type armorType;
        public final EquipmentSlot equipmentSlot;

        PhantomPiece(ArmorItem.Type pArmorType, EquipmentSlot pSlot) {
            this.armorType = pArmorType;
            this.equipmentSlot = pSlot;
        }

        public boolean isCloak() {
            return this == CLOAK;
        }
    }

    public PhantomPiece Piece;

    public PhantomArmor(Properties pProperties, PhantomPiece pPiece) {
        super(WLArmorMaterials.PHANTOM_MATERIAL, pPiece.armorType,
                pProperties
                        .durability(pPiece.armorType.getDurability(WLAttributeConfig.CONFIG.ItemPhantomArmorDurabilityFactor.get()))
                        .attributes(PhantomArmor.createAttributes(pPiece))
                        .rarity(WLEnumExtensions.RARITY_PHANTOM.getValue()));
        Piece = pPiece;
    }

    public static @NotNull ItemAttributeModifiers createAttributes(PhantomPiece pPiece) {
        switch (pPiece) {
            case HOOD -> {
                return ItemAttributeModifiers.builder()
                        .add(Attributes.ARMOR,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.phantom_hood"), WLAttributeConfig.CONFIG.ItemPhantomHoodArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD)
                        .add(Attributes.ARMOR_TOUGHNESS,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor_toughness.phantom_hood"), WLAttributeConfig.CONFIG.ItemPhantomHoodArmorToughness.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD)
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.phantom_hood"), WLAttributeConfig.CONFIG.ItemPhantomHoodMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                                EquipmentSlotGroup.HEAD)
                        .add(Attributes.SAFE_FALL_DISTANCE,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "safe_fall_distance.phantom_hood"), WLAttributeConfig.CONFIG.ItemPhantomHoodSafeFallDistance.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD)
                        .build();
            }
            case TUNIC -> {
                return ItemAttributeModifiers.builder()
                        .add(Attributes.ARMOR,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.phantom_tunic"), WLAttributeConfig.CONFIG.ItemPhantomTunicArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.CHEST)
                        .add(Attributes.ARMOR_TOUGHNESS,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor_toughness.phantom_tunic"), WLAttributeConfig.CONFIG.ItemPhantomTunicArmorToughness.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.CHEST)
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.phantom_tunic"), WLAttributeConfig.CONFIG.ItemPhantomTunicMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                                EquipmentSlotGroup.CHEST)
                        .add(Attributes.SAFE_FALL_DISTANCE,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "safe_fall_distance.phantom_tunic"), WLAttributeConfig.CONFIG.ItemPhantomTunicSafeFallDistance.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.CHEST)
                        .build();
            }
            case LEGGINGS -> {
                return ItemAttributeModifiers.builder()
                        .add(Attributes.ARMOR,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.phantom_leggings"), WLAttributeConfig.CONFIG.ItemPhantomLeggingsArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.LEGS)
                        .add(Attributes.ARMOR_TOUGHNESS,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor_toughness.phantom_leggings"), WLAttributeConfig.CONFIG.ItemPhantomLeggingsArmorToughness.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.LEGS)
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.phantom_leggings"), WLAttributeConfig.CONFIG.ItemPhantomLeggingsMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                                EquipmentSlotGroup.LEGS)
                        .add(Attributes.SAFE_FALL_DISTANCE,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "safe_fall_distance.phantom_leggings"), WLAttributeConfig.CONFIG.ItemPhantomLeggingsSafeFallDistance.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.LEGS)
                        .build();
            }
            case BOOTS -> {
                return ItemAttributeModifiers.builder()
                        .add(Attributes.ARMOR,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.phantom_boots"), WLAttributeConfig.CONFIG.ItemPhantomBootsArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.FEET)
                        .add(Attributes.ARMOR_TOUGHNESS,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor_toughness.phantom_boots"), WLAttributeConfig.CONFIG.ItemPhantomBootsArmorToughness.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.FEET)
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.phantom_boots"), WLAttributeConfig.CONFIG.ItemPhantomBootsMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                                EquipmentSlotGroup.FEET)
                        .add(Attributes.SAFE_FALL_DISTANCE,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "safe_fall_distance.phantom_boots"), WLAttributeConfig.CONFIG.ItemPhantomBootsSafeFallDistance.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.FEET)
                        .build();
            }
            case CLOAK -> {
                return ItemAttributeModifiers.builder()
                        .add(Attributes.ARMOR,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.phantom_cloak"), WLAttributeConfig.CONFIG.ItemPhantomCloakArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.CHEST)
                        .add(Attributes.ARMOR_TOUGHNESS,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor_toughness.phantom_cloak"), WLAttributeConfig.CONFIG.ItemPhantomCloakArmorToughness.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.CHEST)
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.phantom_cloak"), WLAttributeConfig.CONFIG.ItemPhantomCloakMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                                EquipmentSlotGroup.CHEST)
                        .add(Attributes.SAFE_FALL_DISTANCE,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "safe_fall_distance.phantom_cloak"), WLAttributeConfig.CONFIG.ItemPhantomCloakSafeFallDistance.get(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.CHEST)
                        .build();
            }
        }
        return null;
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return Piece == PhantomPiece.BOOTS;
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack pItemStack, Entity pEntity, EquipmentSlot pSlot, ArmorMaterial.Layer pLayer, boolean pInnerModel) {
        if (DyedItemColor.getOrDefault(pItemStack, 0) == 0 && pLayer.dyeable()) {
            return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_empty.png");
        }
        switch (Piece) {
            case HOOD -> {
                if (pLayer.dyeable()) {
                    return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_hood_overlay.png");
                } else {
                    return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_hood.png");
                }
            }
            case CLOAK -> {
                if (pLayer.dyeable()) {
                    return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_cloak_overlay.png");
                } else {
                    return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_cloak.png");
                }
            }
        }
        return null;
    }
}
