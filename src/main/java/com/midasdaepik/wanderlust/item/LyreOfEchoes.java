package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLUtil;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LyreOfEchoes extends Item {
    public LyreOfEchoes(Properties pProperties) {
        super(pProperties.durability(128).rarity(WLEnumExtensions.RARITY_SCULK.getValue()));
    }

    @Override
    public int getEnchantmentValue(ItemStack pItemStack) {
        return 10;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pItemStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.ECHO_SHARD);
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemStack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (pTimeUsing >= 300) {
            pTimeUsing = 300;
        }

        if (pTimeUsing >= 60) {
            double AABBCenterX = pLivingEntity.getEyePosition().x + pLivingEntity.getLookAngle().x;
            double AABBCenterY = pLivingEntity.getEyePosition().y + pLivingEntity.getLookAngle().y;
            double AABBCenterZ = pLivingEntity.getEyePosition().z + pLivingEntity.getLookAngle().z;
            Set<LivingEntity> pFoundTarget = new HashSet<>(Set.of());
            for (int Loop = 1; Loop <= pTimeUsing / 5 + 20; Loop++) {
                pFoundTarget.addAll(new HashSet<>(pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenterX, AABBCenterY, AABBCenterZ, AABBCenterX, AABBCenterY, AABBCenterZ).inflate(1.5d))));

                if (pLevel instanceof ServerLevel pServerLevel && Loop % 4 == 0) {
                    pServerLevel.sendParticles(ParticleTypes.SONIC_BOOM, AABBCenterX, AABBCenterY, AABBCenterZ, 1, 0, 0, 0, 0);
                }

                AABBCenterX += pLivingEntity.getLookAngle().x * 0.5;
                AABBCenterY += pLivingEntity.getLookAngle().y * 0.5;
                AABBCenterZ += pLivingEntity.getLookAngle().z * 0.5;
            }

            int pDamage = pTimeUsing / 10 + 10;
            for (LivingEntity pEntityIterator : pFoundTarget) {
                pEntityIterator.hurt(WLDamageSource.damageSource(pLevel, pLivingEntity, WLDamageSource.SONIC_BOOM), pDamage);
            }

            pLivingEntity.setDeltaMovement(pLivingEntity.getDeltaMovement().x - pLivingEntity.getLookAngle().x * pTimeUsing * 0.006, pLivingEntity.getDeltaMovement().y - pLivingEntity.getLookAngle().y * pTimeUsing * 0.006, pLivingEntity.getDeltaMovement().z - pLivingEntity.getLookAngle().z * pTimeUsing * 0.006);

            pLevel.playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_LYRE_OF_ECHOES_SONIC_BOOM, SoundSource.PLAYERS, 2f, 1.2f - pTimeUsing * 0.001f,0);

            if (pLivingEntity instanceof Player pPlayer) {
                pItemStack.hurtAndBreak(1, pLivingEntity, pLivingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

                pPlayer.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            if (pTimeUsing >= 60 && pTimeUsing <= 180) {
                pServerLevel.sendParticles(ParticleTypes.SCULK_CHARGE_POP, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.05);
            }
            if (pTimeUsing >= 180) {
                pServerLevel.sendParticles(ParticleTypes.SCULK_SOUL, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.05);
            }
            if (pTimeUsing >= 300) {
                pServerLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0);
            }
        }

        if (pTimeUsing % 10 == 0) {
            int NoteNumber = (pTimeUsing / 10) % 6;
            if (NoteNumber <= 2) {
                if (NoteNumber == 0) {
                    pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.ITEM_LYRE_OF_ECHOES_NOTE, SoundSource.RECORDS, 1.5f, 0.561231f,0);
                } else if (NoteNumber == 1) {
                    pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.ITEM_LYRE_OF_ECHOES_NOTE, SoundSource.RECORDS, 1.5f, 0.667420f,0);
                } else {
                    pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.ITEM_LYRE_OF_ECHOES_NOTE, SoundSource.RECORDS, 1.5f, 0.840896f,0);
                }
            } else {
                if (NoteNumber == 3) {
                    pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.ITEM_LYRE_OF_ECHOES_NOTE, SoundSource.RECORDS, 1.5f, 0.890899f,0);
                } else if (NoteNumber == 4) {
                    pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.ITEM_LYRE_OF_ECHOES_NOTE, SoundSource.RECORDS, 1.5f, 0.840896f,0);
                } else {
                    pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.ITEM_LYRE_OF_ECHOES_NOTE, SoundSource.RECORDS, 1.5f, 0.667420f,0);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.lyre_of_echoes.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.lyre_of_echoes.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.lyre_of_echoes.shift_desc_3"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
