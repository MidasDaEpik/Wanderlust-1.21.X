package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLArmorMaterials;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhantomHood extends ArmorItem {
    public PhantomHood(Properties pProperties) {
        super(WLArmorMaterials.PHANTOM_MATERIAL, Type.HELMET,
                pProperties
                        .durability(Type.HELMET.getDurability(WLAttributeConfig.CONFIG.ItemPhantomHoodDurability.get() / 11))
                        .attributes(PhantomHood.createAttributes())
                        .rarity(WLEnumExtensions.RARITY_PHANTOM.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
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

    @Override
    public ResourceLocation getArmorTexture(ItemStack pItemStack, Entity pEntity, EquipmentSlot pSlot, ArmorMaterial.Layer pLayer, boolean pInnerModel) {
        return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_hood.png");
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_hood.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_hood.shift_desc_2"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        ArmorTrim pComponent = pItemStack.get(DataComponents.TRIM);
        if (pItemStack.isEnchanted() || (pComponent != null && pComponent.pattern().isBound())) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
