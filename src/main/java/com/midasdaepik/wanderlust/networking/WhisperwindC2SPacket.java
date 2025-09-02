package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Predicate;

public record WhisperwindC2SPacket() implements CustomPacketPayload {
    public static final Type<WhisperwindC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "whisperwind_c2s_packet"));

    public static final WhisperwindC2SPacket INSTANCE = new WhisperwindC2SPacket();
    public static final StreamCodec<ByteBuf, WhisperwindC2SPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            ServerLevel pLevel = (ServerLevel) pPlayer.level();
            ItemStack pMainhandItem = pPlayer.getMainHandItem();

            if (pMainhandItem.getItem() == WLItems.WHISPERWIND.get() && !pPlayer.getCooldowns().isOnCooldown(WLItems.WHISPERWIND.get())) {
                Predicate<ItemStack> pIsAmmo = pItem -> pItem.is(net.minecraft.world.item.Items.WIND_CHARGE);

                ItemStack ProjectileItemStack = ItemStack.EMPTY;
                if (pIsAmmo.test(pPlayer.getItemInHand(InteractionHand.OFF_HAND))) {
                    ProjectileItemStack = pPlayer.getItemInHand(InteractionHand.OFF_HAND);
                } else  {
                    for (int i = 0; i < pPlayer.getInventory().getContainerSize(); i++) {
                        if (pIsAmmo.test(pPlayer.getInventory().getItem(i))) {
                            ProjectileItemStack = CommonHooks.getProjectile(pPlayer, pMainhandItem, pPlayer.getInventory().getItem(i));
                            i = pPlayer.getInventory().getContainerSize();
                        }
                    }
                }

                if (pPlayer.hasInfiniteMaterials() || !ProjectileItemStack.isEmpty()) {
                    WindCharge windcharge = new WindCharge(pPlayer, pPlayer.level(), pPlayer.position().x(), pPlayer.getEyePosition().y(), pPlayer.position().z());
                    windcharge.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
                    pPlayer.level().addFreshEntity(windcharge);

                    pPlayer.level().playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.WIND_CHARGE_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (pPlayer.level().getRandom().nextFloat() * 0.4F + 0.8F));

                    ProjectileItemStack.consume(1, pPlayer);
                    pMainhandItem.hurtAndBreak(1, pPlayer, EquipmentSlot.MAINHAND);

                    pPlayer.awardStat(Stats.ITEM_USED.get(pMainhandItem.getItem()));
                    pPlayer.getCooldowns().addCooldown(pMainhandItem.getItem(), 10);
                    pPlayer.getCooldowns().addCooldown(Items.WIND_CHARGE, 10);
                }
            }
        });
        return true;
    }
}