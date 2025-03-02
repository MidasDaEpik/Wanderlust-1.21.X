package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Scylla extends SwordItem {
    public Scylla(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return 1688;
            }

            public float getSpeed() {
                return 8f;
            }

            public float getAttackDamageBonus() {
                return 4f;
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
            }

            public int getEnchantmentValue() {
                return 10;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(Items.ECHO_SHARD);
            }
        }, pProperties.attributes(Scylla.createAttributes()).rarity(WLEnumExtensions.RARITY_SCULK.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID,  4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID,  -2.4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pPlayer) {
            if (pPlayer.getAttackStrengthScale(0) >= 0.9F) {
                if (pTarget.hasEffect(WLEffects.ECHO) && pTarget.getEffect(WLEffects.ECHO).getAmplifier() < 4) {
                    pTarget.addEffect(new MobEffectInstance(WLEffects.ECHO, pTarget.getEffect(WLEffects.ECHO).getDuration(), pTarget.getEffect(WLEffects.ECHO).getAmplifier() + 1, true, true));
                    pTarget.level().playSeededSound(null, pTarget.getX(), pTarget.getY(), pTarget.getZ(), SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.PLAYERS, 1f, 1f,0);
                } else {
                    pTarget.addEffect(new MobEffectInstance(WLEffects.ECHO, 100, 0, true, true));
                    pTarget.level().playSeededSound(null, pTarget.getX(), pTarget.getY(), pTarget.getZ(), SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.PLAYERS, 1f, 1f,0);
                }
            }
        } else {
            if (pTarget.hasEffect(WLEffects.ECHO) && pTarget.getEffect(WLEffects.ECHO).getAmplifier() < 4) {
                pTarget.addEffect(new MobEffectInstance(WLEffects.ECHO, pTarget.getEffect(WLEffects.ECHO).getDuration(), pTarget.getEffect(WLEffects.ECHO).getAmplifier() + 1, true, true));
                pTarget.level().playSeededSound(null, pTarget.getX(), pTarget.getY(), pTarget.getZ(), SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.PLAYERS, 1f, 1f,0);
            } else {
                pTarget.addEffect(new MobEffectInstance(WLEffects.ECHO, 100, 0, true, true));
                pTarget.level().playSeededSound(null, pTarget.getX(), pTarget.getY(), pTarget.getZ(), SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.PLAYERS, 1f, 1f,0);
            }
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.scylla.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.scylla.shift_desc_2"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
