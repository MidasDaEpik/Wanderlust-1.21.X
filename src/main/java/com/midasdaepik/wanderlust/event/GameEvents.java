package com.midasdaepik.wanderlust.event;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.networking.CharybdisSyncS2CPacket;
import com.midasdaepik.wanderlust.networking.DragonsRageSyncS2CPacket;
import com.midasdaepik.wanderlust.networking.PyrosweepDashSyncS2CPacket;
import com.midasdaepik.wanderlust.networking.PyrosweepSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.RRItems;
import com.midasdaepik.wanderlust.registries.RRTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingSwapItemsEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Comparator;
import java.util.List;

import static com.midasdaepik.wanderlust.registries.RRAttachmentTypes.*;

@EventBusSubscriber(modid = Wanderlust.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvents {
    @SubscribeEvent
    public static void onLivingDamageEventPost(LivingDamageEvent.Post pEvent) {
        LivingEntity pLivingEntity = pEvent.getEntity();
        if (pLivingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() == RRItems.ELDER_CHESTPLATE.get() && pLivingEntity.level() instanceof ServerLevel pServerLevel) {
            Entity pDamageSourceEntity = pEvent.getSource().getEntity();
            if (pDamageSourceEntity instanceof LivingEntity pDamageSourceLivingEntity && !pDamageSourceLivingEntity.isInvulnerable()) {
                if (pLivingEntity instanceof Player pPlayer) {
                    if (!pPlayer.getCooldowns().isOnCooldown(RRItems.ELDER_CHESTPLATE.get())) {
                        pDamageSourceLivingEntity.hurt(new DamageSource(pServerLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.THORNS), pLivingEntity), pEvent.getOriginalDamage() * 0.6f);
                        pPlayer.getCooldowns().addCooldown(RRItems.ELDER_CHESTPLATE.get(), 40);
                    }
                } else if (Mth.nextInt(RandomSource.create(), 1, 2) == 1) {
                    pDamageSourceLivingEntity.hurt(new DamageSource(pServerLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.THORNS), pLivingEntity), pEvent.getOriginalDamage() * 0.6f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent pEvent) {
        if (!pEvent.loadedFromDisk()) {
            Entity pEntity = pEvent.getEntity();
            if (pEntity instanceof PiglinBrute pPiglinBrute) {
                if (pPiglinBrute.getMainHandItem().getItem() == net.minecraft.world.item.Items.GOLDEN_AXE && Mth.nextInt(RandomSource.create(), 1, 3) == 1) {
                    pPiglinBrute.setItemSlot(EquipmentSlot.MAINHAND, RRItems.PIGLIN_WARAXE.toStack());
                    pPiglinBrute.setDropChance(EquipmentSlot.MAINHAND, 0.2f);
                }
            } else if (pEntity instanceof WitherSkeleton pWitherSkeleton) {
                if (pWitherSkeleton.getMainHandItem().getItem() == net.minecraft.world.item.Items.STONE_SWORD && Mth.nextInt(RandomSource.create(), 1, 3) == 1) {
                    pWitherSkeleton.setItemSlot(EquipmentSlot.MAINHAND, RRItems.WITHERBLADE.toStack());
                    pWitherSkeleton.setDropChance(EquipmentSlot.MAINHAND, 0.15f);
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
    }

    @SubscribeEvent
    public static void onPlayerTickEventPost(PlayerTickEvent.Post pEvent) {
        Player pPlayer = pEvent.getEntity();
        Level pLevel = pPlayer.level();

        if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
            int TimeSinceLastAttack = pPlayer.getData(TIME_SINCE_LAST_ATTACK);
            pPlayer.setData(TIME_SINCE_LAST_ATTACK, TimeSinceLastAttack + 1);

            int CharybdisCharge = pPlayer.getData(CHARYBDIS_CHARGE);
            if (TimeSinceLastAttack >= 300 && CharybdisCharge > 0) {
                CharybdisCharge = Mth.clamp(CharybdisCharge - 2, 0, 1400);
                pPlayer.setData(CHARYBDIS_CHARGE, CharybdisCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new CharybdisSyncS2CPacket(CharybdisCharge));
            }

            int PyrosweepDash = pPlayer.getData(PYROSWEEP_DASH);
            if (PyrosweepDash > 0) {
                PyrosweepDash = PyrosweepDash - 1;
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + 1.75, pPlayer.getZ(), 1, 0, 0, 0, 0);
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + 1.25, pPlayer.getZ(), 1, 0, 0, 0, 0);
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + 0.75, pPlayer.getZ(), 1, 0, 0, 0, 0);
                pServerLevel.sendParticles(ParticleTypes.FLAME, pPlayer.getX(), pPlayer.getY() + 0.25, pPlayer.getZ(), 1, 0, 0, 0, 0);

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

                        pEntityIterator.hurt(new DamageSource(pLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "burn"))), pPlayer), 18);
                        pEntityIterator.igniteForTicks(60);
                    }
                }

                pPlayer.setData(PYROSWEEP_DASH, PyrosweepDash);
                PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepDashSyncS2CPacket(PyrosweepDash));
            }

            int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
            if (PyrosweepCharge > 0) {
                if (TimeSinceLastAttack >= 400) {
                    PyrosweepCharge = Mth.clamp(PyrosweepCharge - 1, 0, 16);
                    pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                    PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepSyncS2CPacket(PyrosweepCharge));
                }
            }

            int RageCharge = pPlayer.getData(DRAGONS_RAGE_CHARGE);
            if (TimeSinceLastAttack >= 200 && RageCharge > 0) {
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
            if (pMainhand.is(RRTags.DUAL_WIELDED_WEAPONS) || pOffhand.is(RRTags.DUAL_WIELDED_WEAPONS)) {
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
