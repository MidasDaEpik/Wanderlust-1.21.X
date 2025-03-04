package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.entity.FireColumn;
import com.midasdaepik.wanderlust.networking.PyrosweepSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
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
                return 1270;
            }

            public float getSpeed() {
                return 9f;
            }

            public float getAttackDamageBonus() {
                return 9f;
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
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID,  5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID,  -3, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.BURNING_TIME,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "burning_time"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.STEP_HEIGHT,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "step_height"), 0.5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 160;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pItemStack) {
        return UseAnim.CROSSBOW;
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
                attackEffects(pItemStack, pTarget, pAttacker);

                if (pPlayer.level() instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                    int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
                    if (PyrosweepCharge < 16) {
                        PyrosweepCharge = Mth.clamp(PyrosweepCharge + 1, 0, 16);
                        pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                        PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepSyncS2CPacket(PyrosweepCharge));
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
            int BurnDamage = Mth.floor((float) PyrosweepCharge / 4 + 1);
            pTarget.hurt(WLDamageSource.damageSource(pServerLevel, pAttacker, WLDamageSource.BURN_NO_COOLDOWN), BurnDamage);
            pTarget.igniteForTicks(60);

            pServerLevel.sendParticles(ParticleTypes.FLAME, pTarget.getX(), pTarget.getY() + 1, pTarget.getZ(), 6, 0.6, 0.6, 0.6, 0);
        }
    }

    @Override
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (pLivingEntity.isCrouching() && pTimeUsing >= 20) {
            int PyrosweepCharge = pLivingEntity.getData(PYROSWEEP_CHARGE);
            BlockHitResult pRaytrace = WLUtil.blockHitRaycast(pLevel, pLivingEntity, ClipContext.Fluid.ANY, 24);
            BlockPos pLookPos = pRaytrace.getBlockPos().relative(pRaytrace.getDirection());

            FireColumn pFireColumn = new FireColumn(pLivingEntity.level(), pLivingEntity, 240, 40);
            pFireColumn.setPos(pLookPos.getX() + 0.5, pLookPos.getY() + 0.5, pLookPos.getZ() + 0.5);
            pLivingEntity.level().addFreshEntity(pFireColumn);

            PyrosweepCharge -= 6;
            pLivingEntity.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
            if (pLivingEntity instanceof ServerPlayer pServerPlayer) {
                PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepSyncS2CPacket(PyrosweepCharge));
            }
        }
        if (pLivingEntity instanceof Player pPlayer) {
            pPlayer.getCooldowns().addCooldown(this, 20);
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;
        if (!pLivingEntity.isCrouching()) {
            pLivingEntity.stopUsingItem();
            if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.getCooldowns().addCooldown(this, 20);
            }
        }
        if (pLevel instanceof ClientLevel pClientLevel) {
            BlockHitResult pRaytrace = WLUtil.blockHitRaycast(pLevel, pLivingEntity, ClipContext.Fluid.ANY, 24);
            BlockPos pLookPos = pRaytrace.getBlockPos().relative(pRaytrace.getDirection());
            if (pTimeUsing >= 20) {
                pClientLevel.addParticle(ParticleTypes.FLAME, true, pLookPos.getX() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), pLookPos.getY() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), pLookPos.getZ() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), 0, 0, 0);
            } else {
                pClientLevel.addParticle(ParticleTypes.LARGE_SMOKE, true, pLookPos.getX() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), pLookPos.getY() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), pLookPos.getZ() + Mth.nextFloat(RandomSource.create(), 0.1f, 0.9f), 0, 0, 0);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        int PyrosweepCharge = pPlayer.getData(PYROSWEEP_CHARGE);
        if (pPlayer.isCrouching()) {
            if (PyrosweepCharge >= 6) {
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

                pPlayer.setData(PYROSWEEP_DASH, 10);

                PyrosweepCharge -= 6;
                pPlayer.setData(PYROSWEEP_CHARGE, PyrosweepCharge);
                if (pPlayer instanceof ServerPlayer pServerPlayer) {
                    PacketDistributor.sendToPlayer(pServerPlayer, new PyrosweepSyncS2CPacket(PyrosweepCharge));
                }

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
            pTooltipComponents.add(Component.translatable("item.wanderlust.pyrosweep.shift_desc_7"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
