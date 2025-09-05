package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLArmorMaterials;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ElderChestplate extends ArmorItem {
    public ElderChestplate(Properties pProperties) {
        super(WLArmorMaterials.ELDER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                pProperties
                        .durability(ArmorItem.Type.CHESTPLATE.getDurability(WLAttributeConfig.CONFIG.ItemElderChestplateDurability.get() / 16))
                        .attributes(ElderChestplate.createAttributes())
                        .rarity(WLEnumExtensions.RARITY_ELDER.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.elder_chestplate"), WLAttributeConfig.CONFIG.ItemElderChestplateArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.CHEST)
                .add(Attributes.ARMOR_TOUGHNESS,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor_toughness.elder_chestplate"), WLAttributeConfig.CONFIG.ItemElderChestplateArmorToughness.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.CHEST)
                .add(Attributes.MINING_EFFICIENCY,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "mining_efficiency.elder_chestplate"), WLAttributeConfig.CONFIG.ItemElderChestplateMiningSpeed.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.CHEST)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "water_movement_efficiency.elder_chestplate"), WLAttributeConfig.CONFIG.ItemElderChestplateWaterMovementEfficiency.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.CHEST)
                .build();
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.elder_chestplate.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.elder_chestplate.shift_desc_2", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        ArmorTrim pComponent = pItemStack.get(DataComponents.TRIM);
        if (pItemStack.isEnchanted() || (pComponent != null && pComponent.pattern().isBound())) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
