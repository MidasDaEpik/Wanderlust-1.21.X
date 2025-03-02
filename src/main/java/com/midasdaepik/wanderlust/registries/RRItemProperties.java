package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.item.DragonsBreathArbalest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;

public class RRItemProperties {
    public static void addCustomItemProperties() {
        ChaliceState(RRItems.CATALYST_CHALICE.get());
        Experience(RRItems.CATALYST_CHALICE.get());
        Pull(RRItems.CHARYBDIS.get());
        Pulling(RRItems.CHARYBDIS.get());
        DragonsBreathArbalestPull(RRItems.DRAGONS_BREATH_ARBALEST.get());
        Pulling(RRItems.DRAGONS_BREATH_ARBALEST.get());
        Charged(RRItems.DRAGONS_BREATH_ARBALEST.get());
        Firework(RRItems.DRAGONS_BREATH_ARBALEST.get());
        Pull(RRItems.LYRE_OF_ECHOES.get());
        Pulling(RRItems.LYRE_OF_ECHOES.get());
        Pulling(RRItems.WARPTHISTLE.get());
        Pull(RRItems.WHISPERWIND.get());
        Pulling(RRItems.WHISPERWIND.get());
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
                        return (float) pItemstack.getOrDefault(RRDataComponents.EXPERIENCE, 0.0).intValue() / pItemstack.getOrDefault(RRDataComponents.MAXIMUM_EXPERIENCE, 1.0).intValue();
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
                        return pItemstack.getOrDefault(RRDataComponents.ITEM_TOGGLE, true) ? 1.0f : 0.0f;
                    } else {
                        return 1.0f;
                    }
                }
        );
    }
}
