package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.networking.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class WLPacketHandler {
    public static void registerNetworking(final RegisterPayloadHandlersEvent pEvent) {
        final PayloadRegistrar pRegistrar = pEvent.registrar(Wanderlust.MOD_ID);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             Client -> Server                                               //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //pRegistrar.playToServer(TestC2SPacket.TYPE, TestC2SPacket.STREAM_CODEC, TestC2SPacket::handle);
        pRegistrar.playToServer(BlazeReapC2SPacket.TYPE, BlazeReapC2SPacket.STREAM_CODEC, BlazeReapC2SPacket::handle);
        pRegistrar.playToServer(DragonsBreathArbalestC2SPacket.TYPE, DragonsBreathArbalestC2SPacket.STREAM_CODEC, DragonsBreathArbalestC2SPacket::handle);
        pRegistrar.playToServer(WhisperwindC2SPacket.TYPE, WhisperwindC2SPacket.STREAM_CODEC, WhisperwindC2SPacket::handle);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             Server -> Client                                               //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        pRegistrar.playToClient(BlazeReapChargeSyncS2CPacket.TYPE, BlazeReapChargeSyncS2CPacket.STREAM_CODEC, BlazeReapChargeSyncS2CPacket::handle);
        pRegistrar.playToClient(CharybdisParticleS2CPacket.TYPE, CharybdisParticleS2CPacket.STREAM_CODEC, CharybdisParticleS2CPacket::handle);
        pRegistrar.playToClient(CharybdisChargeSyncS2CPacket.TYPE, CharybdisChargeSyncS2CPacket.STREAM_CODEC, CharybdisChargeSyncS2CPacket::handle);
        pRegistrar.playToClient(DragonsRageChargeSyncS2CPacket.TYPE, DragonsRageChargeSyncS2CPacket.STREAM_CODEC, DragonsRageChargeSyncS2CPacket::handle);
        pRegistrar.playToClient(PyrosweepDashSyncS2CPacket.TYPE, PyrosweepDashSyncS2CPacket.STREAM_CODEC, PyrosweepDashSyncS2CPacket::handle);
        pRegistrar.playToClient(PyroBarrierParticleS2CPacket.TYPE, PyroBarrierParticleS2CPacket.STREAM_CODEC, PyroBarrierParticleS2CPacket::handle);
        pRegistrar.playToClient(PyrosweepChargeSyncS2CPacket.TYPE, PyrosweepChargeSyncS2CPacket.STREAM_CODEC, PyrosweepChargeSyncS2CPacket::handle);
    }
}
