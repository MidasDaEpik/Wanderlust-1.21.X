package com.midasdaepik.wanderlust.event;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.networking.DragonsBreathArbalestC2SPacket;
import com.midasdaepik.wanderlust.networking.WhisperwindC2SPacket;
import com.midasdaepik.wanderlust.registries.RRItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Predicate;

@EventBusSubscriber(modid = Wanderlust.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ClientGameEvents {
    @SubscribeEvent
    public static void onPlayerHeartTypeEvent(PlayerHeartTypeEvent pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        if (pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() == RRItems.WHISPERWIND.get()) {
            pEvent.setType(Gui.HeartType.FROZEN);
        }
    }

    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent pEvent) {
        Player pPlayer = pEvent.getPlayer();
        ItemStack pItemstack = pPlayer.getUseItem();

        if (pPlayer.isUsingItem()) {
            if (pItemstack.is(RRItems.WHISPERWIND)) {
                float pMDraw = pPlayer.getTicksUsingItem() / 15.0F;
                if (pMDraw > 1.0F) {
                    pMDraw = 1.0F;
                } else {
                    pMDraw *= pMDraw;
                }

                pEvent.setNewFovModifier(pEvent.getFovModifier() * (1.0F - pMDraw * 0.2F));
            } else if (pItemstack.is(RRItems.LYRE_OF_ECHOES)) {
                float pMDraw = pPlayer.getTicksUsingItem() / 300.0F;
                if (pMDraw > 1.0F) {
                    pMDraw = 1.0F;
                } else {
                    pMDraw *= pMDraw;
                }

                pEvent.setNewFovModifier(pEvent.getFovModifier() * (1.0F + pMDraw * 0.2F));
            }
        }
    }

    @SubscribeEvent
    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered pEvent) {
        Minecraft pMinecraft = Minecraft.getInstance();
        Player pClientPlayer = pMinecraft.player;

        if (pClientPlayer == null) {
            return;
        }

        boolean pKeyAttack = pEvent.isAttack();

        if (pKeyAttack) {
            ItemStack pMainhandItem = pClientPlayer.getMainHandItem();
            if (pMainhandItem.getItem() == RRItems.WHISPERWIND.get() && !pClientPlayer.getCooldowns().isOnCooldown(RRItems.WHISPERWIND.get())) {
                Predicate<ItemStack> pIsAmmo = pItem -> pItem.is(net.minecraft.world.item.Items.WIND_CHARGE);

                ItemStack ProjectileItemStack = ItemStack.EMPTY;
                if (pIsAmmo.test(pClientPlayer.getItemInHand(InteractionHand.OFF_HAND))) {
                    ProjectileItemStack = pClientPlayer.getItemInHand(InteractionHand.OFF_HAND);
                } else  {
                    for (int i = 0; i < pClientPlayer.getInventory().getContainerSize(); i++) {
                        if (pIsAmmo.test(pClientPlayer.getInventory().getItem(i))) {
                            ProjectileItemStack = CommonHooks.getProjectile(pClientPlayer, pMainhandItem, pClientPlayer.getInventory().getItem(i));
                            i = pClientPlayer.getInventory().getContainerSize();
                        }
                    }
                }

                if (pClientPlayer.hasInfiniteMaterials() || !ProjectileItemStack.isEmpty()) {
                    PacketDistributor.sendToServer(new WhisperwindC2SPacket());
                }
            } else if (pMainhandItem.getItem() == RRItems.DRAGONS_BREATH_ARBALEST.get() && !pClientPlayer.getCooldowns().isOnCooldown(RRItems.DRAGONS_BREATH_ARBALEST.get())) {
                PacketDistributor.sendToServer(new DragonsBreathArbalestC2SPacket());
            }
        }
    }
}
