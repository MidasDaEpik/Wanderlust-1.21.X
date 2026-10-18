package com.midasdaepik.wanderlust.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BaseBowItem extends BowItem {
    public BowItemStats bowItemStats;

    public BaseBowItem(Properties pProperties, BowItemStats pBowItemStats) {
        super(pProperties);
        bowItemStats = pBowItemStats;
    }

    public abstract void playShootSound(ItemStack pItemstack, Level pLevel, Player pPlayer, float pArrowPower, int pTimeLeft);

    public abstract void modifyArrow(Level pLevel, AbstractArrow pArrow, ArrowItem pArrowItem, LivingEntity pLivingEntity, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit);

    public void modifyArrowBeforeLaunch(Level pLevel, Projectile pProjectile, LivingEntity pLivingEntity, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit, int pTimeUsed) {

    }

    public boolean isCrit(float pPower) {
        return pPower >= 1.0F;
    }

    @Override
    public int getDefaultProjectileRange() {
        return (int) Math.ceil(15 * bowItemStats.projectileVelocityModifier);
    }

    //Modify Firing & Sound & Minimum Draw Time Here
    @Override
    public void releaseUsing(ItemStack pItemstack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player pPlayer) {
            ItemStack ProjectileItemStack = pPlayer.getProjectile(pItemstack);
            if (!ProjectileItemStack.isEmpty()) {
                int i = this.getUseDuration(pItemstack, pEntityLiving) - pTimeLeft;
                i = EventHooks.onArrowLoose(ProjectileItemStack, pLevel, pPlayer, i, !ProjectileItemStack.isEmpty());
                if (i < 0) return;
                float f = bowItemStats.getPowerForTime(i);
                if (!((double) f < 0.1 / bowItemStats.projectileDrawSpeedModifier)) {
                    List<ItemStack> list = draw(pItemstack, ProjectileItemStack, pPlayer);
                    if (pLevel instanceof ServerLevel serverlevel && !list.isEmpty()) {
                        this.shoot(serverlevel, pPlayer, pPlayer.getUsedItemHand(), pItemstack, list,
                                f * 3.0F * bowItemStats.projectileVelocityModifier, bowItemStats.projectileAccuracyModifier, isCrit(f), null, i);
                    }
                    playShootSound(pItemstack, pLevel, pPlayer, Math.min(f, 1) * 3.0F, pTimeLeft);
                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    protected void shoot(ServerLevel pServerLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pWeapon, List<ItemStack> pProjectileItems, float pVelocity, float pInaccuracy, boolean pIsCrit, @Nullable LivingEntity pTarget, int pUseDuration) {
        float f = EnchantmentHelper.processProjectileSpread(pServerLevel, pWeapon, pShooter, 0.0F);
        float f1 = pProjectileItems.size() == 1 ? 0.0F : 2.0F * f / (float) (pProjectileItems.size() - 1);
        float f2 = (float)((pProjectileItems.size() - 1) % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for (int i = 0; i < pProjectileItems.size(); i++) {
            ItemStack pAmmo = pProjectileItems.get(i);
            if (!pAmmo.isEmpty()) {
                float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
                f3 = -f3;
                Projectile pProjectile = this.createProjectile(pServerLevel, pShooter, pWeapon, pAmmo, pIsCrit);
                this.shootProjectile(pShooter, pProjectile, i, pVelocity, pInaccuracy, f4, pTarget);
                modifyArrowBeforeLaunch(pServerLevel, pProjectile, pShooter, pWeapon, pAmmo, pIsCrit, pUseDuration);
                pServerLevel.addFreshEntity(pProjectile);
                pWeapon.hurtAndBreak(this.getDurabilityUse(pAmmo), pShooter, LivingEntity.getSlotForHand(pHand));
                if (pWeapon.isEmpty()) {
                    break;
                }
            }
        }
    }

    //Modfy Projectile Stats Here
    @Override
    protected Projectile createProjectile(Level pLevel, LivingEntity pLivingEntity, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit) {
        ArrowItem arrowitem = pAmmo.getItem() instanceof ArrowItem arrowitem1 ? arrowitem1 : (ArrowItem)Items.ARROW;
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, pAmmo, pLivingEntity, pWeapon);
        if (pIsCrit) {
            abstractarrow.setCritArrow(true);
        }
        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() * bowItemStats.projectileDamageModifier);
        modifyArrow(pLevel, abstractarrow, arrowitem, pLivingEntity, pWeapon, pAmmo, pIsCrit);
        return customArrow(abstractarrow, pAmmo, pWeapon);
    }

    public static abstract class BowItemStats {
        public float projectileDamageModifier;
        public float projectileVelocityModifier;
        public float projectileDrawSpeedModifier;
        public float projectileAccuracyModifier;

        public BowItemStats(float pDamageModifier, float pVelocityModifier, float pDrawSpeedModifier, float pAccuracyModifier) {
            projectileDamageModifier = pDamageModifier;
            projectileVelocityModifier = pVelocityModifier;
            projectileDrawSpeedModifier = pDrawSpeedModifier;
            projectileAccuracyModifier = pAccuracyModifier;
        }

        //Modify Draw Time Here
        public float getPowerForTime(int pTimeUsed) {
            float pValue = (float) pTimeUsed / (20.0F * projectileDrawSpeedModifier);
            pValue = (pValue * pValue + pValue * 2.0F) / 3.0F;
            if (pValue > 1.0F) {
                pValue = 1.0F;
            }
            return pValue;
        }
    }
}
