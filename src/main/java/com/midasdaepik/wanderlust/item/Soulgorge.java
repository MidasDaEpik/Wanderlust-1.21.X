package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class Soulgorge extends SwordItem {
    public Soulgorge(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemSoulgorgeDurability.get();
            }

            public float getSpeed() {
                return 9f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemSoulgorgeAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
            }

            public int getEnchantmentValue() {
                return 18;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(net.minecraft.world.item.Items.NETHERITE_SCRAP);
            }
        }, pProperties.fireResistant().attributes(Soulgorge.createAttributes()).rarity(WLEnumExtensions.RARITY_SOULGORGE.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemSoulgorgeAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemSoulgorgeAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ARMOR,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor.soulgorge"), WLAttributeConfig.CONFIG.ItemSoulgorgeArmor.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "knockback_resistance.soulgorge"), WLAttributeConfig.CONFIG.ItemSoulgorgeKnockbackResistance.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemStack) {
        return UseAnim.BLOCK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity) {
        this.releaseUsing(pItemStack, pLevel, pLivingEntity, 0);
        return pItemStack;
    }

    @Override
    public boolean hurtEnemy(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pLivingEntity) {
            if (pLivingEntity.getAttackStrengthScale(0) >= 0.9F) {
                attackEffects(pTarget, pAttacker);
            }
        } else {
            attackEffects(pTarget, pAttacker);
        }

        return super.hurtEnemy(pItemStack, pTarget, pAttacker);
    }

    public static void attackEffects(LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level().isClientSide() && Mth.nextInt(RandomSource.create(), 1, 5) == 1) {
            final Vec3 AABBCenter = new Vec3(pTarget.getX(), pTarget.getY(), pTarget.getZ());
            List<LivingEntity> pFoundTarget = pTarget.level().getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(3d), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                if (!(pEntityIterator == pAttacker)) {
                    pEntityIterator.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0));
                }
            }
            pTarget.level().playSeededSound(null, pTarget.getEyePosition().x, pTarget.getEyePosition().y, pTarget.getEyePosition().z, WLSounds.ITEM_WITHERBLADE_WITHER, SoundSource.HOSTILE, 1f, 0.8f,0);
        }
    }

    @Override
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
        double pLivingEntityHalfX = pLivingEntitySize.getXsize() / 2;
        double pLivingEntityHalfY = pLivingEntitySize.getYsize() / 2;
        double pLivingEntityHalfZ = pLivingEntitySize.getZsize() / 2;

        if (pTimeUsing >= 20) {
            if (pLevel instanceof ServerLevel pServerLevel) {
                pServerLevel.sendParticles(ParticleTypes.SOUL, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 10, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.02);
                pServerLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 10, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.1);

                WLUtil.particleSphere(pServerLevel, ParticleTypes.SOUL_FIRE_FLAME, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 8);
            }

            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_SOULGORGE_SHIELD, SoundSource.PLAYERS, 1f, 1f,0);

            float AbsorptionShield = pLivingEntity.getAbsorptionAmount() + 3;
            if (AbsorptionShield > 6) {
                AbsorptionShield = 6;
            }

            int BulwarkEffectLevel = 3 - Mth.floor(pLivingEntity.getAbsorptionAmount() + 3 - AbsorptionShield);
            if (pLivingEntity.hasEffect(WLEffects.BULWARK)) {
                pLivingEntity.addEffect(new MobEffectInstance(WLEffects.BULWARK, 1200, pLivingEntity.getEffect(WLEffects.BULWARK).getAmplifier() + BulwarkEffectLevel, true, true));
            } else {
                pLivingEntity.addEffect(new MobEffectInstance(WLEffects.BULWARK, 1200, BulwarkEffectLevel - 1, true, true));
            }

            if (pLivingEntity.getAbsorptionAmount() < AbsorptionShield) {
                pLivingEntity.setAbsorptionAmount(AbsorptionShield);
            }

            int WitherTargets = 0;

            AABB pEntityIteratorSize = null;
            final Vec3 AABBCenter = new Vec3(pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z);
            List<LivingEntity> pFoundTarget = pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(AABBCenter, AABBCenter).inflate(8d), e -> true).stream().sorted(Comparator.comparingDouble(DistanceComparer -> DistanceComparer.distanceToSqr(AABBCenter))).toList();
            for (LivingEntity pEntityIterator : pFoundTarget) {
                if (pEntityIterator.hasEffect(MobEffects.WITHER)) {
                    WitherTargets = WitherTargets + 1;
                    if (!(pEntityIterator == pLivingEntity)) {
                        pEntityIterator.hurt(WLDamageSource.damageSource(pLevel, pLivingEntity, WLDamageSource.MAGIC), (pEntityIterator.getEffect(MobEffects.WITHER).getAmplifier() + 2) * 6);
                        if (pLevel instanceof ServerLevel pServerLevel) {
                            pEntityIteratorSize = pLivingEntity.getBoundingBox();
                            pServerLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pEntityIterator.getX(), pEntityIterator.getY() + pEntityIteratorSize.getYsize() / 2, pEntityIterator.getZ(), 10, pEntityIteratorSize.getXsize() / 2, pEntityIteratorSize.getYsize() / 4, pEntityIteratorSize.getZsize() / 2, 0.1);
                        }
                    }
                    pEntityIterator.removeEffect(MobEffects.WITHER);
                }
            }

            if (pLivingEntity.getAbsorptionAmount() + WitherTargets > 10) {
                AbsorptionShield = 10;
            } else {
                AbsorptionShield = pLivingEntity.getAbsorptionAmount() + WitherTargets;
            }

            BulwarkEffectLevel = WitherTargets - Mth.floor(pLivingEntity.getAbsorptionAmount() + WitherTargets - AbsorptionShield);
            if (pLivingEntity.hasEffect(WLEffects.BULWARK)) {
                pLivingEntity.addEffect(new MobEffectInstance(WLEffects.BULWARK, 1200, pLivingEntity.getEffect(WLEffects.BULWARK).getAmplifier() + BulwarkEffectLevel, true, true));
            } else {
                pLivingEntity.addEffect(new MobEffectInstance(WLEffects.BULWARK, 1200, BulwarkEffectLevel - 1, true, true));
            }

            if (pLivingEntity.getAbsorptionAmount() < AbsorptionShield) {
                pLivingEntity.setAbsorptionAmount(AbsorptionShield);
            }

            pItemStack.hurtAndBreak(10, pLivingEntity, pLivingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.awardStat(Stats.ITEM_USED.get(this));

                pPlayer.getCooldowns().addCooldown(this, 400);
                pPlayer.getCooldowns().addCooldown(WLItems.OBSIDIAN_BULWARK.get(), 400);
            }
        } else {
            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.getCooldowns().addCooldown(this, 10);
                pPlayer.getCooldowns().addCooldown(WLItems.OBSIDIAN_BULWARK.get(), 10);
            }
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            pServerLevel.sendParticles(ParticleTypes.SOUL, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.01);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.two_handed"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.soulgorge.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.soulgorge.shift_desc_2"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.soulgorge.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.soulgorge.shift_desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.soulgorge.shift_desc_5"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.soulgorge.shift_desc_6", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
