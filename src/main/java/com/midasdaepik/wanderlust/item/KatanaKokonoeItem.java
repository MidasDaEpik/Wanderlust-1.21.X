package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.misc.WLUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.KATANA_ABILITY_ACTIVE;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.KATANA_COMBO;

public class KatanaKokonoeItem extends KatanaItem {
    public KatanaKokonoeItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public double reachModifier(Player pPlayer, double pOriginal) {
        if (pPlayer.getData(KATANA_ABILITY_ACTIVE) && pPlayer.getAttackStrengthScale(0) >= 0.9F && pPlayer.getData(KATANA_COMBO) % 3 == 0) {
            return pOriginal * dashStrikeReachModifier();
        } else {
            return pOriginal;
        }
    }

    public float dashStrikeReachModifier() {
        return 5f;
    }

    @Override
    public float calculateAttackDamageBonus(Entity pTarget, float pDamage, DamageSource pDamageSource, float pSuper) {
        return pSuper;
    }

    @Override
    public void abilityComboEffects(ServerLevel pServerLevel, ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker, int pCombo) {
        if (pCombo % 3 == 1) {
            Vec3 pTargetLocation = pTarget.getPosition(0);
            Vec3 pAttackerLocation = pAttacker.getPosition(0);
            double pDistance = pTargetLocation.distanceTo(pAttackerLocation);
            double pReach = pAttacker instanceof Player pPlayer ? Math.max(pPlayer.entityInteractionRange() / 5 - 1, 2) : 2;

            if (pDistance > pReach) {
                pDistance -= pReach - 0.5;
                BlockHitResult pRaycast = WLUtil.blockRaycast(pServerLevel, pAttacker, ClipContext.Fluid.NONE, pDistance);
                BlockPos pLookPos = BlockPos.containing(pRaycast.getLocation()).relative(pRaycast.getDirection());
                Vec3 pTeleportPos = pLookPos.getBottomCenter();

                Vec3 pRaycastDownLocation = pAttackerLocation.add(0, -4.5, 0);
                BlockHitResult pRaycastDown = pServerLevel.clip(new ClipContext(pAttackerLocation, pRaycastDownLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pAttacker));
                double pAttackerDistanceDown = pAttackerLocation.y - pRaycastDown.getLocation().y;

                if (pAttackerDistanceDown <= 4) {
                    Vec3 pTargetPos = pRaycast.getLocation();

                    Vec3 pTargetPosRaycastDownLocation = pTargetPos.add(0, -4.5, 0);
                    BlockHitResult pTargetPosRaycastDown = pServerLevel.clip(new ClipContext(pTargetPos, pTargetPosRaycastDownLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pAttacker));
                    double pTargetPosDistanceDown = pTargetPos.y - pTargetPosRaycastDown.getLocation().y;

                    if (pTargetPosDistanceDown <= 4) {
                        double pAttackerYSize = pAttacker.getBoundingBox().getYsize();

                        Vec3 pTargetPosRaycastUpLocation = pTargetPos.add(0, 4.5 + pAttackerYSize, 0);
                        BlockHitResult pTargetPosRaycastUp = pServerLevel.clip(new ClipContext(pTargetPos, pTargetPosRaycastUpLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pAttacker));
                        double pTargetPosDistanceUp = pTargetPosRaycastUp.getLocation().y - pTargetPos.y - pAttackerYSize;

                        double pTargetPosDistance = pTargetPosDistanceDown + pTargetPosDistanceUp;

                        if (pTargetPosDistance > 0) {
                            pTeleportPos = new Vec3(pTeleportPos.x, pTargetPos.y - pTargetPosDistanceDown + Math.min(pAttackerDistanceDown, pTargetPosDistance) ,pTeleportPos.z);
                        }
                    }
                }

                pAttacker.teleportTo(pTeleportPos.x, pTeleportPos.y, pTeleportPos.z);
            }
        }
    }
}
