package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PYROSWEEP_CHARGE;

public record PyrosweepSyncS2CPacket(int PyrosweepCharge) implements CustomPacketPayload {
    public static final Type<PyrosweepSyncS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "pyrosweep_sync_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PyrosweepSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            PyrosweepSyncS2CPacket::PyrosweepCharge,

            PyrosweepSyncS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
        });
        return true;
    }
}