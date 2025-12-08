package com.midasdaepik.wanderlust.event;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import com.midasdaepik.wanderlust.item.FangsOfFrost;
import com.midasdaepik.wanderlust.item.Keris;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.networking.*;
import com.midasdaepik.wanderlust.registries.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingSwapItemsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;

@EventBusSubscriber(modid = Wanderlust.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvents {
    @SubscribeEvent
    public static void onLivingIncomingDamageEvent(LivingIncomingDamageEvent pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        Level pLevel = pLivingEntity.level();

        if (pLevel instanceof ServerLevel pServerLevel) {
            if (pEvent.getSource().type() == WLDamageSource.damageSource(pServerLevel, WLDamageSource.ECHO).type()) {
                pEvent.setInvulnerabilityTicks(0);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamageEventPre(LivingDamageEvent.Pre pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        Level pLevel = pLivingEntity.level();

        if (pLevel instanceof ServerLevel pServerLevel) {
            DamageSource pDamageSource = pEvent.getSource();
            if (!pDamageSource.is(DamageTypeTags.BYPASSES_EFFECTS) && !pDamageSource.is(DamageTypeTags.BYPASSES_RESISTANCE) && pLivingEntity.hasEffect(WLEffects.VULNERABILITY)) {
                pEvent.setNewDamage(pEvent.getNewDamage() * (1f + (pLivingEntity.getEffect(WLEffects.VULNERABILITY).getAmplifier() + 1f) * 0.2f));
            }

            if (pLivingEntity.hasEffect(WLEffects.ECHO) && pEvent.getSource().type() != WLDamageSource.damageSource(pServerLevel, WLDamageSource.ECHO).type()) {
                int pEchoAmplifier = Mth.clamp(pLivingEntity.getEffect(WLEffects.ECHO).getAmplifier() + 1, 1, 3);

                pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.EFFECT_ECHO_ACCUMULATE.get(), SoundSource.MASTER, 0.5f, 1f, 0);

                pEvent.setNewDamage(pEvent.getNewDamage() * (3 - pEchoAmplifier) / 3f);
                pLivingEntity.setData(ECHO_STORED_DAMAGE, pLivingEntity.getData(ECHO_STORED_DAMAGE) + pEvent.getOriginalDamage() * pEchoAmplifier / 3f);
            }

            if (pLivingEntity.hasEffect(WLEffects.PHANTASMAL)) {
                pEvent.setNewDamage(pEvent.getNewDamage() * 0.5f);
            }

            if (pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.PHANTOM_CLOAK.get()) {
                if (pLivingEntity instanceof Player pPlayer) {
                    if (pLivingEntity.getHealth() - pEvent.getNewDamage() <= pLivingEntity.getMaxHealth() * 0.25 && !pPlayer.getCooldowns().isOnCooldown(WLItems.PHANTOM_CLOAK.get())) {
                        pLivingEntity.addEffect(new MobEffectInstance(WLEffects.PHANTASMAL, 120, 0, false, false, true));

                        pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_PHANTOM_CLOAK_PHANTASMAL, SoundSource.PLAYERS, 2f, 1f,0);

                        pEvent.setNewDamage((float) Math.min(pLivingEntity.getHealth() - pLivingEntity.getMaxHealth() * 0.25, pEvent.getNewDamage()));

                        pPlayer.awardStat(Stats.ITEM_USED.get(WLItems.PHANTOM_CLOAK.get()));
                        pPlayer.getCooldowns().addCooldown(WLItems.PHANTOM_CLOAK.get(), 1200);
                    }
                } else if (RandomSource.create().nextFloat() < 0.33f && pLivingEntity.getHealth() - pEvent.getNewDamage() <= pLivingEntity.getMaxHealth() * 0.5) {
                    pLivingEntity.addEffect(new MobEffectInstance(WLEffects.PHANTASMAL, 20, 0, false, false, true));

                    pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_PHANTOM_CLOAK_PHANTASMAL, SoundSource.PLAYERS, 2f, 1f,0);
                }
            }

            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.setData(TIME_SINCE_DAMAGE_TAKEN, 0);

                if (pPlayer.getUseItem().getItem() == WLItems.PYROSWEEP.get()) {
                    int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
                    int PyrosweepChargeShieldUse = WLCommonConfig.CONFIG.PyrosweepChargeShieldUse.get();

                    if (PyrosweepCharge >= PyrosweepChargeShieldUse) {
                        pEvent.setNewDamage(pEvent.getNewDamage() * 0.5f);

                        AABB pPlayerSize = pPlayer.getBoundingBox();
                        Vec3 pPos = new Vec3(pPlayer.getX() + pPlayerSize.getXsize() / 2, pPlayer.getY() + pPlayerSize.getYsize() / 2, pPlayer.getZ() + pPlayerSize.getZsize() / 2);
                        Vec3 pDistDiff = null;

                        Entity pEntitySource = pEvent.getSource().getDirectEntity();
                        Vec3 pLocationSource = pEvent.getSource().getSourcePosition();
                        if (pEntitySource != null) {
                            AABB pEntitySourceSize = pEntitySource.getBoundingBox();
                            pDistDiff = new Vec3(pEntitySource.getX() + pEntitySourceSize.getXsize() * 0.5 - pPos.x, pEntitySource.getY() + pEntitySourceSize.getYsize() * 0.5 - pPos.y, pEntitySource.getZ() + pEntitySourceSize.getZsize() * 0.5 - pPos.z);
                            pDistDiff.normalize();

                        } else if (pLocationSource != null) {
                            pDistDiff = new Vec3(pLocationSource.x + 0.5 - pPlayer.getX(), pLocationSource.y + 0.5 - pPlayer.getY(), pLocationSource.z + 0.5 - pPlayer.getZ());
                            pDistDiff.normalize();
                        }

                        if (pDistDiff != null) {
                            for(int j = 0; j < pServerLevel.players().size(); ++j) {
                                ServerPlayer pServerPlayer = pServerLevel.players().get(j);
                                if (pServerPlayer.blockPosition().closerToCenterThan(new Vec3(pPos.x, pPos.y, pPos.z), 64.0F)) {
                                    pServerLevel.sendParticles(WLUtil.pyroBarrierVec3dInput(pDistDiff.x, pDistDiff.y, pDistDiff.z), pPos.x + pDistDiff.x * pPlayerSize.getXsize() / 2, pPos.y + pDistDiff.y * pPlayerSize.getYsize() / 2, pPos.z + pDistDiff.z * pPlayerSize.getZsize() / 2, 1, 0, 0, 0, 0);
                                }
                            }
                        }

                        pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_PYROSWEEP_SHIELD, SoundSource.PLAYERS, 1f, 1f,0);

                        PyrosweepCharge -= PyrosweepChargeShieldUse;
                        pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                        if (pPlayer instanceof ServerPlayer pServerPlayer) {
                            PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepChargeSyncS2CPacket(PyrosweepCharge));
                        }

                        if (PyrosweepCharge < PyrosweepChargeShieldUse) {
                            pPlayer.stopUsingItem();
                        }

                        pPlayer.getUseItem().hurtAndBreak(1, pPlayer, pPlayer.getUsedItemHand() == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);

                        pPlayer.awardStat(Stats.ITEM_USED.get(WLItems.PYROSWEEP.get()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamageEventPost(LivingDamageEvent.Post pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        if (pLivingEntity.level() instanceof ServerLevel pServerLevel) {
            if (pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.ELDER_CHESTPLATE.get()) {
                Entity pDamageSourceEntity = pEvent.getSource().getEntity();
                if (pDamageSourceEntity instanceof LivingEntity pDamageSourceLivingEntity) {
                    if (pLivingEntity instanceof Player pPlayer) {
                        if (!pPlayer.getCooldowns().isOnCooldown(WLItems.ELDER_CHESTPLATE.get())) {
                            pDamageSourceLivingEntity.hurt(WLDamageSource.damageSource(pServerLevel, pLivingEntity, DamageTypes.THORNS), pEvent.getOriginalDamage() * 0.6f);

                            pPlayer.awardStat(Stats.ITEM_USED.get(WLItems.ELDER_CHESTPLATE.get()));
                            pPlayer.getCooldowns().addCooldown(WLItems.ELDER_CHESTPLATE.get(), 40);
                        }
                    } else if (RandomSource.create().nextFloat() < 0.5f) {
                        pDamageSourceLivingEntity.hurt(WLDamageSource.damageSource(pServerLevel, pLivingEntity, DamageTypes.THORNS), pEvent.getOriginalDamage() * 0.6f);
                    }
                }
            }
        }

        Entity pSource = pEvent.getSource().getEntity();
        if (pSource instanceof Player pPlayer && pPlayer.level() instanceof ServerLevel) {
            pPlayer.setData(TIME_SINCE_DAMAGE_DEALT, 0);
        }
    }

    @SubscribeEvent
    public static void onAttackEntityEvent(AttackEntityEvent pEvent) {
        Player pPlayer = pEvent.getEntity();

        if (pPlayer.getOffhandItem().is(WLTags.OFF_HAND_WEAPONS)) {
            ItemStack pOffhandItem = pPlayer.getOffhandItem();

            if (pOffhandItem.getItem() == WLItems.FANGS_OF_FROST.get()) {
                if (pEvent.getTarget() instanceof LivingEntity pTarget) {
                    FangsOfFrost.attackEffects(pTarget, pPlayer);
                }
            } else if (pOffhandItem.getItem() == WLItems.KERIS.get()) {
                if (pEvent.getTarget() instanceof LivingEntity pTarget) {
                    Keris.attackEffects(pTarget, pPlayer);
                }
            }

            pOffhandItem.hurtAndBreak(1, pPlayer, EquipmentSlot.OFFHAND);

            pPlayer.awardStat(Stats.ITEM_USED.get(pOffhandItem.getItem()));
        }
    }

    @SubscribeEvent
    public static void onCriticalHitEvent(CriticalHitEvent pEvent) {
        Player pPlayer = pEvent.getEntity();

        if (pPlayer.getMainHandItem().is(WLTags.CRITLESS_WEAPONS)) {
            pEvent.setCriticalHit(false);
            pEvent.setDisableSweep(false);
        }

        if (pPlayer.getOffhandItem().is(WLTags.CRITLESS_WEAPONS) && pPlayer.getOffhandItem().is(WLTags.OFF_HAND_WEAPONS) && pEvent.getDamageMultiplier() > 1.0f) {
            pEvent.setDamageMultiplier((pEvent.getDamageMultiplier() - 1f) * 0.5f + 1f);
        }
    }

    @SubscribeEvent
    public static void onPlayerTickEventPost(PlayerTickEvent.Post pEvent) {
        Player pPlayer = pEvent.getEntity();
        Level pLevel = pPlayer.level();

        if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
            int TimeSinceLastAttack = pPlayer.getData(TIME_SINCE_DAMAGE_DEALT);
            pPlayer.setData(TIME_SINCE_DAMAGE_DEALT, TimeSinceLastAttack + 1);

            int TimeSinceLastDamage = pPlayer.getData(TIME_SINCE_DAMAGE_TAKEN);
            pPlayer.setData(TIME_SINCE_DAMAGE_TAKEN, TimeSinceLastDamage + 1);

            int BlazeReapCharge = pPlayer.getData(BLAZE_REAP_CHARGE);
            if (BlazeReapCharge > 0) {
                BlazeReapCharge = BlazeReapCharge - 1;

                AABB pPlayerSize = pPlayer.getBoundingBox();
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + pPlayerSize.getYsize() / 2, pPlayer.getZ(), 1, pPlayerSize.getXsize() / 2, pPlayerSize.getYsize() / 4, pPlayerSize.getZsize() / 2, 0.01);

                pPlayer.setData(BLAZE_REAP_CHARGE, BlazeReapCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new BlazeReapChargeSyncS2CPacket(BlazeReapCharge));
            }

            int CharybdisCharge = pPlayer.getData(CHARYBDIS_CHARGE);
            if (TimeSinceLastAttack >= 300 && CharybdisCharge > 0) {
                CharybdisCharge = Math.max(CharybdisCharge - WLCommonConfig.CONFIG.CharybdisChargeDecayTimer.get(), 0);
                pPlayer.setData(CHARYBDIS_CHARGE, CharybdisCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new CharybdisChargeSyncS2CPacket(CharybdisCharge));
            }

            int PyrosweepDash = pPlayer.getData(PYROSWEEP_DASH);
            if (PyrosweepDash > 0) {
                PyrosweepDash = PyrosweepDash - 1;
                double pPlayerYSize = pPlayer.getBoundingBox().getYsize();
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + pPlayerYSize * 1.75, pPlayer.getZ(), 1, 0, 0, 0, 0);
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + pPlayerYSize * 1.25, pPlayer.getZ(), 1, 0, 0, 0, 0);
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + pPlayerYSize * 0.75, pPlayer.getZ(), 1, 0, 0, 0, 0);
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + pPlayerYSize * 0.25, pPlayer.getZ(), 1, 0, 0, 0, 0);

                Vec3 pMovement = pPlayer.getDeltaMovement();
                double pDiagonal = Math.sqrt(Math.pow(pMovement.x, 2) + Math.pow(pMovement.z, 2));
                double pXMovement = Math.abs(pMovement.x / pDiagonal * 1.5);
                pXMovement = Double.isNaN(pXMovement) ? 0 : Math.clamp(pMovement.x, -pXMovement, pXMovement);
                double pZMovement = Math.abs(pMovement.z / pDiagonal * 1.5);
                pZMovement = Double.isNaN(pZMovement) ? 0 : Math.clamp(pMovement.z, -pZMovement, pZMovement);
                pPlayer.setDeltaMovement(pXMovement, Math.clamp(pMovement.y, -0.05, 0.05), pZMovement);

                final Vec3 AABBCenter = new Vec3(pPlayer.getEyePosition().x + pMovement.x * 2, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z + pMovement.y * 2);
                List<LivingEntity> pFoundTarget = pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(2d), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
                for (LivingEntity pEntityIterator : pFoundTarget) {
                    if (!(pEntityIterator == pPlayer)) {
                        Vec3 pIteratorMovement = pEntityIterator.getDeltaMovement();
                        double pIteratorDiagonal = Math.sqrt(Math.pow(pIteratorMovement.x, 2) + Math.pow(pIteratorMovement.z, 2));
                        double pIteratorXClamp = Math.abs(pIteratorMovement.x / pIteratorDiagonal * 1.2);
                        pIteratorXClamp = Double.isNaN(pIteratorXClamp) ? 0 : pIteratorXClamp;
                        double pIteratorZClamp = Math.abs(pIteratorMovement.z / pIteratorDiagonal * 1.2);
                        pIteratorZClamp = Double.isNaN(pIteratorZClamp) ? 0 : pIteratorZClamp;
                        pEntityIterator.setDeltaMovement(Math.clamp(pIteratorMovement.x + pIteratorMovement.x * 0.4, -pIteratorXClamp, pIteratorXClamp), pIteratorMovement.y + 0.15, Math.clamp(pIteratorMovement.z + pMovement.z * 0.4, -pIteratorZClamp, pIteratorZClamp));

                        pEntityIterator.hurt(WLDamageSource.damageSource(pServerLevel, pPlayer, WLDamageSource.BURN), 18);
                        pEntityIterator.igniteForTicks(60);
                    }
                }

                pPlayer.setData(PYROSWEEP_DASH, PyrosweepDash);
                PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepDashSyncS2CPacket(PyrosweepDash));
            }

            int DragonCharge = pPlayer.getData(DRAGON_CHARGE);
            if (TimeSinceLastAttack >= 400 && DragonCharge > 0) {
                DragonCharge = Math.max(DragonCharge - WLCommonConfig.CONFIG.DragonChargeDecayTimer.get(), 0);
                pPlayer.setData(DRAGON_CHARGE, DragonCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new DragonChargeSyncS2CPacket(DragonCharge));
            }

            int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
            if (TimeSinceLastAttack >= 300 && TimeSinceLastDamage >= 300 && PyrosweepCharge > 0) {
                PyrosweepCharge = Math.max(PyrosweepCharge - WLCommonConfig.CONFIG.PyrosweepChargeDecayTimer.get(), 0);
                pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepChargeSyncS2CPacket(PyrosweepCharge));
            }

        } else if (pLevel instanceof ClientLevel) {
            int PyrosweepDash = pPlayer.getData(PYROSWEEP_DASH);
            if (PyrosweepDash > 0) {
                Vec3 pMovement = pPlayer.getDeltaMovement();
                double pDiagonal = Math.sqrt(Math.pow(pMovement.x, 2) + Math.pow(pMovement.z, 2));
                double pXMovement = Math.abs(pMovement.x / pDiagonal * 1.5);
                pXMovement = Double.isNaN(pXMovement) ? 0 : Math.clamp(pMovement.x, -pXMovement, pXMovement);
                double pZMovement = Math.abs(pMovement.z / pDiagonal * 1.5);
                pZMovement = Double.isNaN(pZMovement) ? 0 : Math.clamp(pMovement.z, -pZMovement, pZMovement);
                pPlayer.setDeltaMovement(pXMovement, Math.clamp(pMovement.y, -0.05, 0.05), pZMovement);

                PyrosweepDash = PyrosweepDash - 1;
                pPlayer.setData(PYROSWEEP_DASH, PyrosweepDash);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent pEvent) {
        LivingEntity pTarget = pEvent.getEntity();
        if (pTarget.level() instanceof ServerLevel pServerLevel) {
            LivingEntity pAttacker = pTarget.getLastAttacker();

            if (pAttacker != null) {
                if (pAttacker.getMainHandItem().getItem() == WLItems.CATALYST_CRYSTAL.get() || pAttacker.getOffhandItem().getItem() == WLItems.CATALYST_CRYSTAL.get()) {
                    if (pTarget.position().distanceTo(pAttacker.position()) <= 24 && !pTarget.wasExperienceConsumed()) {
                        int pExp = pTarget.getExperienceReward(pServerLevel, pAttacker);
                        if (pTarget.shouldDropExperience() && pExp > 0) {
                            ItemStack pItemStack;
                            int ItemExperience;
                            int ItemMaxExperience;

                            if (pAttacker.getMainHandItem().getItem() == WLItems.CATALYST_CRYSTAL.get()) {
                                pItemStack = pAttacker.getMainHandItem();

                                ItemExperience = pItemStack.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue();
                                ItemMaxExperience = pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 0.0).intValue();

                                if (ItemExperience >= ItemMaxExperience && pAttacker.getOffhandItem().getItem() == WLItems.CATALYST_CRYSTAL.get()) {
                                    pItemStack = pAttacker.getOffhandItem();

                                    ItemExperience = pItemStack.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue();
                                    ItemMaxExperience = pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 0.0).intValue();
                                }

                            } else {
                                pItemStack = pAttacker.getOffhandItem();

                                ItemExperience = pItemStack.getOrDefault(WLDataComponents.EXPERIENCE, 0.0).intValue();
                                ItemMaxExperience = pItemStack.getOrDefault(WLDataComponents.MAXIMUM_EXPERIENCE, 0.0).intValue();
                            }

                            if (ItemExperience < ItemMaxExperience) {
                                AABB pTargetSize = pTarget.getBoundingBox();
                                pServerLevel.sendParticles(ParticleTypes.SCULK_SOUL, pTarget.getX(), pTarget.getY() + pTargetSize.getYsize() / 2, pTarget.getZ(), 8, pTargetSize.getXsize() / 1.5, pTargetSize.getYsize() / 1.5, pTargetSize.getZsize() / 1.5, 0.01);

                                pServerLevel.playSeededSound(null, pAttacker.getEyePosition().x, pAttacker.getEyePosition().y, pAttacker.getEyePosition().z, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1f, 1.2f,0);
                                pServerLevel.playSeededSound(null, pTarget.getEyePosition().x, pTarget.getEyePosition().y, pTarget.getEyePosition().z, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1f, 1.2f,0);

                                Vec3 pTargetCenter = new Vec3(pTarget.getX(), pTarget.getY() + pTargetSize.getYsize() / 2, pTarget.getZ());
                                Vec3 pAttackerCenter = new Vec3(pAttacker.getX(), pAttacker.getY() + pAttacker.getBoundingBox().getYsize() / 2, pAttacker.getZ());
                                int pLength = Mth.floor(pTargetCenter.distanceTo(pAttackerCenter) * 2);

                                Vec3 pDistance = new Vec3((pAttackerCenter.x - pTargetCenter.x) / pLength, (pAttackerCenter.y - pTargetCenter.y) / pLength, (pAttackerCenter.z - pTargetCenter.z) / pLength);

                                for (int Loop = 1; Loop <= pLength; Loop++) {
                                    pServerLevel.sendParticles(ParticleTypes.SCULK_SOUL, pTarget.getX() + pDistance.x * Loop, pTarget.getY() + pTargetSize.getYsize() / 2 + pDistance.y * Loop, pTarget.getZ() + pDistance.z * Loop, 1, 0, 0, 0, 0);
                                }

                                pItemStack.set(WLDataComponents.EXPERIENCE, Math.min(ItemExperience + pExp, ItemMaxExperience));

                                pTarget.skipDropExperience();
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockDropsEvent(BlockDropsEvent pEvent) {
        ItemStack pItemStack = pEvent.getTool();
        if (pItemStack.getItem() == WLItems.MOLTEN_PICKAXE.get() || pItemStack.getItem() == WLItems.BLAZE_REAP.get()) {
            ServerLevel pServerLevel = pEvent.getLevel();
            List<ItemEntity> pItems = pEvent.getDrops();
            Optional<RecipeHolder<SmeltingRecipe>> pOptional;
            ItemStack pItemStackIterator, pItemStackResultIterator;

            for (ItemEntity pItemEntityIterator : pItems) {
                pItemStackIterator = pItemEntityIterator.getItem();
                pOptional = pServerLevel.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(pItemStackIterator), pServerLevel);

                if (pOptional.isPresent()) {
                    pItemStackResultIterator = pOptional.get().value().getResultItem(pServerLevel.registryAccess());
                    pItemEntityIterator.setItem(pItemStackIterator.transmuteCopy(pItemStackResultIterator.getItem(), pItemStackResultIterator.getCount() * pItemStackIterator.getCount()));

                    pServerLevel.sendParticles(ParticleTypes.FLAME, pItemEntityIterator.getX(), pItemEntityIterator.getY() + pItemEntityIterator.getBoundingBox().getYsize() * 0.5, pItemEntityIterator.getZ(), 4, 0.15, 0.15, 0.15, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingSwapItemsEvent(LivingSwapItemsEvent.Hands pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        if (pLivingEntity instanceof Player pPlayer) {
            ItemStack pMainhand = pPlayer.getInventory().getSelected();
            ItemStack pOffhand = pPlayer.getInventory().offhand.get(0);
            if (pMainhand.is(WLTags.TWO_HANDED_WEAPONS) || pOffhand.is(WLTags.TWO_HANDED_WEAPONS)) {
                pEvent.setItemSwappedToMainHand(pOffhand);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent pEvent) {
        Player pPlayer = pEvent.getEntity();
        Level pLevel = pPlayer.level();

        if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
            PacketDistributor.sendToPlayer(pServerPlayer, new CharybdisChargeSyncS2CPacket(pServerPlayer.getData(CHARYBDIS_CHARGE)));
            PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepChargeSyncS2CPacket(pServerPlayer.getData(PYROSWEEP_CHARGE)));
            PacketDistributor.sendToPlayer(pServerPlayer, new DragonChargeSyncS2CPacket(pServerPlayer.getData(DRAGON_CHARGE)));
        }
    }
}