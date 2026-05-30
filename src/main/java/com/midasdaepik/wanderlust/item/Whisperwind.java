package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.registries.WLSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.SPECIAL_ARROW_TYPE;

public class Whisperwind extends WLBowItem {
    public Whisperwind(Properties pProperties) {
        super(pProperties.durability(WLAttributeConfig.CONFIG.ItemWhisperwindDurability.get()).attributes(Whisperwind.createAttributes()).rarity(WLEnumExtensions.RARITY_BREEZE.getValue()),
                WhisperwindStats.INSTANCE);
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.whisperwind"),  WLAttributeConfig.CONFIG.ItemWhisperwindMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed.whisperwind_offhand"),  WLAttributeConfig.CONFIG.ItemWhisperwindOffhandMovementSpeed.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.OFFHAND)
                .build();
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pItemstack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.BREEZE_ROD);
    }

    @Override
    public boolean canAttackBlock(BlockState pBlockState, Level pLevel, BlockPos pBlockPos, Player pPlayer) {
        return false;
    }

    @Override
    public void playShootSound(ItemStack pItemstack, Level pLevel, Player pPlayer, float pArrowPower, int pTimeLeft) {
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), WLSounds.ITEM_WHISPERWIND_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + pArrowPower * 0.5F);
    }

    @Override
    public void modifyArrow(Level pLevel, AbstractArrow pArrow, ArrowItem pArrowItem, LivingEntity pLivingEntity, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit) {
        pArrow.setData(SPECIAL_ARROW_TYPE, 0);
    }

    @Override
    public void appendHoverText(ItemStack pItemstack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.whisperwind.shift_desc_1"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.whisperwind.shift_desc_2"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        pTooltipComponents.add(Component.literal(" ").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon"))));
        super.appendHoverText(pItemstack, pContext, pTooltipComponents, pIsAdvanced);
    }

    public static final class WhisperwindStats extends WLBowItem.BowItemStats {
        public static final Whisperwind.WhisperwindStats INSTANCE = new Whisperwind.WhisperwindStats(
                WLAttributeConfig.CONFIG.ItemWhisperwindDamageModifier.get().floatValue(),
                WLAttributeConfig.CONFIG.ItemWhisperwindVelocityModifier.get().floatValue(),
                WLAttributeConfig.CONFIG.ItemWhisperwindDrawSpeedModifier.get().floatValue(),
                WLAttributeConfig.CONFIG.ItemWhisperwindAccuracyModifier.get().floatValue());

        public WhisperwindStats(float pDamageModifier, float pVelocityModifier, float pDrawSpeedModifier, float pAccuracyModifier) {
            super(pDamageModifier, pVelocityModifier, pDrawSpeedModifier, pAccuracyModifier);
        }
    }
}
