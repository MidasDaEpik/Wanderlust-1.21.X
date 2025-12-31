package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLArmorMaterials;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Mask extends ArmorItem {
    public Mask(Properties pProperties) {
        super(WLArmorMaterials.MASK_MATERIAL, Type.HELMET,
                pProperties
                        .durability(Type.HELMET.getDurability(WLAttributeConfig.CONFIG.ItemMaskDurability.get() / 11))
                        .attributes(Mask.createAttributes())
                        .rarity(Rarity.COMMON));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.mask"), WLAttributeConfig.CONFIG.ItemMaskArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .build();
    }

    @Override
    public Component getName(ItemStack pItemStack) {
        int pMaskValue = pItemStack.getOrDefault(WLDataComponents.MASK_TYPE, 0);
        if (pMaskValue != 0) {
            return Component.translatable(this.getDescriptionId(pItemStack)).append(Component.literal(" - ")).append(Component.translatable(this.getDescriptionId(pItemStack) + "_" + pMaskValue));
        } else {
            return Component.translatable(this.getDescriptionId(pItemStack));
        }
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack pItemStack, Entity pEntity, EquipmentSlot pSlot, ArmorMaterial.Layer pLayer, boolean pInnerModel) {
        if (pItemStack.has(WLDataComponents.MASK_TYPE)) {
            if (pInnerModel) {
                switch (pItemStack.getOrDefault(WLDataComponents.MASK_TYPE, 0)) {
                    case 10, 16 -> {
                        return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/mask/" + pItemStack.getOrDefault(WLDataComponents.MASK_TYPE, 0) + "_alt.png");
                    }
                    default -> {
                        return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/mask/" + pItemStack.getOrDefault(WLDataComponents.MASK_TYPE, 0) + ".png");
                    }
                }
            } else {
                return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/mask/" + pItemStack.getOrDefault(WLDataComponents.MASK_TYPE, 0) + ".png");
            }
        } else {
            return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/mask/base.png");
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pItemStack, ItemStack pOtherItemStack, Slot pSlot, ClickAction pClickAction, Player pPlayer, SlotAccess pSlotAccess) {
        if (pClickAction == ClickAction.SECONDARY && pOtherItemStack.isEmpty()) {
            pPlayer.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.8f, 1f);

            int pToggle = pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 0) + 1;
            if (pToggle == 3) {
                pToggle = -2;
            }
            pItemStack.set(WLDataComponents.ITEM_TOGGLE_INT, pToggle);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.mask.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.mask.shift_desc_2"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.mask.lore_desc_1", pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 0)));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}