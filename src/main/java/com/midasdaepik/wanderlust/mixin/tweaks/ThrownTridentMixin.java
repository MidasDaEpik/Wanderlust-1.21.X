package com.midasdaepik.wanderlust.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
    public ThrownTridentMixin(EntityType<? extends ThrownTrident> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    @Final
    private static EntityDataAccessor<Byte> ID_LOYALTY;

    @Shadow
    private boolean dealtDamage;

    @Shadow
    public int clientSideReturnTridentTickCount;

    @Shadow
    private boolean isAcceptibleReturnOwner() {
        return true;
    }

    @WrapMethod(method = "getLoyaltyFromItem")
    private byte getLoyaltyFromItem(ItemStack pItemStack, Operation<Byte> pOriginal) {
        if (WLTweaksConfig.CONFIG.TridentInnateLoyalty.get() > 0) {
            return (byte) (Math.min(pOriginal.call(pItemStack) + WLTweaksConfig.CONFIG.TridentInnateLoyalty.get().byteValue(), 127));
        } else {
            return pOriginal.call(pItemStack);
        }
    }

    @WrapMethod(method = "tickDespawn")
    private void tickDespawn(Operation<Void> pOriginal) {
        if (!WLTweaksConfig.CONFIG.TridentNoDespawn.get()) {
            pOriginal.call();
        }
    }

    @Override
    protected void onBelowWorld() {
        if (WLTweaksConfig.CONFIG.TridentVoidSaving.get()) {
            if (this.entityData.get(ID_LOYALTY) > 0 && this.isAcceptibleReturnOwner()) {
                this.dealtDamage = true;
                this.setNoPhysics(true);

                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                ++this.clientSideReturnTridentTickCount;
            } else {
                super.onBelowWorld();
            }
        } else  {
            super.onBelowWorld();
        }
    }
}