package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TomeOfEvocation extends Item {
    public TomeOfEvocation(Properties pProperties) {
        super(pProperties.durability(256).rarity(WLEnumExtensions.RARITY_SCULK.getValue()));
    }

    @Override
    public int getEnchantmentValue(ItemStack pItemStack) {
        return 10;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pItemStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.PAPER);
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (pTimeUsing >= 40) {
            double pMinY = pLivingEntity.getY() - 3;
            double pMaxY = pLivingEntity.getY() + 3;
            float pViewAngle = (float) Mth.atan2(pLivingEntity.getLookAngle().z, pLivingEntity.getLookAngle().x);
            if (pLivingEntity.isCrouching()) {
                for(int i = 0; i < 5; ++i) {
                    float pOffset = pViewAngle + i * (float) Math.PI * 0.4F;
                    createSpellEntity(pLevel, pLivingEntity, pLivingEntity.getX() + Mth.cos(pOffset) * 1.5F, pLivingEntity.getZ() + Mth.sin(pOffset) * 1.5F, pMinY, pMaxY, pOffset, 4);
                }

                for(int k = 0; k < 8; ++k) {
                    float pOffset = pViewAngle + k * (float) Math.PI / 4.0F + 1.2566371F;
                    createSpellEntity(pLevel, pLivingEntity, pLivingEntity.getX() + Mth.cos(pOffset) * 2.5F, pLivingEntity.getZ() + Mth.sin(pOffset) * 2.5F, pMinY, pMaxY, pOffset, 8);
                }
            } else {
                for(int l = 0; l < 16; ++l) {
                    double pOffset = 1.25 * (l + 1);
                    createSpellEntity(pLevel, pLivingEntity, pLivingEntity.getX() + Mth.cos(pViewAngle) * pOffset, pLivingEntity.getZ() + Mth.sin(pViewAngle) * pOffset, pMinY, pMaxY, pViewAngle, l + 6);
                }
            }

            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 3));

            if (pLivingEntity instanceof Player pPlayer) {
                pItemStack.hurtAndBreak(1, pLivingEntity, pLivingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

                pPlayer.awardStat(Stats.ITEM_USED.get(this));

                pPlayer.getCooldowns().addCooldown(this, 120);
            }
        }
    }

    private void createSpellEntity(Level pLevel, LivingEntity pLivingEntity, double pX, double pZ, double pMinY, double pMaxY, float pYRot, int pWarmupDelay) {
        Vec3 pLocation = WLUtil.takeHighestPoint(pLevel, pX, pZ, pMinY, pMaxY);
        if (pLocation != null) {
            pLevel.addFreshEntity(new EvokerFangs(pLevel, pLocation.x, pLocation.y, pLocation.z, pYRot, pWarmupDelay, pLivingEntity));
            pLevel.gameEvent(GameEvent.ENTITY_PLACE, new Vec3(pLocation.x, pLocation.y, pLocation.z), GameEvent.Context.of(pLivingEntity));
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            if (pTimeUsing >= 40) {
                pServerLevel.sendParticles(ParticleTypes.WHITE_ASH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.05);
            }
        }

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.tome_of_evocation.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.tome_of_evocation.shift_desc_2"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.tome_of_evocation.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.tome_of_evocation.shift_desc_4"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
