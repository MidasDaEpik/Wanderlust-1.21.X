package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.entity.NoDamageFireball;
import com.midasdaepik.wanderlust.registries.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SearingStaff extends Item {
    public SearingStaff(Properties pProperties) {
        super(pProperties.durability(128).rarity(WLEnumExtensions.RARITY_BLAZE.getValue()));
    }

    @Override
    public int getEnchantmentValue(ItemStack pItemStack) {
        return 12;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pItemstack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.BLAZE_ROD);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (!pLevel.isClientSide) {
            NoDamageFireball fireballMid = new NoDamageFireball(pLevel, pPlayer, new Vec3(0, 0, 0), 400, 400, 2);
            fireballMid.setPos(pPlayer.getX() + Mth.sin(pPlayer.getYRot() * ((float)Math.PI / 180F) - (float)Math.PI) * 2, pPlayer.getEyePosition().y, pPlayer.getZ() + Mth.cos(pPlayer.getYRot() * ((float)Math.PI / 180F) - (float)Math.PI) * -2);
            pLevel.addFreshEntity(fireballMid);

            NoDamageFireball fireballPos45 = new NoDamageFireball(pLevel, pPlayer, new Vec3(0, 0, 0), 400, 400, 2);
            fireballPos45.setPos(pPlayer.getX() + Mth.sin((pPlayer.getYRot() + 45) * ((float)Math.PI / 180F) - (float)Math.PI) * 2, pPlayer.getEyePosition().y, pPlayer.getZ() + Mth.cos((pPlayer.getYRot() + 45) * ((float)Math.PI / 180F) - (float)Math.PI) * -2);
            pLevel.addFreshEntity(fireballPos45);

            NoDamageFireball fireballNeg45 = new NoDamageFireball(pLevel, pPlayer, new Vec3(0, 0, 0), 400, 400, 2);
            fireballNeg45.setPos(pPlayer.getX() + Mth.sin((pPlayer.getYRot() - 45) * ((float)Math.PI / 180F) - (float)Math.PI) * 2, pPlayer.getEyePosition().y, pPlayer.getZ() + Mth.cos((pPlayer.getYRot() - 45) * ((float)Math.PI / 180F) - (float)Math.PI) * -2);
            pLevel.addFreshEntity(fireballNeg45);
        }

        pPlayer.level().playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, WLSounds.ITEM_SEARING_STAFF_SUMMON, SoundSource.PLAYERS, 1f, 1f,0);

        pPlayer.getItemInHand(pHand).hurtAndBreak(1, pPlayer, pHand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);

        pPlayer.awardStat(Stats.ITEM_USED.get(this));

        pPlayer.getCooldowns().addCooldown(this, 160);
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pHand));
    }

    @Override
    public void appendHoverText(ItemStack pItemstack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.searing_staff.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.searing_staff.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.searing_staff.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.searing_staff.shift_desc_4"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        super.appendHoverText(pItemstack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
