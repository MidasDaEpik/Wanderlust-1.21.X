package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.networking.BlazeReapChargeSyncS2CPacket;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.BLAZE_REAP_CHARGE;

public class BlazeReap extends PickaxeItem {
    public BlazeReap(Properties pProperties) {
        super(new Tier() {
            public int getUses() {
                return WLAttributeConfig.CONFIG.ItemBlazeReapDurability.get();
            }

            public float getSpeed() {
                return 6f;
            }

            public float getAttackDamageBonus() {
                return (float) (WLAttributeConfig.CONFIG.ItemBlazeReapAttackDamage.get() - 1);
            }

            public TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
            }

            public int getEnchantmentValue() {
                return 12;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(Items.BLAZE_ROD);
            }
        }, pProperties.fireResistant().attributes(BlazeReap.createAttributes()).rarity(WLEnumExtensions.RARITY_BLAZE.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, WLAttributeConfig.CONFIG.ItemBlazeReapAttackDamage.get() - 1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, WLAttributeConfig.CONFIG.ItemBlazeReapAttackSpeed.get() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return 60;
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
    public void postHurtEnemy(ItemStack pItemStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pItemStack.hurtAndBreak(1, pAttacker, EquipmentSlot.MAINHAND);
        if (pAttacker instanceof Player pPlayer) {
            pPlayer.getCooldowns().addCooldown(this, 10);
        }
    }

    @Override
    public void releaseUsing(ItemStack pItemStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;

        if (pTimeUsing >= 30) {
            pLivingEntity.level().playSeededSound(null, pLivingEntity.getEyePosition().x, pLivingEntity.getEyePosition().y, pLivingEntity.getEyePosition().z, WLSounds.ITEM_BLAZE_REAP_ACTIVATE, SoundSource.PLAYERS, 1f, 1f, 0);

            if (pLevel instanceof ServerLevel pServerLevel && pLivingEntity instanceof ServerPlayer pServerPlayer) {
                pServerPlayer.setData(BLAZE_REAP_CHARGE, 160);
                PacketDistributor.sendToPlayer(pServerPlayer, new BlazeReapChargeSyncS2CPacket(160));
            }
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pItemStack, int pTimeLeft) {
        int pTimeUsing = this.getUseDuration(pItemStack, pLivingEntity) - pTimeLeft;

        if (pLevel instanceof ServerLevel pServerLevel) {
            AABB pLivingEntitySize = pLivingEntity.getBoundingBox();
            if (pTimeUsing >= 30) {
                pServerLevel.sendParticles(ParticleTypes.FLAME, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.01);

            } else {
                pServerLevel.sendParticles(ParticleTypes.SMOKE, pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntitySize.getYsize() / 2, pLivingEntity.getZ(), 1, pLivingEntitySize.getXsize() / 2, pLivingEntitySize.getYsize() / 4, pLivingEntitySize.getZsize() / 2, 0.01);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (pPlayer.getData(BLAZE_REAP_CHARGE) > 0) {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));

        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.blaze_reap.shift_desc_1"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.blaze_reap.shift_desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.blaze_reap.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.blaze_reap.shift_desc_4", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));

        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        if (pItemStack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
