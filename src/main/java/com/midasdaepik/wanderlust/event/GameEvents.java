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
import net.minecraft.world.item.Item;
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
import net.neoforged.neoforge.event.entity.living.*;
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

@EventBusSubscriber(modid = Wanderlust.MOD_ID)
public class GameEvents {
    @SubscribeEvent
    public static void onLivingIncomingDamageEvent(LivingIncomingDamageEvent pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        Level pLevel = pLivingEntity.level();

        if (pLevel instanceof ServerLevel pServerLevel) {
            DamageSource pDamageSource = pEvent.getSource();

            if (pDamageSource.is(WLDamageSource.ECHO)) {
                pEvent.setInvulnerabilityTicks(0);
            }

            if (pDamageSource.is(DamageTypeTags.IS_FIRE) || pDamageSource.is(DamageTypes.WITHER)) {
                int PyrosweepCharge = pLivingEntity.getData(PYROSWEEP_CHARGE);
                if (PyrosweepCharge > 0 && PyrosweepCharge >= WLCommonConfig.CONFIG.PyrosweepChargeFireWitherImmunity.get()) {
                    pEvent.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamageEventPre(LivingDamageEvent.Pre pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        Level pLevel = pLivingEntity.level();

        if (pLevel instanceof ServerLevel pServerLevel) {
            DamageSource pDamageSource = pEvent.getSource();

            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.setData(TIME_SINCE_DAMAGE_TAKEN, 0);
            }

            //Reduction
            if (!pDamageSource.is(DamageTypeTags.BYPASSES_EFFECTS) && !pDamageSource.is(DamageTypeTags.BYPASSES_RESISTANCE) && pLivingEntity.hasEffect(WLEffects.VULNERABILITY)) {
                pEvent.setNewDamage(pEvent.getNewDamage() * (1f + (pLivingEntity.getEffect(WLEffects.VULNERABILITY).getAmplifier() + 1f) * 0.2f));
            }

            if (pLivingEntity.hasEffect(WLEffects.PHANTASMAL)) {
                pEvent.setNewDamage(pEvent.getNewDamage() * 0.5f);
            }

            //Echo
            if (pLivingEntity.hasEffect(WLEffects.ECHO) && pEvent.getSource().type() != WLDamageSource.damageSource(pServerLevel, WLDamageSource.ECHO).type()) {
                int pEchoAmplifier = Mth.clamp(pLivingEntity.getEffect(WLEffects.ECHO).getAmplifier() + 1, 1, 3);

                pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.EFFECT_ECHO_ACCUMULATE.get(), SoundSource.MASTER, 0.5f, 1f, 0);

                pEvent.setNewDamage(pEvent.getNewDamage() * (3 - pEchoAmplifier) / 3f);
                pLivingEntity.setData(ECHO_STORED_DAMAGE, pLivingEntity.getData(ECHO_STORED_DAMAGE) + pEvent.getOriginalDamage() * pEchoAmplifier / 3f);
            }

            //Modify
            Item pHeadItem = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem();
            Item pChestItem = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem();
            Item pLegsItem = pLivingEntity.getItemBySlot(EquipmentSlot.LEGS).getItem();
            Item pFeetItem = pLivingEntity.getItemBySlot(EquipmentSlot.FEET).getItem();

            if ((pHeadItem == WLItems.PHANTOM_HOOD.get() || pChestItem == WLItems.PHANTOM_TUNIC.get() || pLegsItem == WLItems.PHANTOM_LEGGINGS.get() || pFeetItem == WLItems.PHANTOM_BOOTS.get()) && !(pChestItem == WLItems.PHANTOM_CLOAK.get())) {
                int pPieces = 0;
                pPieces += pHeadItem == WLItems.PHANTOM_HOOD.get() ? 1 : 0;
                pPieces += pChestItem == WLItems.PHANTOM_TUNIC.get() ? 1 : 0;
                pPieces += pLegsItem == WLItems.PHANTOM_LEGGINGS.get() ? 1 : 0;
                pPieces += pFeetItem == WLItems.PHANTOM_BOOTS.get() ? 1 : 0;

                if (pPieces >= 2) {
                    if (pLivingEntity instanceof Player pPlayer) {
                        if (pLivingEntity.getHealth() - pEvent.getNewDamage() <= pLivingEntity.getMaxHealth() * 0.25 && !pPlayer.getCooldowns().isOnCooldown(WLItems.PHANTOM_HOOD.get())) {
                            pLivingEntity.addEffect(new MobEffectInstance(WLEffects.PHANTASMAL, 80, 0, false, false, true));

                            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_PHANTOM_ARMOR_PHANTASMAL, SoundSource.PLAYERS, 2f, 1f,0);

                            pEvent.setNewDamage((float) Math.min(pLivingEntity.getHealth() - pLivingEntity.getMaxHealth() * 0.25, pEvent.getNewDamage()));

                            int pCooldown = 1400;
                            if (pLivingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == WLItems.PHANTOM_HOOD.get()) {
                                pCooldown -= 180;
                            }
                            if (pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.PHANTOM_TUNIC.get()) {
                                pCooldown -= 320;
                            }
                            if (pLivingEntity.getItemBySlot(EquipmentSlot.LEGS).getItem() == WLItems.PHANTOM_LEGGINGS.get()) {
                                pCooldown -= 320;
                            }
                            if (pLivingEntity.getItemBySlot(EquipmentSlot.FEET).getItem() == WLItems.PHANTOM_BOOTS.get()) {
                                pCooldown -= 180;
                            }

                            pPlayer.getCooldowns().addCooldown(WLItems.PHANTOM_HOOD.get(), pCooldown);
                        }
                    } else if (RandomSource.create().nextFloat() < 0.33f && pLivingEntity.getHealth() - pEvent.getNewDamage() <= pLivingEntity.getMaxHealth() * 0.5) {
                        pLivingEntity.addEffect(new MobEffectInstance(WLEffects.PHANTASMAL, 20, 0, false, false, true));

                        pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_PHANTOM_ARMOR_PHANTASMAL, SoundSource.PLAYERS, 2f, 1f,0);
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

            int DragonCharge = pPlayer.getData(DRAGON_CHARGE);
            if (TimeSinceLastAttack >= 400 && DragonCharge > 0) {
                DragonCharge = Math.max(DragonCharge - WLCommonConfig.CONFIG.DragonChargeDecayTimer.get(), 0);
                pPlayer.setData(DRAGON_CHARGE, DragonCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new DragonChargeSyncS2CPacket(DragonCharge));
            }

            int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
            if (PyrosweepCharge > 0) {
                if (PyrosweepCharge >= WLCommonConfig.CONFIG.PyrosweepChargeFireWitherImmunity.get()) {
                    AABB pPlayerSize = pPlayer.getBoundingBox();
                    if (TimeSinceLastDamage % 4 == 0) {
                        pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + pPlayerSize.getYsize() / 2, pPlayer.getZ(), 1, pPlayerSize.getXsize() / 2, pPlayerSize.getYsize() / 4, pPlayerSize.getZsize() / 2, 0);
                    }
                }

                if (TimeSinceLastAttack >= 300 && TimeSinceLastDamage >= 300) {
                    PyrosweepCharge = Math.max(PyrosweepCharge - WLCommonConfig.CONFIG.PyrosweepChargeDecayTimer.get(), 0);
                    pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                    PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepChargeSyncS2CPacket(PyrosweepCharge));
                }
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

            int PhantomHover = pPlayer.getData(PHANTOM_HOVER);
            boolean PhantomHoverToggle = pPlayer.getData(PHANTOM_HOVER_TOGGLE);
            if (pPlayer.getItemBySlot(EquipmentSlot.CHEST).is(WLItems.PHANTOM_CLOAK)) {
                int PhantomHoverMax = 1100;
                if (pPlayer.getItemBySlot(EquipmentSlot.HEAD).is(WLItems.PHANTOM_HOOD)) {
                    PhantomHoverMax += 500;
                }
                if (pPlayer.getItemBySlot(EquipmentSlot.LEGS).is(WLItems.PHANTOM_LEGGINGS)) {
                    PhantomHoverMax += 900;
                }
                if (pPlayer.getItemBySlot(EquipmentSlot.FEET).is(WLItems.PHANTOM_BOOTS)) {
                    PhantomHoverMax += 500;
                }

                if (PhantomHoverToggle) {
                    PhantomHover = Math.clamp(PhantomHover - 10, 0, PhantomHoverMax);
                    pPlayer.setData(PHANTOM_HOVER, PhantomHover);
                    PacketDistributor.sendToPlayer(pServerPlayer, new PhantomHoverSyncS2CPacket(PhantomHover));

                    Vec3 pMovement = pPlayer.getDeltaMovement();
                    pPlayer.setDeltaMovement(pMovement.x, pMovement.y * 0.25, pMovement.z);

                    if (pPlayer.fallDistance > 3) {
                        pPlayer.fallDistance = Math.max(pPlayer.fallDistance - 0.3f, 3);
                    }

                    if (PhantomHover == 0) {
                        pPlayer.getCooldowns().addCooldown(WLItems.PHANTOM_CLOAK.get(), 30);
                    }

                } else {
                    if (PhantomHover != PhantomHoverMax && !pPlayer.getCooldowns().isOnCooldown(WLItems.PHANTOM_CLOAK.get())) {
                        PhantomHover = Math.clamp(PhantomHover + 5, 0, PhantomHoverMax);
                        pPlayer.setData(PHANTOM_HOVER, PhantomHover);
                        PacketDistributor.sendToPlayer(pServerPlayer, new PhantomHoverSyncS2CPacket(PhantomHover));
                    }
                }
            } else if (PhantomHover > 0) {
                PhantomHover = 0;
                pPlayer.setData(PHANTOM_HOVER, PhantomHover);
                PacketDistributor.sendToPlayer(pServerPlayer, new PhantomHoverSyncS2CPacket(PhantomHover));
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

            boolean PhantomHoverToggle = pPlayer.getData(PHANTOM_HOVER_TOGGLE);
            if (pPlayer.getItemBySlot(EquipmentSlot.CHEST).is(WLItems.PHANTOM_CLOAK)) {
                int PhantomHover = pPlayer.getData(PHANTOM_HOVER);

                if (PhantomHoverToggle) {
                    if (pPlayer.onGround() || !pPlayer.isCrouching() || PhantomHover <= 0 || pPlayer.getCooldowns().isOnCooldown(WLItems.PHANTOM_CLOAK.get())) {
                        pPlayer.setData(PHANTOM_HOVER_TOGGLE, false);
                        PacketDistributor.sendToServer(new PhantomHoverSyncC2SPacket(false));

                    } else {
                        Vec3 pMovement = pPlayer.getDeltaMovement();
                        pPlayer.setDeltaMovement(pMovement.x, pMovement.y * 0.25, pMovement.z);
                    }
                } else {
                    if (!pPlayer.onGround() && pPlayer.isCrouching() && PhantomHover > 0 && !pPlayer.getCooldowns().isOnCooldown(WLItems.PHANTOM_CLOAK.get())) {
                        pPlayer.setData(PHANTOM_HOVER_TOGGLE, true);
                        PacketDistributor.sendToServer(new PhantomHoverSyncC2SPacket(true));
                    }
                }
            } else if (PhantomHoverToggle) {
                pPlayer.setData(PHANTOM_HOVER_TOGGLE, false);
                PacketDistributor.sendToServer(new PhantomHoverSyncC2SPacket(false));
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
    public static void onEnderManAngerEvent(EnderManAngerEvent pEvent) {
        if (WLUtil.hasMaskEnchantment(pEvent.getPlayer().getItemBySlot(EquipmentSlot.HEAD), WLEnchantmentEffects.CONCEALMENT.get())) {
            pEvent.setCanceled(true);
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
            PacketDistributor.sendToPlayer(pServerPlayer, new DragonChargeSyncS2CPacket(pServerPlayer.getData(DRAGON_CHARGE)));
            PacketDistributor.sendToPlayer(pServerPlayer, new PhantomHoverSyncS2CPacket(pServerPlayer.getData(PHANTOM_HOVER)));
            PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepChargeSyncS2CPacket(pServerPlayer.getData(PYROSWEEP_CHARGE)));
        }
    }
}