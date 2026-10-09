package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.networking.KatanaAbilitySyncS2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

public class KatanaItem extends SwordItem {
    public KatanaItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public int maxCombo() {
        return 9;
    }

    public int abilityMaxCombo() {
        return 9;
    }

    @Override
    public boolean hurtEnemy(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pPlayer) {
            if (pPlayer.getAttackStrengthScale(0) >= 0.9F) {
                attackEffects(pTarget, pAttacker);

                if (pPlayer.level() instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                    boolean KatanaAbilityActive = pPlayer.getData(KATANA_ABILITY_ACTIVE);
                    int KatanaCombo = pPlayer.getData(KATANA_COMBO);
                    if (KatanaAbilityActive) {
                        KatanaCombo += 1;
                        abilityComboEffects(pServerLevel, pItemStack, pTarget, pAttacker, KatanaCombo);
                        if (KatanaCombo == abilityMaxCombo()) {
                            KatanaCombo = 0;
                            KatanaAbilityActive = false;
                            pPlayer.setData(KATANA_ABILITY_ACTIVE, KatanaAbilityActive);
                        }
                        pPlayer.setData(KATANA_COMBO, KatanaCombo);
                        PacketDistributor.sendToPlayer(pServerPlayer, new KatanaAbilitySyncS2CPacket(KatanaCombo, KatanaAbilityActive, pPlayer.getData(KATANA_ABILITY_AVAILABLE_TIMER)));
                    } else {
                        int KatanaAbilityAvailableTimer = pPlayer.getData(KATANA_ABILITY_AVAILABLE_TIMER);
                        KatanaCombo = Math.min(KatanaCombo + 1, maxCombo());
                        if (KatanaCombo == maxCombo()) {
                            KatanaAbilityAvailableTimer = 600;
                            pPlayer.setData(KATANA_ABILITY_AVAILABLE_TIMER, KatanaAbilityAvailableTimer);
                            pPlayer.setData(KATANA_ABILITY_CHARGE_LEVEL, maxCombo());
                        }
                        pPlayer.setData(KATANA_COMBO, KatanaCombo);
                        PacketDistributor.sendToPlayer(pServerPlayer, new KatanaAbilitySyncS2CPacket(KatanaCombo, false, KatanaAbilityAvailableTimer));
                    }
                    pPlayer.setData(KATANA_COMBO_DECAY, 200);
                }
            }
        } else {
            attackEffects(pTarget, pAttacker);
        }

        return super.hurtEnemy(pItemStack, pTarget, pAttacker);
    }

    public double reachModifier(Player pPlayer, double pOriginal) {
        return pOriginal;
    }

    @Override
    public float getAttackDamageBonus(Entity pTarget, float pDamage, DamageSource pDamageSource) {
        return calculateAttackDamageBonus(pTarget, pDamage, pDamageSource, super.getAttackDamageBonus(pTarget, pDamage, pDamageSource));
    }

    public float calculateAttackDamageBonus(Entity pTarget, float pDamage, DamageSource pDamageSource, float pSuper) {
        return pSuper;
    }

    public void attackEffects(LivingEntity pTarget, LivingEntity pAttacker) {

    }

    public void abilityComboEffects(ServerLevel pServerLevel, ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker, int pCombo) {

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (pPlayer.isCrouching() && pPlayer.getData(KATANA_ABILITY_AVAILABLE_TIMER) > 0 && pPlayer.getData(KATANA_ABILITY_CHARGE_LEVEL) >= maxCombo()) {
            pPlayer.setData(KATANA_COMBO, 0);
            pPlayer.setData(KATANA_COMBO_DECAY, 200);
            pPlayer.setData(KATANA_ABILITY_ACTIVE, true);
            pPlayer.setData(KATANA_ABILITY_AVAILABLE_TIMER, 0);
            if (pPlayer instanceof ServerPlayer pServerPlayer) {
                PacketDistributor.sendToPlayer(pServerPlayer, new KatanaAbilitySyncS2CPacket(0, true, 0));
            }

            successfulUse(pLevel, pPlayer, pHand);

            pPlayer.getItemInHand(pHand).hurtAndBreak(5, pPlayer, pHand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);

            pPlayer.awardStat(Stats.ITEM_USED.get(this));

            pPlayer.getCooldowns().addCooldown(this, 20);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
        } else {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
        }
    }

    public void successfulUse(Level pLevel, Player pPlayer, InteractionHand pHand) {

    }
}
