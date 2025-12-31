package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.entity.Firestorm;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FirestormKatana extends SwordItem {
    public FirestormKatana(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemFirestormKatanaDurability.get();
            }

            public float getSpeed() {
                return 8f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemFirestormKatanaAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_IRON_TOOL;
            }

            public int getEnchantmentValue() {
                return 12;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(net.minecraft.world.item.Items.BLAZE_ROD);
            }
        }, pProperties.fireResistant().attributes(FirestormKatana.createAttributes()).rarity(WLEnumExtensions.RARITY_BLAZE.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemFirestormKatanaAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemFirestormKatanaAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
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

    public void attackEffects(LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker.level().isClientSide()) {
            return;
        }

        if (pAttacker.hasEffect(WLEffects.KATANA_COMBO)) {
            int pAmplifier = pAttacker.getEffect(WLEffects.KATANA_COMBO).getAmplifier();
            if (pAmplifier >= 3) {
                if (!(pAttacker instanceof Player) || (pAttacker instanceof Player pPlayer && !pPlayer.getCooldowns().isOnCooldown(this))) {
                    Firestorm pFirestorm = new Firestorm(pAttacker.level(), pAttacker, 160, 20, false);
                    pFirestorm.setPos(pAttacker.getEyePosition().x, pAttacker.getEyePosition().y, pAttacker.getEyePosition().z);
                    pAttacker.level().addFreshEntity(pFirestorm);

                    pAttacker.level().playSeededSound(null, pAttacker.getEyePosition().x, pAttacker.getEyePosition().y, pAttacker.getEyePosition().z, WLSounds.ITEM_FIRESTORM_KATANA_CLOUD, SoundSource.PLAYERS, 1f, 1f, 0);

                    if (pAttacker instanceof Player pPlayer) {
                        pPlayer.getCooldowns().addCooldown(this, 240);
                        pPlayer.getCooldowns().addCooldown(WLItems.MYCORIS.get(), 240);
                    }
                }
                pAttacker.level().playSeededSound(null, pAttacker.getEyePosition().x, pAttacker.getEyePosition().y, pAttacker.getEyePosition().z, WLSounds.ITEM_FIRESTORM_KATANA_SWIPE, SoundSource.PLAYERS, 0.8f, 0.8f,0);
                pAttacker.removeEffect(WLEffects.KATANA_COMBO);
            } else {
                pAttacker.removeEffect(WLEffects.KATANA_COMBO);
                pAttacker.addEffect(new MobEffectInstance(WLEffects.KATANA_COMBO, 30, pAmplifier + 1, true, false, true));
            }
        } else {
            pAttacker.addEffect(new MobEffectInstance(WLEffects.KATANA_COMBO, 30, 0, true, false, true));
        }
    }

    @Override
    public float getAttackDamageBonus(Entity pTarget, float pDamage, DamageSource pDamageSource) {
        return calculateAttackDamageBonus(pTarget, pDamage, pDamageSource, super.getAttackDamageBonus(pTarget, pDamage, pDamageSource));
    }

    public static float calculateAttackDamageBonus(Entity pTarget, float pDamage, DamageSource pDamageSource, float pSuper) {
        Entity pSourceEntity = pDamageSource.getEntity();
        if (pSourceEntity instanceof LivingEntity pLivingEntity && pLivingEntity.hasEffect(WLEffects.KATANA_COMBO) && pLivingEntity.getEffect(WLEffects.KATANA_COMBO).getAmplifier() >= 3) {
            return 2.0f;
        } else {
            return pSuper;
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.firestorm_katana.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.firestorm_katana.shift_desc_2"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.firestorm_katana.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.firestorm_katana.shift_desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.firestorm_katana.shift_desc_5", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
