package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.particle.slash.RoundaboutSweepOptions;
import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
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

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

public class FirestormKatana extends KatanaItem {
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

    public int maxCombo() {
        return 9;
    }

    public int abilityMaxCombo() {
        return 6;
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

        if (pCombo % 3 == 0) {
            pAttacker.level().playSound(null, pAttacker.getEyePosition().x, pAttacker.getEyePosition().y, pAttacker.getEyePosition().z, WLSounds.ITEM_FIRESTORM_KATANA_SWIPE, SoundSource.PLAYERS, 1.2f, 1.3f);
        }

        if (pCombo == 6) {
            pAttacker.setData(KATANA_ABILITY_POST_TIMER, 120);

            final Vec3 AABBCenter = new Vec3(pAttacker.getX(), pAttacker.getY(0.5), pAttacker.getZ());
            List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, pAttacker.getBoundingBox().inflate(4, 1, 4), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                if (!(pEntityIterator == pAttacker)) {
                    pEntityIterator.igniteForTicks(120);
                    pEntityIterator.hurt(WLDamageSource.damageSource(pServerLevel, pAttacker, WLDamageSource.BURN_NO_COOLDOWN), 8);
                }
            }

            if (pAttacker instanceof Player pPlayer) {
                Vec3 pLookAngle = pPlayer.getLookAngle();
                float pYaw = (float) Math.atan2(pLookAngle.x, pLookAngle.z);

                pServerLevel.sendParticles(
                        new RoundaboutSweepOptions(new Vector3f(0.984f, 0.694f, 0.067f), 2, Math.random() <= 0.5, 0, pYaw),
                        pPlayer.getX(), pPlayer.getY(0.55 + Math.random() * 0.1), pPlayer.getZ(),
                        1, 0, 0, 0, 0);
            }
        }
    }

    public static void blazeAura(ServerLevel pServerLevel, LivingEntity pPlayer) {
        long pGameTime = pServerLevel.getGameTime();
        if (pGameTime % 20 < 1) {
            final Vec3 AABBCenter = new Vec3(pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ());
            List<LivingEntity> pFoundTarget = pServerLevel.getEntitiesOfClass(LivingEntity.class, pPlayer.getBoundingBox().inflate(2), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                if (!(pEntityIterator == pPlayer)) {
                    pEntityIterator.igniteForTicks(30);
                    pEntityIterator.hurt(WLDamageSource.damageSource(pServerLevel, pPlayer, WLDamageSource.BURN_NO_COOLDOWN), 4);
                }
                pServerLevel.playSound(null, pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ(), WLSounds.ITEM_FIRESTORM_KATANA_BURN, SoundSource.NEUTRAL, 1.0f, 1.1f);
            }
        }
        pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ(), 1, (pPlayer.getBoundingBox().getXsize() + 2) / 4, (pPlayer.getBoundingBox().getYsize() + pPlayer.getBoundingBox().getXsize() + 2) / 4, (pPlayer.getBoundingBox().getZsize() + pPlayer.getBoundingBox().getXsize() + 2) / 4, 0.03);
    }

    @Override
    public void successfulUse(Level pLevel, Player pPlayer, InteractionHand pHand) {
        super.successfulUse(pLevel, pPlayer, pHand);

        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pPlayer.getBoundingBox();
            pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + pLivingEntitySize.getYsize() / 2, pPlayer.getZ(), 12, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.05);
            pServerLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, pPlayer.getX(), pPlayer.getY() + pLivingEntitySize.getYsize() / 2 - 0.25, pPlayer.getZ(), 8, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0);
        }
    }

    @Override
    public Vector3f particleColor(Player pPlayer, ItemStack pItemStack) {
        boolean KatanaAbilityActive = pPlayer.getData(KATANA_ABILITY_ACTIVE);
        if (KatanaAbilityActive) {
            return new Vector3f(0.984f, 0.694f, 0.067f);
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
