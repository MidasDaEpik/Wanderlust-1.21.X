package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLArmorMaterials;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mask extends ArmorItem {
    public enum MaskType {
        BASIC("basic", 0),
        THE_FOOL("the_fool", 1),
        THE_MAGICIAN("the_magician", 2),
        THE_HIGH_PRIESTESS("the_high_priestess", 3),
        THE_EMPRESS("the_empress", 4),
        THE_EMPEROR("the_emperor", 5),
        THE_HIEROPHANT("the_hierophant", 6),
        THE_LOVERS("the_lovers", 7),
        THE_CHARIOT("the_chariot", 8),
        STRENGTH("strength", 9),
        THE_HERMIT("the_hermit", 10),
        WHEEL_OF_FORTUNE("wheel_of_fortune", 11),
        JUSTICE("justice", 12),
        THE_HANGED_MAN("the_hanged_man", 13),
        DEATH("death", 14),
        TEMPERANCE("temperance", 15),
        THE_DEVIL("the_devil", 16),
        THE_TOWER("the_tower", 17),
        THE_STAR("the_star", 18),
        THE_MOON("the_moon", 19),
        THE_SUN("the_sun", 20),
        JUDGEMENT("judgement", 21),
        THE_WORLD("the_world", 22);

        public final String name;
        public final int id;

        MaskType(String pName, int pId) {
            this.name = pName;
            this.id = pId;
        }

        private static final Map<String, MaskType> BY_NAME =
                Arrays.stream(values()).collect(Collectors.toMap(t -> t.name, Function.identity()));

        public static MaskType fromName(String pName) {
            return BY_NAME.getOrDefault(pName, BASIC);
        }

        public static final Codec<MaskType> CODEC =
                Codec.STRING.xmap(Mask.MaskType::fromName, p_331571_ -> p_331571_.name);

        public static final StreamCodec<ByteBuf, MaskType> STREAM_CODEC =
                ByteBufCodecs.STRING_UTF8.map(MaskType::fromName, p_331640_ -> p_331640_.name);
    }

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
    public ResourceLocation getArmorTexture(ItemStack pItemStack, Entity pEntity, EquipmentSlot pSlot, ArmorMaterial.Layer pLayer, boolean pInnerModel) {
        if (pInnerModel) {
            return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/mask/hood/" + pItemStack.getOrDefault(WLDataComponents.MASK_TYPE, MaskType.BASIC).name + ".png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "textures/models/armor/mask/" + pItemStack.getOrDefault(WLDataComponents.MASK_TYPE, MaskType.BASIC).name + ".png");
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
        pTooltipComponents.add(Component.translatable("item.wanderlust.mask_type." + pItemStack.getOrDefault(WLDataComponents.MASK_TYPE, MaskType.BASIC).name).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        pTooltipComponents.add(Component.empty());
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.mask.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.mask.shift_desc_2"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(Component.translatable("item.wanderlust.mask.lore_desc_1", pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 0)));
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}