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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhantomCloak extends ArmorItem {
    public PhantomCloak(Properties pProperties) {
        super(WLArmorMaterials.PHANTOM_MATERIAL, Type.CHESTPLATE,
                pProperties
                        .durability(Type.CHESTPLATE.getDurability(WLAttributeConfig.CONFIG.ItemPhantomCloakDurability.get() / 16))
                        .attributes(PhantomCloak.createAttributes())
                        .rarity(WLEnumExtensions.RARITY_PHANTOM.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
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

    @Override
    public ResourceLocation getArmorTexture(ItemStack pItemStack, Entity pEntity, EquipmentSlot pSlot, ArmorMaterial.Layer pLayer, boolean pInnerModel) {
        return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/phantom_layer_1_cloak.png");
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.phantom_cloak.shift_desc_3", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
