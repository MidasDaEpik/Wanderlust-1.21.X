package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.midasdaepik.wanderlust.registries.RRAttachmentTypes.PYROSWEEP_DASH;

public record PyrosweepDashSyncS2CPacket(int PyrosweepDash) implements CustomPacketPayload {
    public static final Type<PyrosweepDashSyncS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "pyrosweep_dash_sync_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PyrosweepDashSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            PyrosweepDashSyncS2CPacket::PyrosweepDash,

            PyrosweepDashSyncS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            pPlayer.setData(PYROSWEEP_DASH, PyrosweepDash);
        });
        return true;
    }
}