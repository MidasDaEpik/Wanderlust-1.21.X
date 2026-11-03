package com.midasdaepik.wanderlust.item.extensions;

import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.particle.slash.*;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class WLHitParticleUtil {
    public static void sweepHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {
        sweepHit(pServerLevel, pPlayer, pItemStack, defaultSweepSize(pPlayer, pItemStack), defaultParticleColor(pPlayer, pItemStack), defaultSweepParticle(pPlayer, pItemStack));
    }

    public static void sweepHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack, float pSize, Vector3f pParticleColor, int pSweepParticle) {
        Vec3 pLookAngle = pPlayer.getLookAngle();
        float pPitch = (float) Math.atan2(pLookAngle.y, Math.sqrt((float) (Math.pow(pLookAngle.x, 2) + Math.pow(pLookAngle.z, 2))));
        float pYaw = (float) Math.atan2(pLookAngle.x, pLookAngle.z);

        pSize = Math.clamp(sizeCap(pServerLevel, pPlayer, pItemStack, pSize), 0.25f, pSize);

        switch (pSweepParticle) {
            case 0 -> {
                pServerLevel.sendParticles(
                        new SlowSweepOptions(pParticleColor, pSize, Math.random() <= 0.5, pPitch, pYaw),
                        pPlayer.getX() + pLookAngle.x * pSize, pPlayer.getY(0.55 + Math.random() * 0.1) + pLookAngle.y * pSize, pPlayer.getZ() + pLookAngle.z * pSize,
                        1, 0, 0, 0, 0);
            }
            case 1 -> {
                pServerLevel.sendParticles(
                        new MediumSweepOptions(pParticleColor, pSize, Math.random() <= 0.5, pPitch, pYaw),
                        pPlayer.getX() + pLookAngle.x * pSize, pPlayer.getY(0.55 + Math.random() * 0.1) + pLookAngle.y * pSize, pPlayer.getZ() + pLookAngle.z * pSize,
                        1, 0, 0, 0, 0);
            }
            case 2 -> {
                pServerLevel.sendParticles(
                        new FastSweepOptions(pParticleColor, pSize, Math.random() <= 0.5, pPitch, pYaw),
                        pPlayer.getX() + pLookAngle.x * pSize, pPlayer.getY(0.55 + Math.random() * 0.1) + pLookAngle.y * pSize, pPlayer.getZ() + pLookAngle.z * pSize,
                        1, 0, 0, 0, 0);
            }
        }
    }

    public static void critHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {
        critHit(pServerLevel, pPlayer, pItemStack, defaultCritSize(pPlayer, pItemStack), defaultParticleColor(pPlayer, pItemStack));
    }

    public static void critHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack, float pSize, Vector3f pParticleColor) {
        Vec3 pLookAngle = pPlayer.getLookAngle();
        float pPitch = (float) Math.atan2(pLookAngle.y, Math.sqrt((float) (Math.pow(pLookAngle.x, 2) + Math.pow(pLookAngle.z, 2))));
        float pYaw = (float) Math.atan2(pLookAngle.x, pLookAngle.z);

        pSize = Math.clamp(sizeCap(pServerLevel, pPlayer, pItemStack, pSize), 0.25f, pSize);

        pServerLevel.sendParticles(
                new CritOptions(pParticleColor, pSize, pPitch, pYaw),
                pPlayer.getX() + pLookAngle.x * pSize, pPlayer.getY(0.7 + Math.random() * 0.1) + pLookAngle.y * pSize, pPlayer.getZ() + pLookAngle.z * pSize,
                1, 0, 0, 0, 0);
    }

    public static void roundaboutHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {
        roundaboutHit(pServerLevel, pPlayer, pItemStack, defaultRoundaboutSize(pPlayer, pItemStack), defaultParticleColor(pPlayer, pItemStack));
    }

    public static void roundaboutHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack, float pSize, Vector3f pParticleColor) {
        Vec3 pLookAngle = pPlayer.getLookAngle();
        float pPitch = (float) Math.atan2(pLookAngle.y, Math.sqrt((float) (Math.pow(pLookAngle.x, 2) + Math.pow(pLookAngle.z, 2))));
        float pYaw = (float) Math.atan2(pLookAngle.x, pLookAngle.z);

        pSize = Math.clamp(sizeCap(pServerLevel, pPlayer, pItemStack, pSize), 0.25f, pSize);

        pServerLevel.sendParticles(
                new RoundaboutSweepOptions(pParticleColor, pSize, Math.random() <= 0.5, pPitch, pYaw),
                pPlayer.getX() + pLookAngle.x * pSize, pPlayer.getY(0.55 + Math.random() * 0.1) + pLookAngle.y * pSize, pPlayer.getZ() + pLookAngle.z * pSize,
                1, 0, 0, 0, 0);
    }

    public static void thrustHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {
        thrustHit(pServerLevel, pPlayer, pItemStack, defaultThrustSize(pPlayer, pItemStack), defaultParticleColor(pPlayer, pItemStack));
    }
    public static void thrustHit(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack, float pSize, Vector3f pParticleColor) {
        Vec3 pLookAngle = pPlayer.getLookAngle();
        float pPitch = (float) Math.atan2(pLookAngle.y, Math.sqrt((float) (Math.pow(pLookAngle.x, 2) + Math.pow(pLookAngle.z, 2))));
        float pYaw = (float) Math.atan2(pLookAngle.x, pLookAngle.z);

        pSize = Math.clamp(sizeCap(pServerLevel, pPlayer, pItemStack, pSize), 0.25f, pSize);

        pServerLevel.sendParticles(
                new ThrustOptions(pParticleColor, pSize, pPitch, pYaw),
                pPlayer.getX() + pLookAngle.x * pSize * 1.5, pPlayer.getY(0.55 + Math.random() * 0.1) + pLookAngle.y * pSize * 1.5, pPlayer.getZ() + pLookAngle.z * pSize * 1.5,
                1, 0, 0, 0, 0);
    }

    public static float sizeCap(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack, float pSize) {
        Vec3 pHitPos = WLUtil.blockRaycast(pServerLevel, pPlayer, ClipContext.Fluid.NONE, pSize * 4).getBlockPos().getCenter();
        Vec3 pStartPos = pPlayer.getEyePosition();
        float pDistance = (float) Math.sqrt(Math.pow(pHitPos.x - pStartPos.x, 2) + Math.pow(pHitPos.y - pStartPos.y, 2) + Math.pow(pHitPos.z - pStartPos.z, 2));
        return pDistance / 4;
    }

    public static float defaultSweepSize(Player pPlayer, ItemStack pItemStack) {
        return (float) Math.min(pPlayer.entityInteractionRange() / 3, 4);
    }

    public static float defaultCritSize(Player pPlayer, ItemStack pItemStack) {
        return (float) Math.min(pPlayer.entityInteractionRange() / 3, 4);
    }

    public static float defaultRoundaboutSize(Player pPlayer, ItemStack pItemStack) {
        return (float) Math.min(pPlayer.entityInteractionRange() / 2, 4);
    }

    public static float defaultThrustSize(Player pPlayer, ItemStack pItemStack) {
        if (pItemStack.is(WLTags.WEAPON_MODIFIER_PIERCE)) {
            return (float) Math.min((pPlayer.entityInteractionRange() + 1) / 3, 4);
        }
        return (float) Math.min(pPlayer.entityInteractionRange() / 3, 4);
    }

    public static Vector3f defaultParticleColor(Player pPlayer, ItemStack pItemStack) {
        return new Vector3f(1, 1, 1);
    }

    public static int defaultSweepParticle(Player pPlayer, ItemStack pItemStack) {
        double pAttackSpeed = pPlayer.getAttribute(Attributes.ATTACK_SPEED).getValue();
        if (pAttackSpeed >= 1.8) {
            return 2;
        } else if (pAttackSpeed <= 1) {
            return 0;
        } else {
            return 1;
        }
    }
}
