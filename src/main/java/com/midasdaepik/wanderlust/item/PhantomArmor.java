package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLArmorMaterials;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.client.ClientHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhantomArmor extends ArmorItem {
    public int Piece;

    public PhantomArmor(Properties pProperties, Type pSlot, boolean pSecondary) {
        super(WLArmorMaterials.PHANTOM_MATERIAL, pSlot,
                pProperties
                        .durability(PhantomArmor.getDurability(pSlot, pSecondary))
                        .attributes(PhantomArmor.createAttributes(pSlot, pSecondary))
                        .rarity(WLEnumExtensions.RARITY_PHANTOM.getValue()));

        switch (pSlot) {
            case Type.HELMET -> {
                Piece = 0;
            }
            case Type.CHESTPLATE -> {
                Piece = 4;
            }
        }
    }

    public static int getDurability(Type pSlot, boolean pSecondary) {
        switch (pSlot) {
            case Type.HELMET -> {
                return Type.HELMET.getDurability(WLAttributeConfig.CONFIG.ItemPhantomHoodDurability.get() / 11);
            }
            case Type.CHESTPLATE -> {
                return Type.CHESTPLATE.getDurability(WLAttributeConfig.CONFIG.ItemPhantomCloakDurability.get() / 16);
            }
        }
        return 0;
    }

    public static @NotNull ItemAttributeModifiers createAttributes(Type pSlot, boolean pSecondary) {
        switch (pSlot) {
            case Type.HELMET -> {
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
            case Type.CHESTPLATE -> {
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
    public ResourceLocation getArmorTexture(ItemStack pItemStack, Entity pEntity, EquipmentSlot pSlot, ArmorMaterial.Layer pLayer, boolean pInnerModel) {
        switch (Piece) {
            case 0 -> {
                return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_hood.png");
            }
            case 4 -> {
                return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_cloak.png");
            }
        }
        return ClientHooks.getArmorTexture(pEntity, pItemStack, pLayer, pInnerModel, pSlot);
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        switch (Piece) {
            case 0 -> {
                if (WLUtil.ItemKeys.isHoldingShift()) {
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_hood.shift_desc_1"));
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_hood.shift_desc_2"));
                } else {
                    pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
                }
            }
            case 4 -> {
                if (WLUtil.ItemKeys.isHoldingShift()) {
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_1"));
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_2"));
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_3", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
                } else {
                    pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
                }
            }
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
