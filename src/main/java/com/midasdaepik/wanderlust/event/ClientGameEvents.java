package com.midasdaepik.wanderlust.event;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.networking.BlazeReapC2SPacket;
import com.midasdaepik.wanderlust.networking.DragonsBreathArbalestC2SPacket;
import com.midasdaepik.wanderlust.networking.WhisperwindC2SPacket;
import com.midasdaepik.wanderlust.registries.WLClientEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Predicate;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

@EventBusSubscriber(modid = Wanderlust.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ClientGameEvents {
    @SubscribeEvent
    public static void onPlayerHeartTypeEvent(PlayerHeartTypeEvent pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        if (pLivingEntity.hasEffect(WLEffects.ECHO)) {
            pEvent.setType(WLClientEnumExtensions.HEART_SCULK.getValue());
        }
    }

    @SubscribeEvent
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent pEvent) {
        Player pPlayer = pEvent.getPlayer();
        ItemStack pItemstack = pPlayer.getUseItem();

        if (pPlayer.isUsingItem()) {
            if (pItemstack.is(WLItems.WHISPERWIND)) {
                float pMDraw = pPlayer.getTicksUsingItem() / 15.0F;
                if (pMDraw > 1.0F) {
                    pMDraw = 1.0F;
                } else {
                    pMDraw *= pMDraw;
                }

                pEvent.setNewFovModifier(pEvent.getFovModifier() * (1.0F - pMDraw * 0.2F));
            } else if (pItemstack.is(WLItems.SONIC_ARPEGGIO)) {
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

        if (pClientPlayer.hasEffect(WLEffects.PHANTASMAL)) {
            pEvent.setCanceled(true);
        }

        if (pKeyAttack && !pClientPlayer.isSpectator()) {
            ItemStack pMainhandItem = pClientPlayer.getMainHandItem();
            if (pMainhandItem.getItem() == WLItems.WHISPERWIND.get() && !pClientPlayer.getCooldowns().isOnCooldown(WLItems.WHISPERWIND.get())) {
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
                    pEvent.setCanceled(true);
                }

            } else if (pMainhandItem.getItem() == WLItems.DRAGONS_BREATH_ARBALEST.get() && !pClientPlayer.getCooldowns().isOnCooldown(WLItems.DRAGONS_BREATH_ARBALEST.get()) && pClientPlayer.getData(DRAGON_CHARGE) >= WLCommonConfig.CONFIG.DragonChargeArbalestUse.get()) {
                PacketDistributor.sendToServer(new DragonsBreathArbalestC2SPacket());
                pEvent.setCanceled(true);

            } else if (pMainhandItem.getItem() == WLItems.BLAZE_REAP.get() && !pClientPlayer.getCooldowns().isOnCooldown(WLItems.BLAZE_REAP.get())) {
                int BlazeReapActivate = pClientPlayer.getData(BLAZE_REAP_CHARGE);
                if (BlazeReapActivate > 0) {
                    HitResult pRaycast = WLUtil.raycast(pClientPlayer.level(), pClientPlayer, ClipContext.Fluid.NONE, pClientPlayer.blockInteractionRange(), pClientPlayer.entityInteractionRange());
                    if (pRaycast.getType() != HitResult.Type.MISS) {
                        PacketDistributor.sendToServer(new BlazeReapC2SPacket());
                        pEvent.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClientTickEvent(ClientTickEvent.Pre pEvent) {
        LocalPlayer pPlayer = Minecraft.getInstance().player;
        if (pPlayer != null) {
            pPlayer.setData(DRAGON_WINGS_STATUS, (pPlayer.hasEffect(WLEffects.DRAGONS_ASCENSION) && pPlayer.getEffect(WLEffects.DRAGONS_ASCENSION).getAmplifier() >= 1));
            pPlayer.setData(PHANTASMAL_STATUS, pPlayer.hasEffect(WLEffects.PHANTASMAL));
        }
    }

    @SubscribeEvent
    public static void onRenderLivingEventPre(RenderLivingEvent.Pre pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        if (pLivingEntity.getData(PHANTASMAL_STATUS)) {
            pEvent.setCanceled(true);
        }
    }
}
