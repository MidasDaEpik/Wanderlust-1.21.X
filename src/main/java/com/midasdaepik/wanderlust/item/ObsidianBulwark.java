package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.*;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ObsidianBulwark extends SwordItem {
    public ObsidianBulwark(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return 563;
            }

            public float getSpeed() {
                return 8f;
            }

            public float getAttackDamageBonus() {
                return 8f;
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_GOLD_TOOL;
            }

            public int getEnchantmentValue() {
                return 18;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(net.minecraft.world.item.Items.OBSIDIAN);
            }
        }, pProperties.attributes(ObsidianBulwark.createAttributes()).rarity(WLEnumExtensions.RARITY_GOLD.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID,  8, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID,  -2.8, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ARMOR,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "armor"), 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "knockback_resistance"), 0.3, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack pItemstack, LivingEntity pLivingEntity) {
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemstack) {
        return UseAnim.BLOCK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity) {
        this.releaseUsing(pItemStack, pLevel, pLivingEntity, 0);
        return pItemStack;
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
                pServerLevel.sendParticles(ParticleTypes.HEART, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 6, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0);
                pServerLevel.sendParticles(ParticleTypes.WAX_ON, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntityHalfY, pLivingEntity.getZ(), 16, pLivingEntityHalfX, pLivingEntityHalfY / 2, pLivingEntityHalfZ, 0);
            }

            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_OBSIDIAN_BULWARK_SHIELD.get(), SoundSource.PLAYERS, 1.2f, 1f,0);

            float AbsorptionShield = pLivingEntity.getAbsorptionAmount() + 2;
            if (AbsorptionShield > 6) {
                AbsorptionShield = 6;
            }

            int BulwarkEffectLevel = 2 - Mth.floor(pLivingEntity.getAbsorptionAmount() + 2 - AbsorptionShield);
            if (pLivingEntity.hasEffect(WLEffects.BULWARK)) {
                pLivingEntity.addEffect(new MobEffectInstance(WLEffects.BULWARK, 1200, pLivingEntity.getEffect(WLEffects.BULWARK).getAmplifier() + BulwarkEffectLevel, true, true));
            } else {
                pLivingEntity.addEffect(new MobEffectInstance(WLEffects.BULWARK, 1200, BulwarkEffectLevel - 1, true, true));
            }

            if (pLivingEntity.getAbsorptionAmount() < AbsorptionShield) {
                pLivingEntity.setAbsorptionAmount(AbsorptionShield);
            }

            pItemStack.hurtAndBreak(8, pLivingEntity, pLivingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.awardStat(Stats.ITEM_USED.get(this));

                pPlayer.getCooldowns().addCooldown(this, 300);
                pPlayer.getCooldowns().addCooldown(WLItems.SOULGORGE.get(), 300);
            }
        } else {
            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.getCooldowns().addCooldown(this, 10);
                pPlayer.getCooldowns().addCooldown(WLItems.SOULGORGE.get(), 10);
            }
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            pServerLevel.sendParticles(ParticleTypes.WAX_ON, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.01);
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
            pTooltipComponents.add(Component.translatable("item.wanderlust.two_handed"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.obsidian_bulwark.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.obsidian_bulwark.shift_desc_2"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemstack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemstack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
