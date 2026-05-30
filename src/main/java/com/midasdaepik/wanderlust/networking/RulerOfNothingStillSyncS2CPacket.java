package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.RULER_OF_NOTHING_STILL;

public record RulerOfNothingStillSyncS2CPacket(int RulerOfNothingStill) implements CustomPacketPayload {
    public static final Type<RulerOfNothingStillSyncS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "ruler_of_nothing_still_sync_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RulerOfNothingStillSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            RulerOfNothingStillSyncS2CPacket::RulerOfNothingStill,

            RulerOfNothingStillSyncS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            pPlayer.setData(RULER_OF_NOTHING_STILL, RulerOfNothingStill);
        });
        return true;
    }
}