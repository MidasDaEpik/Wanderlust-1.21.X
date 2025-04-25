package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CharybdisParticleS2CPacket(double pX, double pY, double pZ) implements CustomPacketPayload {
    public static final Type<CharybdisParticleS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "charybdis_particle_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CharybdisParticleS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            CharybdisParticleS2CPacket::pX,

            ByteBufCodecs.DOUBLE,
            CharybdisParticleS2CPacket::pY,

            ByteBufCodecs.DOUBLE,
            CharybdisParticleS2CPacket::pZ,

            CharybdisParticleS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            for (int Loop = 1; Loop <= 2; Loop++) {
                double XZDegrees = Mth.nextInt(RandomSource.create(), 1, 360) * Math.PI / 180;
                float XZRange = Mth.nextFloat(RandomSource.create(), 2.0f, 12.0f);
                double dX = pX + Math.cos(XZDegrees) * XZRange;
                double dZ = pZ + Math.sin(XZDegrees) * XZRange;

                float YRange = Mth.nextFloat(RandomSource.create(), -1.75f, 1.25f);
                float PullSpeed = Mth.nextFloat(RandomSource.create(), -3.0f, -1.5f);

                double pAngle = XZDegrees - Math.PI / 3;
                double dXSpeed = PullSpeed * Math.cos(pAngle);
                double dZSpeed = PullSpeed * Math.sin(pAngle);

                pPlayer.level().addParticle(ParticleTypes.OMINOUS_SPAWNING, dX, pY + YRange, dZ, dXSpeed, Mth.nextFloat(RandomSource.create(), -0.25f, 0.25f), dZSpeed);
            }
        });
        return true;
    }
}