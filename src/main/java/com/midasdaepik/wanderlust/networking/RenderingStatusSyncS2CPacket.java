package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.DRAGON_WINGS_STATUS;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PHANTASMAL_STATUS;

public record RenderingStatusSyncS2CPacket(int EntityTarget, boolean DragonWings, boolean Phantasmal) implements CustomPacketPayload {
    public static final Type<RenderingStatusSyncS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "rendering_status_sync_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RenderingStatusSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            RenderingStatusSyncS2CPacket::EntityTarget,

            ByteBufCodecs.BOOL,
            RenderingStatusSyncS2CPacket::DragonWings,

            ByteBufCodecs.BOOL,
            RenderingStatusSyncS2CPacket::Phantasmal,

            RenderingStatusSyncS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            Level pLevel = pPlayer.level();
            Entity pEntity = pLevel.getEntity(EntityTarget);

            pEntity.setData(DRAGON_WINGS_STATUS, DragonWings);
            pEntity.setData(PHANTASMAL_STATUS, Phantasmal);
        });
        return true;
    }
}