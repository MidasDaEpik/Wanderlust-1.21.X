package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLUtil;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class Witherblade extends SwordItem {
    public Witherblade(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return 604;
            }

            public float getSpeed() {
                return 5f;
            }

            public float getAttackDamageBonus() {
                return 4.5f;
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_IRON_TOOL;
            }

            public int getEnchantmentValue() {
                return 6;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(Items.BLACKSTONE);
            }
        }, pProperties.fireResistant().attributes(Witherblade.createAttributes()).rarity(WLEnumExtensions.RARITY_WITHERBLADE.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID,  4.5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID,  -2.4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean hurtEnemy(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pPlayer) {
            if (pPlayer.getAttackStrengthScale(0) >= 0.9F) {
                attackEffects(pItemStack, pTarget, pAttacker);
            }
        } else {
            attackEffects(pItemStack, pTarget, pAttacker);
        }

        return super.hurtEnemy(pItemStack, pTarget, pAttacker);
    }

    public void attackEffects(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level().isClientSide() && Mth.nextInt(RandomSource.create(), 1, 12) == 1) {
            pTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1, false, true));
            pTarget.level().playSeededSound(null, pTarget.getEyePosition().x, pTarget.getEyePosition().y, pTarget.getEyePosition().z, WLSounds.ITEM_WITHERBLADE_WITHER.get(), SoundSource.PLAYERS, 1f, 1f,0);
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.witherblade.shift_desc_1"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
