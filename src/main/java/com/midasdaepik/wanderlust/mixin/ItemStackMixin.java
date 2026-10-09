package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAbilitiesConfig;
import com.midasdaepik.wanderlust.item.Mask;
import com.midasdaepik.wanderlust.misc.MaskContents;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @WrapOperation(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", ordinal = 1))
    private <T extends TooltipProvider> void getTooltipLines(ItemStack pItemStack, DataComponentType<T> pComponent, Item.TooltipContext pTooltip, Consumer<Component> pTooltipAdder, TooltipFlag pTooltipFlag, Operation<Void> pOriginal) {
        if (pItemStack.has(WLDataComponents.MASK_SLOT)) {
            MaskContents pMaskContents = pItemStack.get(WLDataComponents.MASK_SLOT);
            if (pMaskContents != null) {
                ItemStack pMaskItemStack = pMaskContents.pMask();
                Component pMaskTypeComponent = Component.translatable("item.wanderlust.mask_type." + pMaskItemStack.getOrDefault(WLDataComponents.MASK_TYPE, Mask.MaskType.BASIC).name);

                Component pMaskHeightComponent = Component.literal("(" + pMaskItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 0) + ")").withStyle(ChatFormatting.DARK_GRAY);

                if (pMaskItemStack.has(DataComponents.CUSTOM_NAME)) {
                    Component pMaskCustomNameComponent = pMaskItemStack.getOrDefault(DataComponents.CUSTOM_NAME, Component.empty());

                    pTooltipAdder.accept(Component.translatable("item.wanderlust.mask.label").withStyle(ChatFormatting.GRAY)
                            .append(" ").append(pMaskCustomNameComponent)
                            .append(" ").append(Component.literal("(").withStyle(ChatFormatting.DARK_GRAY)).append(pMaskTypeComponent).append(Component.literal(")").withStyle(ChatFormatting.DARK_GRAY))
                            .append(" ").append(pMaskHeightComponent));
                } else {
                    pTooltipAdder.accept(Component.translatable("item.wanderlust.mask.label").withStyle(ChatFormatting.GRAY)
                            .append(" ").append(pMaskTypeComponent)
                            .append(" ").append(pMaskHeightComponent));
                }
            }
        }

        if (pItemStack.has(WLDataComponents.COSMETIC_TYPE) && pItemStack.has(WLDataComponents.COSMETIC_MATERIAL)) {
            pTooltipAdder.accept(Component.translatable("item.wanderlust.smithing_template.effect").withStyle(ChatFormatting.GRAY));
            pTooltipAdder.accept(CommonComponents.space().append(Component.translatable("item.wanderlust." + pItemStack.get(WLDataComponents.COSMETIC_TYPE) + "_armor_effect_smithing_template_desc").withStyle(ChatFormatting.GOLD)));
            pTooltipAdder.accept(CommonComponents.space().append(Component.translatable("item.wanderlust." + pItemStack.get(WLDataComponents.COSMETIC_TYPE) + "_armor_effect_smithing_template_desc_" + pItemStack.get(WLDataComponents.COSMETIC_MATERIAL)).withStyle(ChatFormatting.GOLD)));
        }

        pOriginal.call(pItemStack, pComponent, pTooltip, pTooltipAdder, pTooltipFlag);
    }

    @WrapMethod(method = "getTooltipLines")
    private List<Component> getTooltipLines(Item.TooltipContext pTooltipContext, Player pPlayer, TooltipFlag pTooltipFlag, Operation<List<Component>> pOriginal) {
        ItemStack pThis = (ItemStack) (Object) this;
        List<Component> pTooltipComponents = pOriginal.call(pTooltipContext, pPlayer, pTooltipFlag);
        int pIndex = pTooltipComponents.indexOf(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        if (pIndex > 0) {
            pTooltipComponents.remove(pIndex);
            if (pTooltipComponents.size() >= pIndex + 1) {
                Component pComponent = pTooltipComponents.get(pIndex);
                if (!pComponent.getString().isEmpty() && !pComponent.getString().equals(pThis.getItem().toString())) {
                    pTooltipComponents.add(pIndex, Component.empty());
                }
            }
        }

        Item pItem = pThis.getItem();
        ResourceLocation pResourceLocation = BuiltInRegistries.ITEM.getKey(pItem);
        String pTranslationKey = "item." + pResourceLocation.getNamespace() + "." + pResourceLocation.getPath() + ".shift_desc_1";
        if (Language.getInstance().has(pTranslationKey)) {
            if (pTooltipFlag.hasShiftDown()) {
                String pIteratedTranslationKey;
                for (int pIterator = 1; ; pIterator++) {
                    pIteratedTranslationKey = "item." + pResourceLocation.getNamespace() + "." + pResourceLocation.getPath() + ".shift_desc_" + pIterator;
                    if (Language.getInstance().has(pIteratedTranslationKey)) {
                        pTooltipComponents.add(wanderlust_1_21_X$convertTextToComponentWithFormatting(Component.translatable(pIteratedTranslationKey).getString(), pPlayer));
                    } else {
                        break;
                    }
                }
            } else {
                pTooltipComponents.add(wanderlust_1_21_X$convertTextToComponentWithFormatting(Component.translatable("item.wanderlust.shift_desc_info").getString(), pPlayer));
            }
        }

        return pTooltipComponents;
    }

    @Unique
    public Component wanderlust_1_21_X$convertTextToComponentWithFormatting(String pText, Player pPlayer) {
        if (pText.isBlank()) {
            return Component.empty();
        }
        String[] pSplitText = pText.split("(?<=})|(?=\\{)");
        MutableComponent pMutableComponent = Component.empty();
        int pColor = 0xAAAAAA;
        for (String pListText : pSplitText) {
            if (pListText.charAt(0) == '{' && pListText.charAt(pListText.length() - 1) == '}' && pListText.charAt(2) == '/') {
                String pIteratedText = pListText.substring(3, pListText.length() - 1);
                char pId = pListText.charAt(1);
                if (pId == 'c' && wanderlust_1_21_X$isInteger(pIteratedText)) {
                    pColor = wanderlust_1_21_X$colorLimiter(Integer.parseInt(pIteratedText, 16));
                } else if (pId == 'e' && wanderlust_1_21_X$isEffect(pIteratedText)) {
                    String[] pMobEffectSplitText = pIteratedText.split("/");
                    if (pMobEffectSplitText.length == 1) {
                        MobEffect pMobEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(pIteratedText));
                        pMutableComponent.append(Component.translatable(pMobEffect.getDescriptionId()).withColor(wanderlust_1_21_X$colorLimiter(pMobEffect.getColor())));
                    } else if (pMobEffectSplitText.length == 2) {
                        MobEffect pMobEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(pMobEffectSplitText[0]));
                        pMutableComponent.append(Component.translatable(pMobEffect.getDescriptionId()).append(" " + pMobEffectSplitText[1]).withColor(wanderlust_1_21_X$colorLimiter(pMobEffect.getColor())));
                    }
                } else if (pId == 'i' && wanderlust_1_21_X$isItem(pIteratedText)) {
                    Item pItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(pIteratedText));
                    Style pStyle = pItem.components().getOrDefault(DataComponents.RARITY, Rarity.COMMON).getStyleModifier().apply(Style.EMPTY);
                    pMutableComponent.append(Component.translatable(pItem.getDescriptionId()).withStyle(pStyle).withColor(wanderlust_1_21_X$colorLimiter(pStyle.getColor().getValue())));
                }  else if (pId == 't') {
                    pMutableComponent.append(Component.translatable(pIteratedText).withColor(wanderlust_1_21_X$colorLimiter(pColor)));
                } else if (pId == 's') {
                    pMutableComponent.append(Component.literal(pIteratedText).setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
                } else if (pId == 'r') {
                    pColor = 0xAAAAAA;
                } else if (pId == 'w') {
                    switch (pIteratedText) {
                        case "phantom_armor" -> {
                            if (pPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.PHANTOM_CLOAK.get()) {
                                pMutableComponent.append(Component.translatable("item.wanderlust.phantom_armor.shift_desc_ability_inactive").withStyle(ChatFormatting.DARK_GRAY));
                            } else {
                                int pCooldown = WLAbilitiesConfig.CONFIG.ItemPhantomArmorBaseCooldown.get();
                                int pPieces = 0;
                                if (pPlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() == WLItems.PHANTOM_HOOD.get()) {
                                    pCooldown -= WLAbilitiesConfig.CONFIG.ItemPhantomArmorHoodCooldownDecrease.get();
                                    pPieces += 1;
                                }
                                if (pPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.PHANTOM_TUNIC.get()) {
                                    pCooldown -= WLAbilitiesConfig.CONFIG.ItemPhantomArmorTunicCooldownDecrease.get();
                                    pPieces += 1;
                                }
                                if (pPlayer.getItemBySlot(EquipmentSlot.LEGS).getItem() == WLItems.PHANTOM_LEGGINGS.get()) {
                                    pCooldown -= WLAbilitiesConfig.CONFIG.ItemPhantomArmorLeggingsCooldownDecrease.get();
                                    pPieces += 1;
                                }
                                if (pPlayer.getItemBySlot(EquipmentSlot.FEET).getItem() == WLItems.PHANTOM_BOOTS.get()) {
                                    pCooldown -= WLAbilitiesConfig.CONFIG.ItemPhantomArmorBootsCooldownDecrease.get();
                                    pPieces += 1;
                                }

                                if (pPieces >= 2) {
                                    pMutableComponent.append(Component.translatable("item.wanderlust.phantom_armor.shift_desc_ability_cooldown", Component.literal(String.valueOf((int) Math.floor((double) pCooldown / 20)))).withStyle(ChatFormatting.DARK_GRAY));
                                } else {
                                    pMutableComponent.append(Component.translatable("item.wanderlust.phantom_armor.shift_desc_ability_inactive").withStyle(ChatFormatting.DARK_GRAY));
                                }
                            }
                        }
                    }
                } else {
                    pMutableComponent.append(Component.literal(pListText).withColor(wanderlust_1_21_X$colorLimiter(pColor)));
                }
            } else {
                pMutableComponent.append(Component.literal(pListText).withColor(wanderlust_1_21_X$colorLimiter(pColor)));
            }
        }
        return pMutableComponent;
    }

    @Unique
    public boolean wanderlust_1_21_X$isInteger(String pString) {
        try {
            Integer.parseInt(pString, 16);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Unique
    public boolean wanderlust_1_21_X$isEffect(String pString) {
        String[] pSplitText = pString.split("/");
        if (pSplitText.length == 1) {
            return BuiltInRegistries.MOB_EFFECT.getOptional(ResourceLocation.parse(pString)).isPresent();
        } else if (pSplitText.length == 2) {
            return BuiltInRegistries.MOB_EFFECT.getOptional(ResourceLocation.parse(pSplitText[0])).isPresent();
        } else {
            return false;
        }
    }

    @Unique
    public boolean wanderlust_1_21_X$isItem(String pString) {
        return BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(pString)).isPresent();
    }

    @Unique
    public int wanderlust_1_21_X$colorLimiter(int pInputColor) {
        Color pColor = Color.decode(String.valueOf(pInputColor));
        return (new Color(Math.round((float) pColor.getRed() / 85) * 85, Math.round((float) pColor.getGreen() / 85) * 85, Math.round((float) pColor.getBlue() / 85) * 85)).getRGB();
    }
}