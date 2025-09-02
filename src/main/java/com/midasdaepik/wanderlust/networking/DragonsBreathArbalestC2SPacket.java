package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import com.midasdaepik.wanderlust.entity.DragonsFireball;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector3f;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.DRAGON_CHARGE;

public record DragonsBreathArbalestC2SPacket() implements CustomPacketPayload {
    public static final Type<DragonsBreathArbalestC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "dragons_breath_arbalest_c2s_packet"));

    public static final DragonsBreathArbalestC2SPacket INSTANCE = new DragonsBreathArbalestC2SPacket();
    public static final StreamCodec<ByteBuf, DragonsBreathArbalestC2SPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            ServerLevel pLevel = (ServerLevel) pPlayer.level();
            ItemStack pMainhandItem = pPlayer.getMainHandItem();
            int DragonCharge = pPlayer.getData(DRAGON_CHARGE);
            int DragonChargeArbalestUse = WLCommonConfig.CONFIG.DragonChargeArbalestUse.get();

            if (pMainhandItem.getItem() == WLItems.DRAGONS_BREATH_ARBALEST.get() && !pPlayer.getCooldowns().isOnCooldown(WLItems.DRAGONS_BREATH_ARBALEST.get()) && DragonCharge >= DragonChargeArbalestUse) {

                DragonsFireball pDragonsFireball = new DragonsFireball(pLevel, pPlayer, pPlayer.getLookAngle(), 80);
                pDragonsFireball.setPos(pPlayer.getEyePosition().x, pPlayer.getEyePosition().y - 0.3, pPlayer.getEyePosition().z);
                pLevel.addFreshEntity(pDragonsFireball);

                DragonCharge -= DragonChargeArbalestUse;
                pPlayer.setData(DRAGON_CHARGE, DragonCharge);
                if (pPlayer instanceof ServerPlayer pServerPlayer) {
                    PacketDistributor.sendToPlayer(pServerPlayer, new DragonChargeSyncS2CPacket(DragonCharge));
                }

                pLevel.playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1.5f, 1.2f,0);

                pLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, 8, 0.2, 0.2, 0.2, 0);

                Vec3 pPlayerLookAngle = pPlayer.getLookAngle();
                pLevel.sendParticles(
                        WLUtil.orientedCircleVec3dInput(new Vector3f(0.64f, 0.08f, 0.80f), 8,0.75f, 0.85f,  pPlayerLookAngle.x, pPlayerLookAngle.y, pPlayerLookAngle.z),
                        pPlayer.getEyePosition().x + pPlayerLookAngle.x, pPlayer.getEyePosition().y + pPlayerLookAngle.y, pPlayer.getEyePosition().z + pPlayerLookAngle.z, 1, 0, 0, 0, 0);

                pMainhandItem.hurtAndBreak(2, pPlayer, EquipmentSlot.MAINHAND);

                pPlayer.awardStat(Stats.ITEM_USED.get(pMainhandItem.getItem()));
                pPlayer.getCooldowns().addCooldown(pMainhandItem.getItem(), 80);
            }
        });
        return true;
    }
}