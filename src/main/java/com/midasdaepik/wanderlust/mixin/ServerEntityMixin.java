package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.networking.RenderingStatusSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.WLEffects;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {
    @Shadow @Final private Entity entity;

    @Shadow private int tickCount;

    @Shadow @Final private int updateInterval;

    @Shadow @Final private Consumer<Packet<?>> broadcast;

    @Shadow @Final private ServerLevel level;

    @Inject(method = "sendChanges", at = @At("HEAD"))
    private void sendChanges(CallbackInfo pCallbackInfo) {
        ServerEntity pThis = (ServerEntity) (Object) this;

        if (this.tickCount % this.updateInterval == 0 || this.entity.hasImpulse || this.entity.getEntityData().isDirty()) {
            if (this.entity instanceof LivingEntity pLivingEntity) {
                Level pLevel = this.level;

                if (pLevel instanceof ServerLevel pServerLevel) {
                    pServerLevel.getChunkSource().broadcast(pLivingEntity, new RenderingStatusSyncS2CPacket(
                            pLivingEntity.getId(),
                            (pLivingEntity.hasEffect(WLEffects.DRAGONS_ASCENSION) && pLivingEntity.getEffect(WLEffects.DRAGONS_ASCENSION).getAmplifier() >= 1),
                            pLivingEntity.hasEffect(WLEffects.PHANTASMAL)));
                }
            }
        }
    }
}