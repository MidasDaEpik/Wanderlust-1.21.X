package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.entity.SporeCloud;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.particle.CubeOptions;
import com.midasdaepik.wanderlust.particle.slash.RoundaboutSweepOptions;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLParticles;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.KATANA_ABILITY_ACTIVE;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.KATANA_COMBO;

public class Mycoris extends KatanaItem {
    public Mycoris(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemMycorisDurability.get();
            }

            public float getSpeed() {
                return 9f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemMycorisAttackDamage.get() - 1);
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
        }, pProperties.fireResistant().attributes(Mycoris.createAttributes()).rarity(WLEnumExtensions.RARITY_MYCORIS.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemMycorisAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemMycorisAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
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
        pTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));

        if (pCombo % 3 == 0) {
            pAttacker.level().playSound(null, pAttacker.getEyePosition().x, pAttacker.getEyePosition().y, pAttacker.getEyePosition().z, WLSounds.ITEM_MYCORIS_SWIPE, SoundSource.PLAYERS, 1.2f, 1.3f);
        }

        if (pCombo == 9) {
            SporeCloud pSporeCloud = new SporeCloud(pAttacker.level(), pAttacker, 160);
            pSporeCloud.setPos(pAttacker.getEyePosition().x, pAttacker.getEyePosition().y, pAttacker.getEyePosition().z);
            pAttacker.level().addFreshEntity(pSporeCloud);

            final Vec3 AABBCenter = new Vec3(pAttacker.getX(), pAttacker.getY(0.5), pAttacker.getZ());
            List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter.x + 5, AABBCenter.y + 5, AABBCenter.z + 5, AABBCenter.x - 5, AABBCenter.y - 5, AABBCenter.z - 5), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                for (Object pObject : pEntityIterator.getActiveEffects().toArray()) {
                    if (pObject instanceof MobEffectInstance pMobEffectInstance) {
                        Holder<MobEffect> pEffect = pMobEffectInstance.getEffect();
                        int pDuration = pMobEffectInstance.getDuration();
                        if (pEffect.value().getCategory() == MobEffectCategory.HARMFUL
                                && (!pEffect.is(MobEffects.WITHER.getKey()) || !pEffect.is(WLEffects.SPORARUM.getKey()))
                                && !pEffect.value().isInstantenous()
                                && Math.floor((double) pDuration / 2) > 0) {
                            for (LivingEntity pEntityIterator2 : pFoundTarget) {
                                if (!(pEntityIterator2 == pAttacker) && !(pEntityIterator2 == pEntityIterator)) {
                                    pEntityIterator2.addEffect(new MobEffectInstance(pEffect, (int) Math.floor((double) pDuration / 2), pMobEffectInstance.getAmplifier()));
                                }
                            }
                        }
                    }
                }
                pServerLevel.sendParticles(WLParticles.SPORE_EFFECT.get(), pEntityIterator.getX(), pEntityIterator.getY(0.5), pEntityIterator.getZ(), 8, 0.25, 0.25, 0.25, 0.1);

                pAttacker.level().playSound(null, pEntityIterator.getEyePosition().x, pEntityIterator.getEyePosition().y, pEntityIterator.getEyePosition().z, WLSounds.ITEM_MYCORIS_EXPLODE, SoundSource.PLAYERS, 1f, 1.2f);
            }

            if (pAttacker instanceof Player pPlayer) {
                Vec3 pLookAngle = pPlayer.getLookAngle();
                float pYaw = (float) Math.atan2(pLookAngle.x, pLookAngle.z);

                pServerLevel.sendParticles(
                        new RoundaboutSweepOptions(new Vector3f(0.894f, 0.086f, 0.086f), 2.5f, Math.random() <= 0.5, 0, pYaw),
                        pPlayer.getX(), pPlayer.getY(0.55 + Math.random() * 0.1), pPlayer.getZ(),
                        1, 0, 0, 0, 0);
            }
        }
    }

    public static void sporeCloud(ServerLevel pServerLevel, LivingEntity pPlayer, int pCombo) {
        long pGameTime = pServerLevel.getGameTime();
        if (pCombo >= 3 && pGameTime % 2 < 1) {
            final Vec3 AABBCenter = new Vec3(pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ());
            List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter.x + 5, AABBCenter.y + 5, AABBCenter.z + 5, AABBCenter.x - 5, AABBCenter.y - 5, AABBCenter.z - 5), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                if (!(pEntityIterator == pPlayer)) {
                    if (!pEntityIterator.hasEffect(WLEffects.SPORARUM) || (pEntityIterator.hasEffect(WLEffects.SPORARUM) && pEntityIterator.getEffect(WLEffects.SPORARUM).getDuration() < 10)) {
                        pEntityIterator.addEffect(new MobEffectInstance(WLEffects.SPORARUM, 30, 0));
                    }
                    if (pCombo >= 6 && (!pEntityIterator.hasEffect(MobEffects.WITHER) || (pEntityIterator.hasEffect(MobEffects.WITHER) && pEntityIterator.getEffect(MobEffects.WITHER).getDuration() < 10))) {
                        pEntityIterator.addEffect(new MobEffectInstance(MobEffects.WITHER, 30, 1));
                    }
                }
            }
        }
        if (pCombo >= 3) {
            if (pGameTime % 10 < 1) {
                WLUtil.particleSphere(pServerLevel, new DustColorTransitionOptions(new Vector3f(0.772f,0.203f,0.223f), new Vector3f(0.482f,0f,0f), 0.9f), pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 5);
            }
            if (pGameTime % 20 < 1) {
                pServerLevel.playSound(null, pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ(), WLSounds.ITEM_MYCORIS_CLOUD, SoundSource.NEUTRAL, 1.0f, 1.1f);
            }
        }
        pServerLevel.sendParticles(WLParticles.SPORE.get(), pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ(), 1, 1.25, 1.25, 1.25, 0);
    }

    @Override
    public void successfulUse(Level pLevel, Player pPlayer, InteractionHand pHand) {
        super.successfulUse(pLevel, pPlayer, pHand);

        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pPlayer.getBoundingBox();
            pServerLevel.sendParticles(WLParticles.SPORE.get(), pPlayer.getX(), pPlayer.getY() + pLivingEntitySize.getYsize() / 2, pPlayer.getZ(), 16, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0);
            pServerLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, pPlayer.getX(), pPlayer.getY() + pLivingEntitySize.getYsize() / 2 - 0.25, pPlayer.getZ(), 8, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0);
        }
    }

    @Override
    public Vector3f particleColor(Player pPlayer, ItemStack pItemStack) {
        boolean KatanaAbilityActive = pPlayer.getData(KATANA_ABILITY_ACTIVE);
        if (KatanaAbilityActive) {
            return new Vector3f(0.894f, 0.086f, 0.086f);
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
