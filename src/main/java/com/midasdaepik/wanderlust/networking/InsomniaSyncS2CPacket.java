package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.INSOMNIA;

public record InsomniaSyncS2CPacket(int Insomnia) implements CustomPacketPayload {
    public static final Type<InsomniaSyncS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "insomnia_sync_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InsomniaSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            InsomniaSyncS2CPacket::Insomnia,

            InsomniaSyncS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            pPlayer.setData(INSOMNIA, Insomnia);
        });
        return true;
    }
}