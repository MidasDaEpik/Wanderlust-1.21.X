package com.midasdaepik.wanderlust.item.extensions;

import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public interface HitParticleExtension {
    //These are executed after the actual hit btw
    default void particleSweepHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {
        if (pItemStack.is(WLTags.WEAPON_MODIFIER_PIERCE)) {
            WLHitParticleUtil.thrustHit(pServerLevel, pPlayer, pItemStack,
                    particleThrustSize(pPlayer, pItemStack), particleColor(pPlayer, pItemStack));
        } else {
            WLHitParticleUtil.sweepHit(pServerLevel, pPlayer, pItemStack,
                    particleSweepSize(pPlayer, pItemStack), particleColor(pPlayer, pItemStack), particleTypeSweep(pPlayer, pItemStack));
        }
    }

    default void particleCritHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {
        WLHitParticleUtil.critHit(pServerLevel, pPlayer, pItemStack,
                particleCritSize(pPlayer, pItemStack), particleColor(pPlayer, pItemStack));
    }

    default void particleStrongHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {
        if (pItemStack.is(WLTags.WEAPON_MODIFIER_CLEAVE)) {
            WLHitParticleUtil.sweepHit(pServerLevel, pPlayer, pItemStack,
                    particleSweepSize(pPlayer, pItemStack), particleColor(pPlayer, pItemStack), particleTypeSweep(pPlayer, pItemStack));
        } else {
            WLHitParticleUtil.thrustHit(pServerLevel, pPlayer, pItemStack,
                    particleThrustSize(pPlayer, pItemStack), particleColor(pPlayer, pItemStack));
        }
    }

    default void particleWeakHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {

    }

    default float particleSweepSize(Player pPlayer, ItemStack pItemStack) {
        return WLHitParticleUtil.defaultSweepSize(pPlayer, pItemStack);
    }

    default float particleCritSize(Player pPlayer, ItemStack pItemStack) {
        return WLHitParticleUtil.defaultCritSize(pPlayer, pItemStack);
    }

    default float particleThrustSize(Player pPlayer, ItemStack pItemStack) {
        return WLHitParticleUtil.defaultThrustSize(pPlayer, pItemStack);
    }

    default Vector3f particleColor(Player pPlayer, ItemStack pItemStack) {
        return WLHitParticleUtil.defaultParticleColor(pPlayer, pItemStack);
    }

    default int particleTypeSweep(Player pPlayer, ItemStack pItemStack) {
        return WLHitParticleUtil.defaultSweepParticle(pPlayer, pItemStack);
    }
}
