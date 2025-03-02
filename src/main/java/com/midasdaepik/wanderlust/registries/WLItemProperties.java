package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.item.DragonsBreathArbalest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;

public class WLItemProperties {
    public static void addCustomItemProperties() {
        ChaliceState(WLItems.CATALYST_CHALICE.get());
        Experience(WLItems.CATALYST_CHALICE.get());
        Pull(WLItems.CHARYBDIS.get());
        Pulling(WLItems.CHARYBDIS.get());
        DragonsBreathArbalestPull(WLItems.DRAGONS_BREATH_ARBALEST.get());
        Pulling(WLItems.DRAGONS_BREATH_ARBALEST.get());
        Charged(WLItems.DRAGONS_BREATH_ARBALEST.get());
        Firework(WLItems.DRAGONS_BREATH_ARBALEST.get());
        Pull(WLItems.LYRE_OF_ECHOES.get());
        Pulling(WLItems.LYRE_OF_ECHOES.get());
        Pulling(WLItems.WARPTHISTLE.get());
        Pull(WLItems.WHISPERWIND.get());
        Pulling(WLItems.WHISPERWIND.get());
    }

    private static void Pull(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.withDefaultNamespace("pull"),
                (pItemstack, pLevel, pLivingEntity, pSeed) -> {
                    if (pLivingEntity == null) {
                        return 0.0F;
                    } else {
                        return pLivingEntity.getUseItem() != pItemstack ? 0.0f : (float) (pItemstack.getUseDuration(pLivingEntity) - pLivingEntity.getUseItemRemainingTicks()) / 20.0f;
                    }
                }
        );
    }

    private static void Pulling(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.withDefaultNamespace("pulling"),
                (pItemstack, pLevel, pLivingEntity, pSeed) -> pLivingEntity != null && pLivingEntity.isUsingItem() && pLivingEntity.getUseItem() == pItemstack ? 1.0f : 0.0f
        );
    }

    private static void DragonsBreathArbalestPull(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.withDefaultNamespace("pull"),
                (pItemstack, pLevel, pLivingEntity, pSeed) -> {
                    if (pLivingEntity == null) {
                        return 0.0F;
                    } else {
                        return DragonsBreathArbalest.isCharged(pItemstack)
                                ? 0.0F
                                : (float) (pItemstack.getUseDuration(pLivingEntity) - pLivingEntity.getUseItemRemainingTicks())
                                / (float) DragonsBreathArbalest.getChargeDuration(pItemstack, pLivingEntity);
                    }
                }
        );
    }

    private static void Charged(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.withDefaultNamespace("charged"),
                (pItemstack, pLevel, pLivingEntity, pSeed) -> CrossbowItem.isCharged(pItemstack) ? 1.0F : 0.0F
        );
    }

    private static void Firework(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.withDefaultNamespace("firework"),
                (pItemstack, pLevel, pLivingEntity, pSeed) -> {
                    ChargedProjectiles chargedprojectiles = pItemstack.get(DataComponents.CHARGED_PROJECTILES);
                    return chargedprojectiles != null && chargedprojectiles.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
                }
        );
    }

    private static void Experience(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.withDefaultNamespace("experience"),
                (pItemstack, pLevel, pLivingEntity, pSeed) -> {
                    if (pLivingEntity != null) {
                        return (float) pItemstack.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue() / pItemstack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 1.0).intValue();
                    } else {
                        return 0.0f;
                    }
                }
        );
    }

    private static void ChaliceState(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.withDefaultNamespace("chalice_state"),
                (pItemstack, pLevel, pLivingEntity, pSeed) -> {
                    if (pLivingEntity != null) {
                        return pItemstack.getOrDefault(WLDataComponents.ITEM_TOGGLE, true) ? 1.0f : 0.0f;
                    } else {
                        return 1.0f;
                    }
                }
        );
    }
}
