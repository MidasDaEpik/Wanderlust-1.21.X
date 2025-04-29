package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.networking.PyrosweepChargeSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.*;
import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.PYROSWEEP_CHARGE;

public class Pyrosweep extends SwordItem {
    public Pyrosweep(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemPyrosweepDurability.get();
            }

            public float getSpeed() {
                return 9f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemPyrosweepAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
            }

            public int getEnchantmentValue() {
                return 18;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(Items.NETHERITE_SCRAP);
            }
        }, pProperties.fireResistant().attributes(Pyrosweep.createAttributes()).rarity(WLEnumExtensions.RARITY_PYROSWEEP.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemPyrosweepAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemPyrosweepAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.BURNING_TIME,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "burning_time.pyrosweep"), WLAttributeConfig.CONFIG.ItemPyrosweepBurnTime.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.STEP_HEIGHT,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "step_height.pyrosweep"), WLAttributeConfig.CONFIG.ItemPyrosweepStepHeight.get(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemStack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public boolean hurtEnemy(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pPlayer) {
            if (pPlayer.getAttackStrengthScale(0) >= 0.9F) {
                attackEffects(pItemStack, pTarget, pAttacker);

                if (pPlayer.level() instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                    int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
                    if (PyrosweepCharge < 16) {
                        PyrosweepCharge = Mth.clamp(PyrosweepCharge + 1, 0, 16);
                        pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                        PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepChargeSyncS2CPacket(PyrosweepCharge));
                    }
                }
            }
        } else {
            attackEffects(pItemStack, pTarget, pAttacker);
        }

        return super.hurtEnemy(pItemStack, pTarget, pAttacker);
    }

    public void attackEffects(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        int PyrosweepCharge = pAttacker.getData(PYROSWEEP_CHARGE);
        Level pLevel = pAttacker.level();
        if (pLevel instanceof ServerLevel pServerLevel && PyrosweepCharge > 0) {
            int BurnDamage = Mth.floor((float) PyrosweepCharge / 2);
            pTarget.hurt(WLDamageSource.damageSource(pServerLevel, pAttacker, WLDamageSource.BURN_NO_COOLDOWN), BurnDamage);
            pTarget.igniteForTicks(60);

            AABB pTargetSize = pTarget.getBoundingBox();
            pServerLevel.sendParticles(ParticleTypes.FLAME, pTarget.getX(), pTarget.getY() + pTargetSize.getYsize() / 2, pTarget.getZ(), 6, pTargetSize.getXsize() / 2, pTargetSize.getYsize() / 4, pTargetSize.getZsize() / 2, 0);
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        int PyrosweepCharge = pLivingEntity.getData(PYROSWEEP_CHARGE);
        if (PyrosweepCharge < 1) {
            pLivingEntity.stopUsingItem();
            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.getCooldowns().addCooldown(this, 20);
            }
        } else {
            if (pLevel instanceof ServerLevel pServerLevel) {
                AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
                pServerLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
        if (pPlayer.isCrouching()) {
            if (PyrosweepCharge >= 1) {
                pPlayer.startUsingItem(pHand);
                return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
            } else {
                return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
            }
        } else {
            if (PyrosweepCharge >= 6) {
                Vec3 pMovement = pPlayer.getDeltaMovement();
                Float pXRot = pPlayer.getYRot();
                pPlayer.setDeltaMovement(pMovement.x + Math.sin(pXRot * Math.PI / 180) * -1.5, 0, pMovement.z + Math.cos(pXRot * Math.PI / 180) * 1.5);

                pPlayer.level().playSeededSound(null, pPlayer.getEyePosition().x, pPlayer.getEyePosition().y, pPlayer.getEyePosition().z, WLSounds.ITEM_PYROSWEEP_DASH, SoundSource.PLAYERS, 1f, 1f, 0);

                pPlayer.setData(PYROSWEEP_DASH, 10);

                PyrosweepCharge -= 6;
                pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                if (pPlayer instanceof ServerPlayer pServerPlayer) {
                    PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepChargeSyncS2CPacket(PyrosweepCharge));
                }

                pPlayer.getItemInHand(pHand).hurtAndBreak(3, pPlayer, pHand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);

                pPlayer.awardStat(Stats.ITEM_USED.get(this));

                pPlayer.getCooldowns().addCooldown(this, 20);
                return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
            } else {
                return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.critless"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.two_handed"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.pyrosweep.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.pyrosweep.shift_desc_2"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.pyrosweep.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.pyrosweep.shift_desc_4"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.pyrosweep.shift_desc_5"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.pyrosweep.shift_desc_6"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
