package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.config.WLAbilitiesConfig;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.SPECIAL_ARROW_TYPE;

public class Sovereign extends BaseBowItem {
    public Sovereign(Properties pProperties) {
        super(pProperties.durability(WLAttributeConfig.CONFIG.ItemSovereignDurability.get()).rarity(WLEnumExtensions.RARITY_UMBRA.getValue()),
                SovereignStats.INSTANCE);
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pItemstack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(WLItems.UMBRA);
    }

    @Override
    public void playShootSound(ItemStack pItemstack, Level pLevel, Player pPlayer, float pArrowPower, int pTimeLeft) {
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + pArrowPower * 0.5F);
    }

    @Override
    public void modifyArrow(Level pLevel, AbstractArrow pArrow, ArrowItem pArrowItem, LivingEntity pLivingEntity, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit) {
        pArrow.setData(SPECIAL_ARROW_TYPE, 2);
    }

    @Override
    public void modifyArrowBeforeLaunch(Level pLevel, Projectile pProjectile, LivingEntity pLivingEntity, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit, int pTimeUsed) {
        if (pProjectile instanceof AbstractArrow pArrow) {
            float pValue = (float) pTimeUsed / (20.0F * bowItemStats.projectileDrawSpeedModifier);
            if (pValue > 1.0F) {
                pValue = (Math.min(pValue, 3.0F) - 1) / 2;
                pArrow.setBaseDamage(pArrow.getBaseDamage() * (1 + pValue * WLAbilitiesConfig.CONFIG.ItemSovereignOverchargeDamageModifier.get().floatValue()));
            }
        }
    }

    public static final class SovereignStats extends BowItemStats {
        public static final Sovereign.SovereignStats INSTANCE = new Sovereign.SovereignStats(
                WLAttributeConfig.CONFIG.ItemSovereignDamageModifier.get().floatValue(),
                WLAttributeConfig.CONFIG.ItemSovereignVelocityModifier.get().floatValue(),
                WLAttributeConfig.CONFIG.ItemSovereignDrawSpeedModifier.get().floatValue(),
                WLAttributeConfig.CONFIG.ItemSovereignAccuracyModifier.get().floatValue());

        public SovereignStats(float pDamageModifier, float pVelocityModifier, float pDrawSpeedModifier, float pAccuracyModifier) {
            super(pDamageModifier, pVelocityModifier, pDrawSpeedModifier, pAccuracyModifier);
        }
    }
}
