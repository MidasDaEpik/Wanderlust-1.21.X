package com.midasdaepik.wanderlust.event;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.networking.*;
import com.midasdaepik.wanderlust.registries.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingSwapItemsEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Comparator;
import java.util.List;

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
            if (pLivingEntity.hasEffect(WLEffects.ECHO) && pEvent.getSource().type() != WLDamageSource.damageSource(pServerLevel, WLDamageSource.ECHO).type()) {
                int pEchoAmplifier = Mth.clamp(pLivingEntity.getEffect(WLEffects.ECHO).getAmplifier() + 1, 1, 3);

                pLevel.playSeededSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), WLSounds.EFFECT_ECHO_ACCUMULATE.get(), SoundSource.MASTER, 0.5f, 1f, 0);

                pEvent.setNewDamage(pEvent.getNewDamage() * (3 - pEchoAmplifier) / 3f);
                pLivingEntity.setData(ECHO_STORED_DAMAGE, pLivingEntity.getData(ECHO_STORED_DAMAGE) + pEvent.getOriginalDamage() * pEchoAmplifier / 3f);
            }

            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.setData(TIME_SINCE_LAST_DAMAGE, 0);

                if (pPlayer.getUseItem().getItem() == WLItems.PYROSWEEP.get()) {
                    int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
                    if (PyrosweepCharge >= 1) {
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
                                    PacketDistributor.sendToPlayer(pServerPlayer, new PyroBarrierParticleS2CPacket(pPos.x + pDistDiff.x * pPlayerSize.getXsize() / 2, pPos.y + pDistDiff.y * pPlayerSize.getYsize() / 2, pPos.z + pDistDiff.z * pPlayerSize.getZsize() / 2, pDistDiff.x, pDistDiff.y, pDistDiff.z));
                                }
                            }
                        }

                        pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_PYROSWEEP_SHIELD, SoundSource.PLAYERS, 1f, 1f,0);

                        PyrosweepCharge -= 1;
                        pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                        if (pPlayer instanceof ServerPlayer pServerPlayer) {
                            PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepSyncS2CPacket(PyrosweepCharge));
                        }

                        if (PyrosweepCharge < 1) {
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
        if (pLivingEntity.level() instanceof ServerLevel pServerLevel && pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() == WLItems.ELDER_CHESTPLATE.get()) {
            Entity pDamageSourceEntity = pEvent.getSource().getEntity();
            if (pDamageSourceEntity instanceof LivingEntity pDamageSourceLivingEntity) {
                if (pLivingEntity instanceof Player pPlayer) {
                    if (!pPlayer.getCooldowns().isOnCooldown(WLItems.ELDER_CHESTPLATE.get())) {
                        pDamageSourceLivingEntity.hurt(WLDamageSource.damageSource(pServerLevel, pLivingEntity, DamageTypes.THORNS), pEvent.getOriginalDamage() * 0.6f);
                        pPlayer.getCooldowns().addCooldown(WLItems.ELDER_CHESTPLATE.get(), 40);
                    }
                } else if (RandomSource.create().nextFloat() < 0.5f) {
                    pDamageSourceLivingEntity.hurt(WLDamageSource.damageSource(pServerLevel, pLivingEntity, DamageTypes.THORNS), pEvent.getOriginalDamage() * 0.6f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCriticalHitEvent(CriticalHitEvent pEvent) {
        Player pPlayer = pEvent.getEntity();
        Level pLevel = pPlayer.level();

        if (pLevel instanceof ServerLevel pServerLevel) {
            pPlayer.setData(TIME_SINCE_LAST_ATTACK, 0);
        }

        if (pPlayer.getMainHandItem().is(WLTags.CRITLESS_WEAPONS)) {
            pEvent.setCriticalHit(false);
            pEvent.setDisableSweep(false);
        }
    }

    @SubscribeEvent
    public static void onPlayerTickEventPost(PlayerTickEvent.Post pEvent) {
        Player pPlayer = pEvent.getEntity();
        Level pLevel = pPlayer.level();

        if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
            int TimeSinceLastAttack = pPlayer.getData(TIME_SINCE_LAST_ATTACK);
            pPlayer.setData(TIME_SINCE_LAST_ATTACK, TimeSinceLastAttack + 1);

            int TimeSinceLastDamage = pPlayer.getData(TIME_SINCE_LAST_DAMAGE);
            pPlayer.setData(TIME_SINCE_LAST_DAMAGE, TimeSinceLastDamage + 1);

            int CharybdisCharge = pPlayer.getData(CHARYBDIS_CHARGE);
            if (TimeSinceLastAttack >= 300 && CharybdisCharge > 0) {
                CharybdisCharge = Mth.clamp(CharybdisCharge - 2, 0, 1400);
                pPlayer.setData(CHARYBDIS_CHARGE, CharybdisCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new CharybdisSyncS2CPacket(CharybdisCharge));
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

            int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
            if (TimeSinceLastAttack >= 300 && TimeSinceLastDamage >= 300 && PyrosweepCharge > 0) {
                PyrosweepCharge = Mth.clamp(PyrosweepCharge - 1, 0, 16);
                pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepSyncS2CPacket(PyrosweepCharge));
            }

            int RageCharge = pPlayer.getData(DRAGONS_RAGE_CHARGE);
            if (TimeSinceLastAttack >= 400 && RageCharge > 0) {
                RageCharge = Mth.clamp(RageCharge - 6, 0, 1800);
                pPlayer.setData(DRAGONS_RAGE_CHARGE, RageCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new DragonsRageSyncS2CPacket(RageCharge));
            }
        } else {
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
            PacketDistributor.sendToPlayer(pServerPlayer, new CharybdisSyncS2CPacket(pServerPlayer.getData(CHARYBDIS_CHARGE)));
            PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepSyncS2CPacket(pServerPlayer.getData(PYROSWEEP_CHARGE)));
            PacketDistributor.sendToPlayer(pServerPlayer, new DragonsRageSyncS2CPacket(pServerPlayer.getData(DRAGONS_RAGE_CHARGE)));
        }
    }
}