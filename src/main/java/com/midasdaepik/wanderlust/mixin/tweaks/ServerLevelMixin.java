package com.midasdaepik.wanderlust.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.midasdaepik.wanderlust.networking.InsomniaSyncS2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.INSOMNIA;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.LAST_SLEPT_LOCATION;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @WrapOperation(method = "lambda$wakeUpAllPlayers$3", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;stopSleepInBed(ZZ)V"))
    private static void lambda$wakeUpAllPlayers$3(ServerPlayer pServerPlayer, boolean pWakeImmediately, boolean pUpdateLevelForSleepingPlayers, Operation<Void> pOriginal) {
        Vec3 pPosition = pServerPlayer.getPosition(0);
        Vec3 LastSleptPosition = pServerPlayer.getData(LAST_SLEPT_LOCATION);

        int Insomnia = pServerPlayer.getData(INSOMNIA);
        if (pPosition.distanceTo(LastSleptPosition) >= 32) {
            Insomnia = Insomnia + 2 * 24000;
        } else {
            Insomnia = Insomnia - 4 * 24000;
        }
        Insomnia = Math.clamp(Insomnia, 0, 24000 * 16);
        pServerPlayer.setData(INSOMNIA, Insomnia);
        PacketDistributor.sendToPlayer(pServerPlayer, new InsomniaSyncS2CPacket(Insomnia));

        pServerPlayer.setData(LAST_SLEPT_LOCATION, pPosition);
        pOriginal.call(pServerPlayer, pWakeImmediately, pUpdateLevelForSleepingPlayers);
    }
}