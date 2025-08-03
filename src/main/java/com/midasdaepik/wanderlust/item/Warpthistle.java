package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class Warpthistle extends SwordItem {
    double pTeleportRange = 12f;

    public Warpthistle(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemWarpthistleDurability.get();
            }

            public float getSpeed() {
                return 9f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemWarpthistleAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
            }

            public int getEnchantmentValue() {
                return 13;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(net.minecraft.world.item.Items.NETHERITE_SCRAP);
            }
        }, pProperties.fireResistant().attributes(Warpthistle.createAttributes()).rarity(WLEnumExtensions.RARITY_WARPTHISTLE.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemWarpthistleAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemWarpthistleAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.warpthistle"), WLAttributeConfig.CONFIG.ItemWarpthistleMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 15;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemStack) {
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity) {
        this.releaseUsing(pItemStack, pLevel, pLivingEntity, 0);
        return pItemStack;
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
        if (!pAttacker.level().isClientSide() && Mth.nextInt(RandomSource.create(), 1, 8) == 1) {
            pTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 2));
            pTarget.level().playSeededSound(null, pTarget.getEyePosition().x, pTarget.getEyePosition().y, pTarget.getEyePosition().z, WLSounds.ITEM_WITHERBLADE_WITHER, SoundSource.HOSTILE, 1f, 1.2f,0);
        }
    }

    @Override
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;

        if (pTimeUsing >= 10) {
            boolean pDamageToggle = pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE, true);
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            double pLivingEntityHalfX = pLivingEntitySize.getXsize() / 2;
            double pLivingEntityHalfY = pLivingEntitySize.getYsize() / 2;
            double pLivingEntityHalfZ = pLivingEntitySize.getZsize() / 2;

            if (pLevel instanceof ServerLevel pServerLevel) {
                pServerLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 16, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.02);
                pServerLevel.sendParticles(ParticleTypes.LARGE_SMOKE, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 8, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.01);
                pServerLevel.sendParticles(ParticleTypes.FLASH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 1, 0, 0, 0, 0);
                if (pDamageToggle) {
                    pServerLevel.sendParticles(ParticleTypes.RAID_OMEN, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 8, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.01);
                }
            }

            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_WARPTHISTLE_TELEPORT, SoundSource.PLAYERS, 1f, 1f, 0);

            BlockHitResult pRaycast = WLUtil.blockRaycast(pLevel, pLivingEntity, ClipContext.Fluid.NONE, pTeleportRange);
            BlockPos pLookPos = pRaycast.getBlockPos().relative(pRaycast.getDirection());
            pLivingEntity.teleportTo(pLookPos.getX() + 0.5, pLookPos.getY(), pLookPos.getZ() + 0.5);
            pLivingEntity.fallDistance = pLivingEntity.fallDistance * 0.66f - (float) (pTeleportRange - 4);

            if (pDamageToggle) {
                final Vec3 AABBCenter = new Vec3(pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z);
                List<LivingEntity> pFoundTarget = pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(2.5d), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
                for (LivingEntity pEntityIterator : pFoundTarget) {
                    if (!(pEntityIterator == pLivingEntity)) {
                        pEntityIterator.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 3));
                        pEntityIterator.hurt(WLDamageSource.damageSource(pLevel, pLivingEntity, WLDamageSource.MAGIC), 14);
                    }
                }
            }

            if (pLevel instanceof ServerLevel pServerLevel) {
                pServerLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 16, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.02);
                pServerLevel.sendParticles(ParticleTypes.LARGE_SMOKE, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 8, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.01);
                pServerLevel.sendParticles(ParticleTypes.FLASH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 1, 0, 0, 0, 0);
                if (pDamageToggle) {
                    pServerLevel.sendParticles(ParticleTypes.RAID_OMEN, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 8, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.01);
                }
            }

            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_WARPTHISTLE_TELEPORT, SoundSource.PLAYERS, 1f, 1f,0);

            if (pLivingEntity instanceof Player pPlayer) {
                pItemStack.hurtAndBreak(3, pLivingEntity, pLivingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

                pPlayer.awardStat(Stats.ITEM_USED.get(this));

                pPlayer.getCooldowns().addCooldown(this, 140);
                pPlayer.getCooldowns().addCooldown(WLItems.WARPED_RAPIER.get(), 140);
            }
        } else {
            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.getCooldowns().addCooldown(this, 10);
                pPlayer.getCooldowns().addCooldown(WLItems.WARPED_RAPIER.get(), 10);
            }
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.01);
        } else if (pLevel.isClientSide) {
            BlockHitResult pRaycast = WLUtil.blockRaycast(pLevel, pLivingEntity, ClipContext.Fluid.ANY, pTeleportRange);
            BlockPos pLookPos = pRaycast.getBlockPos().relative(pRaycast.getDirection());
            pLevel.addParticle(ParticleTypes.DRAGON_BREATH, true, pLookPos.getX() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), pLookPos.getY() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), pLookPos.getZ() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), 0, 0, 0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pItemStack, ItemStack pOtherItemStack, Slot pSlot, ClickAction pClickAction, Player pPlayer, SlotAccess pSlotAccess) {
        if (pClickAction == ClickAction.SECONDARY && pOtherItemStack.isEmpty()) {
            if (pPlayer.level().isClientSide) {
                pPlayer.level().playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1f, 1f, 0);
            }
            if (pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE, true)) {
                pItemStack.set(WLDataComponents.ITEM_TOGGLE, false);
            } else {
                pItemStack.set(WLDataComponents.ITEM_TOGGLE, true);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.warpthistle.shift_desc_1"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.warpthistle.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.warpthistle.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.warpthistle.shift_desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.warpthistle.shift_desc_5"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.warpthistle.lore_damage_toggle", pItemStack.getOrDefault(WLDataComponents.ITEM_TOGGLE, true) ? "§aEnabled" : "§cDisabled"));
        }
        if (pItemStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
