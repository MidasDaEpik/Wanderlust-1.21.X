package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

public class Kitsunebi extends KatanaItem {
    public Kitsunebi(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemKitsunebiDurability.get();
            }

            public float getSpeed() {
                return 9f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemKitsunebiAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
            }

            public int getEnchantmentValue() {
                return 16;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(net.minecraft.world.item.Items.NETHERITE_SCRAP);
            }
        }, pProperties.fireResistant().attributes(Kitsunebi.createAttributes()).rarity(WLEnumExtensions.RARITY_KITSUNEBI.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemKitsunebiAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemKitsunebiAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public int maxCombo() {
        return 9;
    }

    public int abilityMaxCombo() {
        return 9;
    }

    @Override
    public double reachModifier(Player pPlayer, double pOriginal) {
        if (pPlayer.getData(KATANA_ABILITY_ACTIVE) && pPlayer.getAttackStrengthScale(0) >= 0.9F) {
            return pOriginal * 1.4f;
        } else {
            return pOriginal;
        }
    }

    @Override
    public void abilityComboEffects(ServerLevel pServerLevel, ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker, int pCombo) {
        pTarget.igniteForTicks(80);
        pTarget.addEffect(new MobEffectInstance(WLEffects.IMMOLATION, 80, 0));

        if (pCombo % 3 == 0) {
            pServerLevel.playSound(null, pAttacker.getEyePosition().x, pAttacker.getEyePosition().y, pAttacker.getEyePosition().z, WLSounds.ITEM_KITSUNEBI_SWIPE, SoundSource.PLAYERS, 1.2f, 1.4f);
        }

        int pKitsunebiTargetId = pAttacker.getData(KITSUNEBI_TARGET);
        if (pCombo == 1) {
            pAttacker.setData(KITSUNEBI_TARGET, pTarget.getId());
            pKitsunebiTargetId = pTarget.getId();
        } else {
            Entity pKitsunebiTarget = pServerLevel.getEntity(pKitsunebiTargetId);
            if (!(pKitsunebiTarget instanceof LivingEntity && pKitsunebiTarget.isAlive() &&
                    pKitsunebiTarget.getPosition(0.0f).distanceTo(pAttacker.getPosition(0.0f)) < 128f)) {
                pAttacker.setData(KITSUNEBI_TARGET, pTarget.getId());
                pKitsunebiTargetId = pTarget.getId();
            }
        }
        if (pCombo == 9) {
            if (pServerLevel.getEntity(pKitsunebiTargetId) instanceof LivingEntity pKitsunebiTarget) {
                pKitsunebiTarget.removeEffect(WLEffects.SOUL_WISPS);
                pKitsunebiTarget.hurt(WLDamageSource.damageSource(pServerLevel, pAttacker, WLDamageSource.SOUL_BURN_NO_COOLDOWN), 25);

                pServerLevel.playSound(null, pTarget.getEyePosition().x, pTarget.getEyePosition().y, pTarget.getEyePosition().z, WLSounds.ITEM_KITSUNEBI_SOUL, SoundSource.PLAYERS, 1.2f, 1f);
            }
        }
    }

    public static void soulWispsTick(ServerLevel pServerLevel, LivingEntity pPlayer, int pCombo) {
        if (pCombo >= 1) {
            Entity pKitsunebiTarget = pServerLevel.getEntity(pPlayer.getData(KITSUNEBI_TARGET));
            if (pKitsunebiTarget instanceof LivingEntity pTarget) {
                pTarget.addEffect(new MobEffectInstance(WLEffects.SOUL_WISPS, 30, pCombo - 1));
                if (pCombo >= 3) {
                    pTarget.igniteForTicks(30);
                    pTarget.addEffect(new MobEffectInstance(WLEffects.IMMOLATION, 30, 0));
                }
                if (pCombo >= 6) {
                    pTarget.addEffect(new MobEffectInstance(WLEffects.VULNERABILITY, 30, 0));
                }
            }
        }
    }

    @Override
    public void successfulUse(Level pLevel, Player pPlayer, InteractionHand pHand) {
        super.successfulUse(pLevel, pPlayer, pHand);

        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pPlayer.getBoundingBox();
            pServerLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pPlayer.getX(), pPlayer.getY() + pLivingEntitySize.getYsize() / 2, pPlayer.getZ(), 16, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0);
            pServerLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, pPlayer.getX(), pPlayer.getY() + pLivingEntitySize.getYsize() / 2 - 0.25, pPlayer.getZ(), 8, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0);
        }
    }

    @Override
    public Vector3f particleColor(Player pPlayer, ItemStack pItemStack) {
        boolean KatanaAbilityActive = pPlayer.getData(KATANA_ABILITY_ACTIVE);
        if (KatanaAbilityActive) {
            return new Vector3f(0.690f, 0.792f, 1.000f);
        } else {
            return new Vector3f(1, 1, 1);
        }
    }

    @Override
    public boolean particleCancel(ServerLevel pServerLevel, Player pPlayer, ItemStack pItemStack) {
        int KatanaCombo = pPlayer.getData(KATANA_COMBO);
        boolean KatanaAbilityActive = pPlayer.getData(KATANA_ABILITY_ACTIVE);
        if (!KatanaAbilityActive && KatanaCombo == 0) {
            return true;
        } else {
            return false;
        }
    }
}
