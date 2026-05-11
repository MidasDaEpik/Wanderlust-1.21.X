package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLSounds;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class SelfResonantBell extends Item {
    public SelfResonantBell(Properties pProperties) {
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
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        boolean pHasTarget = false;
        final Vec3 AABBCenter = new Vec3(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
        Set<LivingEntity> pFoundTarget = new HashSet<>(pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(32d, 32d, 32d),
                e -> e != pPlayer && (AABBCenter.distanceTo(e.getPosition(0.5f))) <= 32));
        for (LivingEntity pEntityIterator : pFoundTarget) {
            AtomicBoolean pHasSculk = new AtomicBoolean(false);
            AABB pBoundingBox = pEntityIterator.getBoundingBox().inflate(2f);
            BlockPos.betweenClosed(Mth.floor(pBoundingBox.minX), Mth.floor(pBoundingBox.minY), Mth.floor(pBoundingBox.minZ),
                    Mth.floor(pBoundingBox.maxX), Mth.floor(pBoundingBox.maxY), Mth.floor(pBoundingBox.maxZ)).forEach(
                    pBlockPosIteration -> {
                        if (pLevel.getBlockState(pBlockPosIteration).is(WLTags.SCULK_BLOCKS)) {
                            pHasSculk.set(true);
                        }
                    }
            );

            if (pEntityIterator.hasEffect(WLEffects.SCULKED) || pHasSculk.get()) {
                pEntityIterator.addEffect(new MobEffectInstance(MobEffects.GLOWING, 320, 0));
            } else {
                pEntityIterator.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0));
            }
            pHasTarget = true;
        }

        pPlayer.level().playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, WLSounds.ITEM_SELF_RESONANT_BELL_RING, SoundSource.PLAYERS, 6f, 1.0f, 0);
        if (pHasTarget) {
            pPlayer.level().playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 6f, 0.8f, 0);
        }

        pPlayer.getItemInHand(pHand).hurtAndBreak(1, pPlayer, pHand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);

        pPlayer.awardStat(Stats.ITEM_USED.get(this));

        if (pHasTarget) {
            pPlayer.getCooldowns().addCooldown(this, 800);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
        } else {
            pPlayer.getCooldowns().addCooldown(this, 40);
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.self_resonant_bell.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.self_resonant_bell.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.self_resonant_bell.shift_desc_3", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
