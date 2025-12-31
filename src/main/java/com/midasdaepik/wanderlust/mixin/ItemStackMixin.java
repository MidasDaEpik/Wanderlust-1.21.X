package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.misc.MaskContents;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
                Component pMaskTypeComponent = Component.translatable("item.wanderlust.mask_" + pMaskItemStack.getOrDefault(WLDataComponents.MASK_TYPE, 0));

                int pMaskHeight = pMaskItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE_INT, 0);
                Component pMaskHeightComponent = Component.translatable("(" + pMaskHeight + ")").withStyle(ChatFormatting.DARK_GRAY);

                pTooltipAdder.accept(Component.translatable("item.wanderlust.mask.label", pMaskTypeComponent, pMaskHeightComponent).withStyle(ChatFormatting.GRAY));
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
        List<Component> pComponents = pOriginal.call(pTooltipContext, pPlayer, pTooltipFlag);
        int pIndex = pComponents.indexOf(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        if (pIndex > 0) {
            pComponents.remove(pIndex);
            if (pComponents.size() >= pIndex + 1) {
                Component pComponent = pComponents.get(pIndex);
                if (!pComponent.getString().isEmpty() && !pComponent.getString().equals(pThis.getItem().toString())) {
                    pComponents.add(pIndex, Component.empty());
                }
            }
        }
        return pComponents;
    }
}