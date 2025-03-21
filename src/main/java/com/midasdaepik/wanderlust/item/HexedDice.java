package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class HexedDice extends Item {
    public HexedDice(Properties pProperties) {
        super(pProperties.durability(16).rarity(WLEnumExtensions.RARITY_GOLD.getValue()));
    }

    @Override
    public int getEnchantmentValue(ItemStack pItemStack) {
        return 22;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pPlayer.level().isClientSide()) {
            int random_number = Mth.nextInt(RandomSource.create(), 1, 6);
            if (random_number == 1) {
                pPlayer.addEffect(new MobEffectInstance(WLEffects.HEXED_DICE_ONE, 2400, 0, false, false));
            } else if (random_number == 2) {
                pPlayer.addEffect(new MobEffectInstance(WLEffects.HEXED_DICE_TWO, 2400, 0, false, false));
            } else if (random_number == 3) {
                pPlayer.addEffect(new MobEffectInstance(WLEffects.HEXED_DICE_THREE, 2400, 0, false, false));
            } else if (random_number == 4) {
                pPlayer.addEffect(new MobEffectInstance(WLEffects.HEXED_DICE_FOUR, 2400, 0, false, false));
            } else if (random_number == 5) {
                pPlayer.addEffect(new MobEffectInstance(WLEffects.HEXED_DICE_FIVE, 2400, 0, false, false));
            } else if (random_number == 6) {
                pPlayer.addEffect(new MobEffectInstance(WLEffects.HEXED_DICE_SIX, 2400, 0, false, false));
            }
            pPlayer.displayClientMessage(Component.literal(("§6" + random_number)), true);
        }
        pLevel.playLocalSound(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 1, 1.2f, false);

        pPlayer.getItemInHand(pUsedHand).hurtAndBreak(1, pPlayer, pUsedHand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);

        pPlayer.awardStat(Stats.ITEM_USED.get(this));

        pPlayer.getCooldowns().addCooldown(this, 2400);
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.hexed_dice.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.hexed_dice.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.hexed_dice.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.hexed_dice.shift_desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.hexed_dice.shift_desc_5"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.hexed_dice.shift_desc_6"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.hexed_dice.shift_desc_7"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
