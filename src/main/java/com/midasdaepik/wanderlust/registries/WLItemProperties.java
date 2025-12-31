package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.item.DragonsBreathArbalest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.BLAZE_REAP_CHARGE;

public class WLItemProperties {
    public static void addCustomItemProperties() {
        BlazeReapActivated(WLItems.BLAZE_REAP.get());
        ChaliceState(WLItems.CATALYST_CHALICE.get());
        Experience(WLItems.CATALYST_CHALICE.get());
        Experience(WLItems.CATALYST_CRYSTAL.get());
        Pull(WLItems.CHARYBDIS.get());
        Pulling(WLItems.CHARYBDIS.get());
        DragonsBreathArbalestPull(WLItems.DRAGONS_BREATH_ARBALEST.get());
        Pulling(WLItems.DRAGONS_BREATH_ARBALEST.get());
        Charged(WLItems.DRAGONS_BREATH_ARBALEST.get());
        Firework(WLItems.DRAGONS_BREATH_ARBALEST.get());
        MaskType(WLItems.MASK.get());
        Pull(WLItems.SONIC_ARPEGGIO.get());
        Pulling(WLItems.SONIC_ARPEGGIO.get());
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
                (pItemstack, pLevel, pLivingEntity, pSeed) ->
                        CrossbowItem.isCharged(pItemstack) ? 1.0F : 0.0F
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

    private static void BlazeReapActivated(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "blaze_reap_activated"),
                (pItemstack, pLevel, pLivingEntity, pSeed) -> {
                    if (pLivingEntity instanceof Player pPlayer) {
                        return pPlayer.getData(BLAZE_REAP_CHARGE);
                    } else {
                        return 0.0f;
                    }
                }
        );
    }

    private static void Experience(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
               ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "experience"),
                (pItemstack, pLevel, pLivingEntity, pSeed) ->
                        (float) pItemstack.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue() / pItemstack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 1.0).intValue()
        );
    }

    private static void ChaliceState(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
               ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "chalice_state"),
                (pItemstack, pLevel, pLivingEntity, pSeed) ->
                        pItemstack.getOrDefault(WLDataComponents.ITEM_TOGGLE_BOOL, true) ? 1.0f : 0.0f
        );
    }

    private static void MaskType(Item pItem) {
        net.minecraft.client.renderer.item.ItemProperties.register(
                pItem,
                ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "mask_type"),
                (pItemstack, pLevel, pLivingEntity, pSeed) ->
                        pItemstack.getOrDefault(WLDataComponents.MASK_TYPE, 0) / 10f
        );
    }
}
