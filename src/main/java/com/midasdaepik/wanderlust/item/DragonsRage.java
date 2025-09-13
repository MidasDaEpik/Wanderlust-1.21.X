package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.networking.DragonChargeSyncS2CPacket;
import com.midasdaepik.wanderlust.networking.PyrosweepChargeSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.DRAGON_CHARGE;

public class DragonsRage extends SwordItem {
    public DragonsRage(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemDragonsRageDurability.get();
            }

            public float getSpeed() {
                return 9f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemDragonsRageAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
            }

            public int getEnchantmentValue() {
                return 14;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(WLItems.DRAGONBONE.get());
            }
        }, pProperties.attributes(DragonsRage.createAttributes()).rarity(WLEnumExtensions.RARITY_DRAGON.getValue()).component(WLDataComponents.NO_GRAVITY, true));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemDragonsRageAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemDragonsRageAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 1200;
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
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (pLivingEntity instanceof Player pPlayer) {
            pItemStack.hurtAndBreak(pTimeUsing / 20, pLivingEntity, pLivingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

            pPlayer.awardStat(Stats.ITEM_USED.get(this));

            pPlayer.getCooldowns().addCooldown(this, 20);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pPlayer) {
            if (pPlayer.getAttackStrengthScale(0) >= 0.9F) {
                attackEffects(pTarget, pAttacker);

                if (pPlayer.level() instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                    int DragonCharge = pPlayer.getData(DRAGON_CHARGE);
                    int DragonChargeCap = WLCommonConfig.CONFIG.DragonChargeCap.get();
                    if (DragonCharge < DragonChargeCap) {
                        DragonCharge = Math.min(DragonCharge + WLCommonConfig.CONFIG.DragonChargeOnHit.get(), DragonChargeCap);
                        pPlayer.setData(DRAGON_CHARGE, DragonCharge);
                        PacketDistributor.sendToPlayer(pServerPlayer, new DragonChargeSyncS2CPacket(DragonCharge));
                    }
                }
            }
        } else {
            attackEffects(pTarget, pAttacker);
        }

        return super.hurtEnemy(pItemStack, pTarget, pAttacker);
    }

    public static void attackEffects(LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(WLEffects.PLUNGING, 100, 0));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        int DragonCharge = pPlayer.getData(DRAGON_CHARGE);
        int DragonChargeSwordUse = WLCommonConfig.CONFIG.DragonChargeSwordUse.get();

        if (pPlayer.hasEffect(WLEffects.DRAGONS_ASCENSION) && pPlayer.getEffect(WLEffects.DRAGONS_ASCENSION).getAmplifier() == 1) {
            pPlayer.removeEffect(WLEffects.DRAGONS_ASCENSION);
            pPlayer.addEffect(new MobEffectInstance(WLEffects.DRAGONS_ASCENSION, 20, 2, true, false, true));

            Vec3 pPlayerLookAngle = pPlayer.getLookAngle();
            pPlayer.setDeltaMovement(new Vec3(pPlayerLookAngle.x * 2, pPlayerLookAngle.y * 2, pPlayerLookAngle.z * 2));

            if (pPlayer.level() instanceof ServerLevel pServerLevel) {
                pServerLevel.sendParticles(
                        WLUtil.orientedCircleVec3dInput(new Vector3f(0.64f, 0.08f, 0.80f), 8,0.6f, 3.6f,  pPlayerLookAngle.x, pPlayerLookAngle.y, pPlayerLookAngle.z),
                        pPlayer.getX() - pPlayerLookAngle.x, pPlayer.getY() + pPlayer.getBoundingBox().getYsize() / 2 - pPlayerLookAngle.y, pPlayer.getZ() - pPlayerLookAngle.z, 1, 0, 0, 0, 0);
            }

            pPlayer.getCooldowns().addCooldown(this, 800);

            return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));

        } else if (DragonCharge >= DragonChargeSwordUse && pPlayer.isCrouching() && pPlayer.onGround() && !pPlayer.hasEffect(WLEffects.DRAGONS_ASCENSION)) {
            pPlayer.addEffect(new MobEffectInstance(WLEffects.DRAGONS_ASCENSION, 80, 0, true, false, true));

            if (pPlayer.isFallFlying()) {
                pPlayer.stopFallFlying();
            }
            if (pPlayer.isSprinting()) {
                pPlayer.setSprinting(false);
            }
            if (pPlayer.isSwimming()) {
                pPlayer.setSwimming(false);
            }

            DragonCharge -= DragonChargeSwordUse;
            pPlayer.setData(DRAGON_CHARGE, DragonCharge);
            if (pPlayer instanceof ServerPlayer pServerPlayer) {
                PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepChargeSyncS2CPacket(DragonCharge));
            }

            pPlayer.getItemInHand(pHand).hurtAndBreak(10, pPlayer, pHand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);

            pPlayer.awardStat(Stats.ITEM_USED.get(this));

            pPlayer.getCooldowns().addCooldown(this, 80);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
        } else {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_rage.shift_desc_1", Component.translatable("item.wanderlust.breath_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_rage.shift_desc_2"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_rage.shift_desc_3", Component.translatable("item.wanderlust.breath_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_rage.shift_desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_rage.shift_desc_5"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_rage.shift_desc_6"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_rage.shift_desc_7"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_rage.shift_desc_8", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}