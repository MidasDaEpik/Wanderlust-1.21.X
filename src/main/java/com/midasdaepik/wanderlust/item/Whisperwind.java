package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.RREnumExtensions;
import com.midasdaepik.wanderlust.registries.RRUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static com.midasdaepik.wanderlust.registries.RRAttachmentTypes.SPECIAL_ARROW_TYPE;

public class Whisperwind extends BowItem {
    public Whisperwind(Properties pProperties) {
        super(pProperties.durability(576).attributes(Whisperwind.createAttributes()).rarity(RREnumExtensions.RARITY_WIND.getValue()));
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "movement_speed"),  0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.MAINHAND)
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
    public int getUseDuration(ItemStack pItemstack, LivingEntity pLivingEntity) {
        return 72000;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 10;
    }

    //Modify Sound Here
    @Override
    public void releaseUsing(ItemStack pItemstack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player pPlayer) {
            ItemStack ProjectileItemStack = pPlayer.getProjectile(pItemstack);
            if (!ProjectileItemStack.isEmpty()) {
                int i = this.getUseDuration(pItemstack, pEntityLiving) - pTimeLeft;
                i = EventHooks.onArrowLoose(ProjectileItemStack, pLevel, pPlayer, i, !ProjectileItemStack.isEmpty());
                if (i < 0) return;
                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(pItemstack, ProjectileItemStack, pPlayer);
                    if (pLevel instanceof ServerLevel serverlevel && !list.isEmpty()) {
                        this.shoot(serverlevel, pPlayer, pPlayer.getUsedItemHand(), pItemstack, list, f * 3.0F, 1.0F, f == 1.0F, null);
                    }
                    pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BREEZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    //Modfy Projectile Stats Here
    @Override
    protected Projectile createProjectile(Level pLevel, LivingEntity pLivingEntity, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit) {
        ArrowItem arrowitem = pAmmo.getItem() instanceof ArrowItem arrowitem1 ? arrowitem1 : (ArrowItem)Items.ARROW;
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, pAmmo, pLivingEntity, pWeapon);
        if (pIsCrit) {
            abstractarrow.setCritArrow(true);
        }
        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() * 0.66);
        abstractarrow.setData(SPECIAL_ARROW_TYPE, 0);

        return customArrow(abstractarrow, pAmmo, pWeapon);
    }

    //Modify Projectile Firing Here
    @Override
    protected void shootProjectile(LivingEntity pLivingEntity, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @Nullable LivingEntity pTarget) {
        pProjectile.shootFromRotation(pLivingEntity, pLivingEntity.getXRot(), pLivingEntity.getYRot() + pAngle, 0.0F, pVelocity * 0.5f, pInaccuracy * 0.5f);
    }

    //Modify Draw Time Here
    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / 15.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public void appendHoverText(ItemStack pItemstack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (RRUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.whisperwind.shift_desc_1"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.whisperwind.shift_desc_2"));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info"));
        }
        if (pItemstack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemstack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
