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
        pRegistrar.playToServer(DragonsBreathArbalestC2SPacket.TYPE, DragonsBreathArbalestC2SPacket.STREAM_CODEC, DragonsBreathArbalestC2SPacket::handle);
        pRegistrar.playToServer(WhisperwindC2SPacket.TYPE, WhisperwindC2SPacket.STREAM_CODEC, WhisperwindC2SPacket::handle);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                             Server -> Client                                               //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        pRegistrar.playToClient(CharybdisSyncS2CPacket.TYPE, CharybdisSyncS2CPacket.STREAM_CODEC, CharybdisSyncS2CPacket::handle);
        pRegistrar.playToClient(DragonsRageSyncS2CPacket.TYPE, DragonsRageSyncS2CPacket.STREAM_CODEC, DragonsRageSyncS2CPacket::handle);
        pRegistrar.playToClient(PyrosweepDashSyncS2CPacket.TYPE, PyrosweepDashSyncS2CPacket.STREAM_CODEC, PyrosweepDashSyncS2CPacket::handle);
        pRegistrar.playToClient(PyrosweepParticleS2CPacket.TYPE, PyrosweepParticleS2CPacket.STREAM_CODEC, PyrosweepParticleS2CPacket::handle);
        pRegistrar.playToClient(PyrosweepSyncS2CPacket.TYPE, PyrosweepSyncS2CPacket.STREAM_CODEC, PyrosweepSyncS2CPacket::handle);
    }
}
