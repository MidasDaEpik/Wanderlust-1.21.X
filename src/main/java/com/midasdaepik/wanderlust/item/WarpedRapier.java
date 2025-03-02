package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLUtil;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarpedRapier extends SwordItem {
    public WarpedRapier(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return 641;
            }

            public float getSpeed() {
                return 8f;
            }

            public float getAttackDamageBonus() {
                return 4.5f;
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
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID,  4.5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID,  -2.2, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
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
            if (pLevel instanceof ServerLevel pServerLevel) {
                pServerLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 16, 0.5, 0.5, 0.5, 0.02);
                pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 8, 0.5, 0.5, 0.5, 0.01);
                pServerLevel.sendParticles(ParticleTypes.FLASH, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 1, 0, 0, 0, 0);
            }

            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_WARPED_RAPIER_TELEPORT.get(), SoundSource.PLAYERS, 1f, 1f, 0);

            BlockHitResult pRaytrace = WLUtil.blockHitRaycast(pLevel, pLivingEntity, ClipContext.Fluid.NONE, 12);
            BlockPos pLookPos = pRaytrace.getBlockPos().relative(pRaytrace.getDirection());
            pLivingEntity.setPos(pLookPos.getX() + 0.5, pLookPos.getY(), pLookPos.getZ() + 0.5);
            pLivingEntity.fallDistance = pLivingEntity.fallDistance - 5.0F;

            if (pLevel instanceof ServerLevel pServerLevel) {
                pServerLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 16, 0.5, 0.5, 0.5, 0.02);
                pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 8, 0.5, 0.5, 0.5, 0.01);
                pServerLevel.sendParticles(ParticleTypes.FLASH, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 1, 0, 0, 0, 0);
            }

            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_WARPED_RAPIER_TELEPORT.get(), SoundSource.PLAYERS, 1f, 1f,0);

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
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (pLevel instanceof ServerLevel pServerLevel) {
            pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pLivingEntity.getX(), pLivingEntity.getY() + 1, pLivingEntity.getZ(), 1, 0.4, 0.4, 0.4, 0.01);
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
            pTooltipComponents.add(Component.translatable("item.wanderlust.warped_rapier.shift_desc_2"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemstack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemstack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
