package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaintedDagger extends SwordItem {
    public TaintedDagger(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemTaintedDaggerDurability.get();
            }

            public float getSpeed() {
                return 6f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemTaintedDaggerAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
            }

            public int getEnchantmentValue() {
                return 14;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(WLTags.COMPAT_WETLAND_WHIMSY_BLEMISH_ROD);
            }
        }, pProperties.attributes(TaintedDagger.createAttributes()).rarity(WLEnumExtensions.RARITY_BLEMISH.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemTaintedDaggerAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemTaintedDaggerAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "entity_interaction_range.tainted_dagger"),  WLAttributeConfig.CONFIG.ItemTaintedDaggerEntityInteractionRange.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "attack_damage.tainted_dagger_offhand"), WLAttributeConfig.CONFIG.ItemTaintedDaggerOffhandAttackDamage.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.OFFHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "attack_speed.tainted_dagger_offhand"), WLAttributeConfig.CONFIG.ItemTaintedDaggerOffhandAttackSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.OFFHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "entity_interaction_range.tainted_dagger_offhand"),  WLAttributeConfig.CONFIG.ItemTaintedDaggerOffhandEntityInteractionRange.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.OFFHAND)
                .build();
    }

    @Override
    public boolean hurtEnemy(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pPlayer) {
            if (pPlayer.getAttackStrengthScale(0) >= 0.9F) {
                attackEffects(pTarget, pAttacker);
            }
        } else {
            attackEffects(pTarget, pAttacker);
        }

        return super.hurtEnemy(pItemStack, pTarget, pAttacker);
    }

    public static void attackEffects(LivingEntity pTarget, LivingEntity pAttacker) {
        if (pTarget.hasEffect(MobEffects.POISON)) {
            if (pTarget.getEffect(MobEffects.POISON).getDuration() <= 60 && pTarget.getEffect(MobEffects.POISON).getAmplifier() == 1) {
                pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, Math.max(pTarget.getEffect(MobEffects.POISON).getDuration() + 20, 60), 1));
            }
        } else {
            pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1));
        }
        pAttacker.addEffect(new MobEffectInstance(WLEffects.VULNERABILITY, 50, 0));
    }

    @Override
    public float getAttackDamageBonus(Entity pTarget, float pDamage, DamageSource pDamageSource) {
        return calculateAttackDamageBonus(pTarget, pDamage, pDamageSource, super.getAttackDamageBonus(pTarget, pDamage, pDamageSource));
    }

    public static float calculateAttackDamageBonus(Entity pTarget, float pDamage, DamageSource pDamageSource, float pSuper) {
        Vec3 pSourceLocation = pDamageSource.getSourcePosition();
        if (pSourceLocation == null) {
            return pSuper;
        }
        float pDistance = (float) pSourceLocation.distanceTo(pTarget.position());

        return (float) (Math.clamp(2 - pDistance, 0, 1.5) * 4f + pSuper);
    }

    @Override
    public void appendHoverText(ItemStack pItemstack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.critless"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.tainted_dagger.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.tainted_dagger.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.tainted_dagger.shift_desc_3"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        if (pItemstack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemstack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
