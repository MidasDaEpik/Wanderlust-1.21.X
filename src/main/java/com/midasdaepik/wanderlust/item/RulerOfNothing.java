package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.registries.WLArmorMaterials;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

public class RulerOfNothing extends ArmorItem {
    public RulerOfNothing(Properties pProperties) {
        super(WLArmorMaterials.SCULK_ARMOR_MATERIAL, Type.HELMET,
                pProperties
                        .durability(Type.HELMET.getDurability(WLAttributeConfig.CONFIG.ItemRulerOfNothingDurabilityFactor.get()))
                        .attributes(RulerOfNothing.createAttributes())
                        .rarity(WLEnumExtensions.RARITY_SCULK.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.ruler_of_nothing"), WLAttributeConfig.CONFIG.ItemRulerOfNothingArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.ARMOR_TOUGHNESS,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor_toughness.ruler_of_nothing"), WLAttributeConfig.CONFIG.ItemRulerOfNothingArmorToughness.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "knockback_resistance.ruler_of_nothing"), WLAttributeConfig.CONFIG.ItemRulerOfNothingKnockbackResistance.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .build();
    }
}
