package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.item.extensions.HitParticleExtension;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class EchoTuner extends SwordItem implements HitParticleExtension {
    public EchoTuner(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemEchoTunerDurability.get();
            }

            public float getSpeed() {
                return 8f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemEchoTunerAttackDamage.get() - 1);
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
        }, pProperties.attributes(EchoTuner.createAttributes()).rarity(WLEnumExtensions.RARITY_SCULK.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemEchoTunerAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemEchoTunerAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
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

    public static void attackEffects(LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.hasEffect(WLEffects.ECHO)) {
            pTarget.addEffect(new MobEffectInstance(WLEffects.ECHO, 100, 0, true, false, true));
            pTarget.level().playSound(null, pTarget.getX(), pTarget.getY(), pTarget.getZ(), WLSounds.ITEM_ECHO_TUNER_SPREAD.get(), SoundSource.PLAYERS, 0.8f, 1f);

            pTarget.addEffect(new MobEffectInstance(WLEffects.SCULKED, 600, 0, true, false, true));
        }
    }

    @Override
    public Vector3f particleColor(Player pPlayer, ItemStack pItemStack) {
        return new Vector3f(0f,0.573f,0.584f);
    }
}
