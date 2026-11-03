package com.midasdaepik.wanderlust.item.extensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface OnHitExtension {
    //These are executed after the acctual hit btw
    default void onHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack, boolean pIsSweep, boolean pIsCrit, boolean pIsStrongHit) {

    }

    default void onSweepHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {

    }

    default void onCritHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {

    }

    default void onStrongHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {

    }

    default void onWeakHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {

    }
}
