package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLStartupConfig;
import com.midasdaepik.wanderlust.networking.CharybdisSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.WLDamageSource;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.CHARYBDIS_CHARGE;

public class Charybdis extends SwordItem {
    public Charybdis(Properties pProperties) {
        this(new Tier() {
            public int getUses() {
                return WLStartupConfig.CONFIG.ItemCharybdisDurability.get();
            }

            public float getSpeed() {
                return 6f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLStartupConfig.CONFIG.ItemCharybdisAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
            }

            public int getEnchantmentValue() {
                return 13;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(Items.PRISMARINE_CRYSTALS);
            }
        }, pProperties);
    }

    public Charybdis(Tier pTier, Properties pProperties) {
        super(pTier, pProperties
                        .attributes(Charybdis.createAttributes())
                        .rarity(WLEnumExtensions.RARITY_ELDER.getValue()),
                pTier.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE)
        );
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLStartupConfig.CONFIG.ItemCharybdisAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLStartupConfig.CONFIG.ItemCharybdisAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.SWEEPING_DAMAGE_RATIO,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "sweeping_damage_ratio"),  WLStartupConfig.CONFIG.ItemCharybdisSweepingDamageRatio.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.SUBMERGED_MINING_SPEED,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "submerged_mining_speed"),  WLStartupConfig.CONFIG.ItemCharybdisSubmergedMiningSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean canPerformAction(ItemStack pItemStack, ItemAbility pItemAbility) {
        return super.canPerformAction(pItemStack, pItemAbility) || ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(pItemAbility);
    }

   @Override
   public @NotNull AABB getSweepHitBox(ItemStack pItemStack, Player pPlayer, Entity pTarget) {
        return pTarget.getBoundingBox().inflate(1.5, 0.25, 1.5);
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 400;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity) {
        this.releaseUsing(pItemStack, pLevel, pLivingEntity, 0);
        return pItemStack;
    }

    @Override
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (pLivingEntity instanceof Player pPlayer) {
            pItemStack.hurtAndBreak(pTimeUsing / 10, pLivingEntity, pLivingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

            pPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pPlayer) {
            if (pPlayer.getAttackStrengthScale(0) >= 0.9F) {
                attackEffects(pItemStack, pTarget, pAttacker);

                if (pPlayer.level() instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                    int CharybdisCharge = pPlayer.getData(CHARYBDIS_CHARGE);
                    if (CharybdisCharge < 1400) {
                        CharybdisCharge = Mth.clamp(CharybdisCharge + 70, 0, 1400);
                        pPlayer.setData(CHARYBDIS_CHARGE, CharybdisCharge);
                        PacketDistributor.sendToPlayer(pServerPlayer, new CharybdisSyncS2CPacket(CharybdisCharge));
                    }
                }
            }
        } else {
            attackEffects(pItemStack, pTarget, pAttacker);
        }

        return super.hurtEnemy(pItemStack, pTarget, pAttacker);
    }

    public void attackEffects(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        double dX = pAttacker.getX() - pTarget.getX();
        double dZ = pAttacker.getZ() - pTarget.getZ();
        double dXZNormalized = Math.sqrt(dX * dX + dZ * dZ);

        pTarget.setDeltaMovement(pTarget.getDeltaMovement().add(dX / dXZNormalized * 0.8, 0, dZ / dXZNormalized * 0.8));
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;

        if (pLivingEntity instanceof Player pPlayer) {
            int CharybdisCharge = pPlayer.getData(CHARYBDIS_CHARGE);

            if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                CharybdisCharge = Mth.clamp(CharybdisCharge - 5, 0, 1400);
                pPlayer.setData(CHARYBDIS_CHARGE, CharybdisCharge);
                PacketDistributor.sendToPlayer(pServerPlayer, new CharybdisSyncS2CPacket(CharybdisCharge));

                final Vec3 AABBCenter = new Vec3(pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ());
                Set<Entity> pFoundTarget = new HashSet<>(pLevel.getEntitiesOfClass(Entity.class, new AABB(AABBCenter, AABBCenter).inflate(12d, 8d, 12d), e -> true));
                for (Entity pEntityIterator : pFoundTarget) {
                    if (!(pEntityIterator == pLivingEntity) && (pEntityIterator instanceof LivingEntity || pEntityIterator instanceof Projectile || pEntityIterator instanceof ItemEntity)) {
                        double dX = pLivingEntity.getX() - pEntityIterator.getX();
                        double dY = (pLivingEntity.getY() - pEntityIterator.getY()) * 1.5;
                        double dZ = pLivingEntity.getZ() - pEntityIterator.getZ();

                        double dXZNormalized = Math.sqrt(dX * dX + dZ * dZ);

                        double pAngle = Math.PI * 5 / 12;
                        double dXFinal = dX * Math.cos(pAngle) - dZ * Math.sin(pAngle);
                        double dZFinal = dZ * Math.cos(pAngle) + dX * Math.sin(pAngle);

                        if (dXZNormalized < 1.5) {
                            dXFinal = 0;
                            dZFinal = 0;
                        }

                        if (dY >= 0) {
                            dY = 12 - dY;
                            if (dY > 10) {
                                dY = 0;
                            }
                        } else {
                            dY = -12 - dY;
                            if (dY < -10) {
                                dY = 0;
                            }
                        }

                        dY = Mth.clamp(dY / 2, -4, 4);

                        double pMult;
                        if (pEntityIterator instanceof Projectile) {
                            pMult = 0.1;
                        } else if (pEntityIterator instanceof ItemEntity) {
                            pMult = 0.05;
                        } else {
                            pMult = 0.05;
                        }

                        pEntityIterator.setDeltaMovement(dXFinal * pMult, dY * pMult, dZFinal * pMult);
                    }
                }

                if (pTimeUsing % 20 == 0) {
                    int DamageLimit = 0;
                    List<LivingEntity> pFoundTarget2 = pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(1.5d), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
                    for (LivingEntity pEntityIterator : pFoundTarget2) {
                        if (!(pEntityIterator == pLivingEntity) && DamageLimit <= 5) {
                            boolean pSuccess = pEntityIterator.hurt(WLDamageSource.damageSource(pServerLevel, pLivingEntity, WLDamageSource.WHIRLPOOL), 12);
                            if (pSuccess) {
                                DamageLimit += 1;
                            }
                        }
                    }
                }
                AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
                double pLivingEntityHalfX = pLivingEntitySize.getXsize() / 2;
                double pLivingEntityHalfY = pLivingEntitySize.getYsize() / 2;
                double pLivingEntityHalfZ = pLivingEntitySize.getZsize() / 2;

                pServerLevel.sendParticles(ParticleTypes.BUBBLE, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 3, 4, 1, 4, 0);

                if (pTimeUsing % 10 == 0) {
                    WLUtil.particleCircle(pServerLevel, new DustColorTransitionOptions(new Vector3f(0f,0.4f,0.8f), new Vector3f(0,0.2f,0.4f), 1), pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY * 1.75, pLivingEntity.getZ(), 12, 3);
                    WLUtil.particleCircle(pServerLevel, new DustColorTransitionOptions(new Vector3f(0f,0.4f,0.8f), new Vector3f(0,0.2f,0.4f), 1), pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY * 0.25, pLivingEntity.getZ(), 12, 3);
                }

                if (CharybdisCharge < 4) {
                    pPlayer.stopUsingItem();
                }

            } else if (pLevel instanceof ClientLevel pClientLevel) {
                for (int Loop = 1; Loop <= 2; Loop++) {
                    double XZDegrees = Mth.nextInt(RandomSource.create(), 1, 360) * Math.PI / 180;
                    float XZRange = Mth.nextFloat(RandomSource.create(), 2.0f, 12.0f);
                    double dX = pLivingEntity.getEyePosition().x + Math.cos(XZDegrees) * XZRange;
                    double dZ = pLivingEntity.getEyePosition().z + Math.sin(XZDegrees) * XZRange;

                    float YRange = Mth.nextFloat(RandomSource.create(), -1.75f, 1.25f);
                    float PullSpeed = Mth.nextFloat(RandomSource.create(), -3.0f, -1.5f);

                    double pAngle = XZDegrees - Math.PI / 3;
                    double dXSpeed = PullSpeed * Math.cos(pAngle);
                    double dZSpeed = PullSpeed * Math.sin(pAngle);

                    pClientLevel.addParticle(ParticleTypes.OMINOUS_SPAWNING, dX, pLivingEntity.getEyePosition().y + YRange, dZ, dXSpeed, Mth.nextFloat(RandomSource.create(), -0.25f, 0.25f), dZSpeed);
                }

                if (CharybdisCharge < 4) {
                    pPlayer.stopUsingItem();
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (pPlayer.getData(CHARYBDIS_CHARGE) > 0 && pPlayer.isCrouching()) {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
        } else {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.sweeping"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.charybdis.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.charybdis.shift_desc_2"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.charybdis.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.charybdis.shift_desc_4"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
