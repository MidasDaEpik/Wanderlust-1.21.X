package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLArmorMaterials;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    public ResourceLocation getArmorTexture(ItemStack pItemStack, Entity pEntity, EquipmentSlot pSlot, ArmorMaterial.Layer pLayer, boolean pInnerModel) {
        switch (Piece) {
            case HOOD -> {
                return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_hood.png");
            }
            case CLOAK -> {
                return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_cloak.png");
            }
        }
        return pLayer.texture(pInnerModel);
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            if (Piece.isCloak()) {
                pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_1"));
                pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_2"));
            } else {
                if (FMLLoader.getDist().isClient()) {
                    clientDynamicTooltip(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
                } else {
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_1"));
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_2"));
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_3"));
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_4"));
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_5", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))), Component.literal("60").withStyle(ChatFormatting.DARK_GRAY)));
                }
            }
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }

    @OnlyIn(Dist.CLIENT)
    public void clientDynamicTooltip(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        Player pPlayer = Minecraft.getInstance().player;

        if (pPlayer != null) {
            if (pPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.PHANTOM_CLOAK.get()) {
                pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_1"));
                pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_2"));
            } else {
                int pCooldown = 70;
                int pPieces = 0;
                if (pPlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() == WLItems.PHANTOM_HOOD.get()) {
                    pCooldown -= 9;
                    pPieces += 1;
                }
                if (pPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.PHANTOM_TUNIC.get()) {
                    pCooldown -= 16;
                    pPieces += 1;
                }
                if (pPlayer.getItemBySlot(EquipmentSlot.LEGS).getItem() == WLItems.PHANTOM_LEGGINGS.get()) {
                    pCooldown -= 16;
                    pPieces += 1;
                }
                if (pPlayer.getItemBySlot(EquipmentSlot.FEET).getItem() == WLItems.PHANTOM_BOOTS.get()) {
                    pCooldown -= 9;
                    pPieces += 1;
                }

                pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_1"));
                pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_2"));
                pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_3"));
                pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_4"));
                if (pPieces >= 2) {
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_5", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))), Component.literal(String.valueOf(pCooldown)).withStyle(ChatFormatting.DARK_GRAY)));
                } else {
                    pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_5_inactive", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
                }
            }
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_armor.shift_desc_5", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))), Component.literal("60").withStyle(ChatFormatting.DARK_GRAY)));
        }
    }
}
