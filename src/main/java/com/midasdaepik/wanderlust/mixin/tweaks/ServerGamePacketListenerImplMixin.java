package com.midasdaepik.wanderlust.mixin.tweaks;

import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import com.midasdaepik.wanderlust.mixin.LivingEntityAccessor;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @Inject(method = "handlePlayerAction", at = @At("TAIL"))
    private void handlePlayerAction(ServerboundPlayerActionPacket pPacket, CallbackInfo pCallbackInfo) {
        if (WLTweaksConfig.CONFIG.AttributeSwappingFix.get() && pPacket.getAction() == SWAP_ITEM_WITH_OFFHAND) {
            player.resetAttackStrengthTicker();
            ((LivingEntityAccessor) player).collectEquipmentChangesInvoker();
        }
    }

    @Inject(method = "handleSetCarriedItem", at = @At("TAIL"))
    private void handleSetCarriedItem(ServerboundSetCarriedItemPacket pPacket, CallbackInfo pCallbackInfo) {
        if (WLTweaksConfig.CONFIG.AttributeSwappingFix.get()) {
            player.resetAttackStrengthTicker();
            ((LivingEntityAccessor) player).collectEquipmentChangesInvoker();
        }
    }
}