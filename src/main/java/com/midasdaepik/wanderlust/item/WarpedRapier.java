package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarpedRapier extends SwordItem {
    double pTeleportRange = 12f;

    public WarpedRapier(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemWarpedRapierDurability.get();
            }

            public float getSpeed() {
                return 8f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemWarpedRapierAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_IRON_TOOL;
            }

            public int getEnchantmentValue() {
                return 9;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(net.minecraft.world.item.Items.ENDER_PEARL);
            }
        }, pProperties.attributes(WarpedRapier.createAttributes()).rarity(WLEnumExtensions.RARITY_WARPED_RAPIER.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemWarpedRapierAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemWarpedRapierAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.warped_rapier"), WLAttributeConfig.CONFIG.ItemWarpedRapierMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack pItemstack, LivingEntity pLivingEntity) {
        return 15;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemstack) {
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity) {
        this.releaseUsing(pItemStack, pLevel, pLivingEntity, 0);
        return pItemStack;
    }

    @Override
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;

        if (pTimeUsing >= 10) {
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            double pLivingEntityHalfX = pLivingEntitySize.getXsize() / 2;
            double pLivingEntityHalfY = pLivingEntitySize.getYsize() / 2;
            double pLivingEntityHalfZ = pLivingEntitySize.getZsize() / 2;

            if (pLevel instanceof ServerLevel pServerLevel) {
                pServerLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 16, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.02);
                pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 8, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.01);
                pServerLevel.sendParticles(ParticleTypes.FLASH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 1, 0, 0, 0, 0);
            }

            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_WARPED_RAPIER_TELEPORT, SoundSource.PLAYERS, 1f, 1f, 0);

            BlockHitResult pRaycast = WLUtil.blockRaycast(pLevel, pLivingEntity, ClipContext.Fluid.NONE, pTeleportRange);
            BlockPos pLookPos = pRaycast.getBlockPos().relative(pRaycast.getDirection());
            pLivingEntity.teleportTo(pLookPos.getX() + 0.5, pLookPos.getY(), pLookPos.getZ() + 0.5);
            pLivingEntity.fallDistance = pLivingEntity.fallDistance * 0.66f - (float) (pTeleportRange - 4);

            if (pLevel instanceof ServerLevel pServerLevel) {
                pServerLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 16, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.02);
                pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 8, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0.01);
                pServerLevel.sendParticles(ParticleTypes.FLASH, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 1, 0, 0, 0, 0);
            }

            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_WARPED_RAPIER_TELEPORT, SoundSource.PLAYERS, 1f, 1f,0);

            if (pLivingEntity instanceof Player pPlayer) {
                pItemStack.hurtAndBreak(3, pLivingEntity, pLivingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

                pPlayer.awardStat(Stats.ITEM_USED.get(this));

                pPlayer.getCooldowns().addCooldown(this, 160);
                pPlayer.getCooldowns().addCooldown(WLItems.WARPTHISTLE.get(), 160);
            }
        } else {
            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.getCooldowns().addCooldown(this, 10);
                pPlayer.getCooldowns().addCooldown(WLItems.WARPTHISTLE.get(), 10);
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
    public void appendHoverText(ItemStack pItemstack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.warped_rapier.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.warped_rapier.shift_desc_2", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        if (pItemstack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemstack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
