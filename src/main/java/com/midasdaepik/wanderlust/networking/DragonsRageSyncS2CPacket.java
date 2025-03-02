package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.midasdaepik.wanderlust.registries.RRAttachmentTypes.DRAGONS_RAGE_CHARGE;

public record DragonsRageSyncS2CPacket(int DragonsRageCharge) implements CustomPacketPayload {
    public static final Type<DragonsRageSyncS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "dragons_rage_sync_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DragonsRageSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            DragonsRageSyncS2CPacket::DragonsRageCharge,

            DragonsRageSyncS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            pPlayer.setData(DRAGONS_RAGE_CHARGE, DragonsRageCharge);
        });
        return true;
    }
}