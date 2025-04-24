package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLParticles;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PyroBarrierParticleS2CPacket(double pX, double pY, double pZ, double pXDir, double pYDir, double pZDir) implements CustomPacketPayload {
    public static final Type<PyroBarrierParticleS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "pyrosweep_particle_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PyroBarrierParticleS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            PyroBarrierParticleS2CPacket::pX,

            ByteBufCodecs.DOUBLE,
            PyroBarrierParticleS2CPacket::pY,

            ByteBufCodecs.DOUBLE,
            PyroBarrierParticleS2CPacket::pZ,

            ByteBufCodecs.DOUBLE,
            PyroBarrierParticleS2CPacket::pXDir,

            ByteBufCodecs.DOUBLE,
            PyroBarrierParticleS2CPacket::pYDir,

            ByteBufCodecs.DOUBLE,
            PyroBarrierParticleS2CPacket::pZDir,

            PyroBarrierParticleS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            pPlayer.level().addParticle(WLParticles.PYRO_BARRIER.get(), pX, pY, pZ, pXDir, pYDir, pZDir);
        });
        return true;
    }
}