package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PHANTOM_HOVER;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PHANTOM_HOVER_TOGGLE;

public record PhantomHoverSyncC2SPacket(boolean PhantomHoverValue) implements CustomPacketPayload {
    public static final Type<PhantomHoverSyncC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "phantom_hover_sync_c2s_packet"));

    public static final StreamCodec<ByteBuf, PhantomHoverSyncC2SPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            PhantomHoverSyncC2SPacket::PhantomHoverValue,

            PhantomHoverSyncC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            pPlayer.setData(PHANTOM_HOVER_TOGGLE, PhantomHoverValue);

            if (PhantomHoverValue && pPlayer instanceof ServerPlayer pServerPlayer) {
                int PhantomHover = pPlayer.getData(PHANTOM_HOVER);
                if (PhantomHover <= 0) {
                    PacketDistributor.sendToPlayer(pServerPlayer, new PhantomHoverSyncS2CPacket(PhantomHover));
                }
            }
        });
        return true;
    }
}