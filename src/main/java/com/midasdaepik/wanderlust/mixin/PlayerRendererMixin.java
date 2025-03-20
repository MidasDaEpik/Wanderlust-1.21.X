package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void dualWieldItem(AbstractClientPlayer pPlayer, InteractionHand pHand, CallbackInfoReturnable<HumanoidModel.ArmPose> pReturn) {
        ItemStack pItemStack = pPlayer.getItemInHand(pHand);
        if (pItemStack.is(WLTags.TWO_HANDED_WEAPONS) && pHand == InteractionHand.MAIN_HAND) {
            if (pPlayer.isUsingItem()) {
                pReturn.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
            } else {
                pReturn.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_CHARGE);
            }
        }
    }
}