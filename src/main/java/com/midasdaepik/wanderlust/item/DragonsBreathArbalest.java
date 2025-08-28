package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLDataComponents;
import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.SPECIAL_ARROW_TYPE;

public class DragonsBreathArbalest extends CrossbowItem {
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    private static final CrossbowItem.ChargingSounds DEFAULT_SOUNDS = new CrossbowItem.ChargingSounds(
            Optional.of(SoundEvents.CROSSBOW_LOADING_START), Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE), Optional.of(SoundEvents.CROSSBOW_LOADING_END)
    );

    public DragonsBreathArbalest(Properties pProperties) {
        super(pProperties.durability(930).rarity(WLEnumExtensions.RARITY_DRAGON.getValue()).component(WLDataComponents.NO_GRAVITY, true));
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pItemstack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(WLItems.DRAGONBONE);
    }

    @Override
    public boolean canAttackBlock(BlockState pBlockState, Level pLevel, BlockPos pBlockPos, Player pPlayer) {
        return false;
    }

    @Override
    public int getUseDuration(ItemStack pItemStack, LivingEntity pLivingEntity) {
        return getChargeDuration(pItemStack, pLivingEntity) + 3;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 10;
    }

    //Modify Charge Time Here
    public static int getChargeDuration(ItemStack stack, LivingEntity pLivingEntity) {
        float f = EnchantmentHelper.modifyCrossbowChargingTime(stack, pLivingEntity, 2.0F);
        return Mth.floor(f * 20.0F);
    }

    //Modify Velocity Here
    private static float getShootingPower(ChargedProjectiles projectile) {
        return projectile.contains(Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    //Modify Sound Here
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack, entityLiving) - timeLeft;
        float f = getPowerForTime(i, stack, entityLiving);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(entityLiving, stack)) {
            CrossbowItem.ChargingSounds crossbowitem$chargingsounds = this.getChargingSounds(stack);
            crossbowitem$chargingsounds.end()
                    .ifPresent(
                            p_352852_ -> level.playSound(
                                    null,
                                    entityLiving.getX(),
                                    entityLiving.getY(),
                                    entityLiving.getZ(),
                                    p_352852_.value(),
                                    entityLiving.getSoundSource(),
                                    1.0F,
                                    1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                            )
                    );
        }
    }

    private static boolean tryLoadProjectiles(LivingEntity pLivingEntity, ItemStack crossbowStack) {
        List<ItemStack> list = draw(crossbowStack, pLivingEntity.getProjectile(crossbowStack), pLivingEntity);
        if (!list.isEmpty()) {
            crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCharged(ItemStack crossbowStack) {
        ChargedProjectiles chargedprojectiles = crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        return !chargedprojectiles.isEmpty();
    }

    //Modify Firing Sound Here
    public void performShooting(
            Level level, LivingEntity pLivingEntity, InteractionHand pHand, ItemStack pItemStack, float velocity, float inaccuracy, @Nullable LivingEntity target
    ) {
        if (level instanceof ServerLevel serverlevel) {
            if (pLivingEntity instanceof Player pPlayer && net.neoforged.neoforge.event.EventHooks.onArrowLoose(pItemStack, pLivingEntity.level(), pPlayer, 1, true) < 0) return;
            ChargedProjectiles chargedprojectiles = pItemStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
                this.shoot(serverlevel, pLivingEntity, pHand, pItemStack, chargedprojectiles.getItems(), velocity, inaccuracy, pLivingEntity instanceof Player, target);
                if (pLivingEntity instanceof ServerPlayer pServerPlayer) {
                    CriteriaTriggers.SHOT_CROSSBOW.trigger(pServerPlayer, pItemStack);
                    pServerPlayer.awardStat(Stats.ITEM_USED.get(pItemStack.getItem()));
                    pServerPlayer.getCooldowns().addCooldown(this, 20);
                }
            }
        }
    }

    private static Vector3f getProjectileShotVector(LivingEntity pLivingEntity, Vec3 distance, float angle) {
        Vector3f vector3f = distance.toVector3f().normalize();
        Vector3f vector3f1 = new Vector3f(vector3f).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = pLivingEntity.getUpVector(1.0F);
            vector3f1 = new Vector3f(vector3f).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = new Vector3f(vector3f).rotateAxis((float) (Math.PI / 2), vector3f1.x, vector3f1.y, vector3f1.z);
        return new Vector3f(vector3f).rotateAxis(angle * (float) (Math.PI / 180.0), vector3f2.x, vector3f2.y, vector3f2.z);
    }

    //Modify Projectile Here
    @Override
    protected Projectile createProjectile(Level level, LivingEntity pLivingEntity, ItemStack pItemStack, ItemStack ammo, boolean isCrit) {
        Projectile projectile = super.createProjectile(level, pLivingEntity, pItemStack, ammo, isCrit);
        if (projectile instanceof AbstractArrow abstractarrow) {
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() * 1.6);
            abstractarrow.setData(SPECIAL_ARROW_TYPE, 1);
        }
        return projectile;
    }

    @Override
    protected void shootProjectile(
            LivingEntity pLivingEntity, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target
    ) {
        Vector3f vector3f;
        if (target != null) {
            double d0 = target.getX() - pLivingEntity.getX();
            double d1 = target.getZ() - pLivingEntity.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = target.getY(0.3333333333333333) - projectile.getY() + d2 * 0.2F;
            vector3f = getProjectileShotVector(pLivingEntity, new Vec3(d0, d3, d1), angle);
        } else {
            Vec3 vec3 = pLivingEntity.getUpVector(1.0F);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis((double)(angle * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = pLivingEntity.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);
        float f = getShotPitch(pLivingEntity.getRandom(), index);
        pLivingEntity.level().playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), SoundEvents.CROSSBOW_SHOOT, pLivingEntity.getSoundSource(), 1.0F, f);
    }

    private static float getShotPitch(RandomSource random, int index) {
        return index == 0 ? 1.0F : getRandomShotPitch((index & 1) == 1, random);
    }

    private static float getRandomShotPitch(boolean isHighPitched, RandomSource random) {
        float f = isHighPitched ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static float getPowerForTime(int timeLeft, ItemStack stack, LivingEntity pLivingEntity) {
        float f = (float)timeLeft / (float)getChargeDuration(stack, pLivingEntity);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    //Modify Sound Here
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (!level.isClientSide) {
            CrossbowItem.ChargingSounds crossbowitem$chargingsounds = this.getChargingSounds(stack);
            float f = (float)(stack.getUseDuration(livingEntity) - count) / (float)getChargeDuration(stack, livingEntity);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                crossbowitem$chargingsounds.start()
                        .ifPresent(
                                p_352849_ -> level.playSound(
                                        null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), p_352849_.value(), SoundSource.PLAYERS, 0.5F, 1.0F
                                )
                        );
            }

            if (f >= 0.5F && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                crossbowitem$chargingsounds.mid()
                        .ifPresent(
                                p_352855_ -> level.playSound(
                                        null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), p_352855_.value(), SoundSource.PLAYERS, 0.5F, 1.0F
                                )
                        );
            }
        }
    }

    CrossbowItem.ChargingSounds getChargingSounds(ItemStack stack) {
        return EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS).orElse(DEFAULT_SOUNDS);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        ChargedProjectiles chargedprojectiles = itemstack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            this.performShooting(level, pPlayer, pHand, itemstack, getShootingPower(chargedprojectiles), 1.0F, null);
            return InteractionResultHolder.consume(itemstack);
        } else if (!pPlayer.getProjectile(itemstack).isEmpty()) {
            this.startSoundPlayed = false;
            this.midLoadSoundPlayed = false;
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public void appendHoverText(ItemStack pItemstack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (WLUtil.ItemKeys.isHoldingShift()) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_breath_arbalest.shift_desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_breath_arbalest.shift_desc_2"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_breath_arbalest.shift_desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_breath_arbalest.shift_desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.dragons_breath_arbalest.shift_desc_5", Component.translatable("item.wanderlust.cooldown_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        } else {
            pTooltipComponents.add(Component.translatable("item.wanderlust.shift_desc_info", Component.translatable("item.wanderlust.shift_desc_info_icon").setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "icon")))));
        }
        ChargedProjectiles pChargedProjectiles = pItemstack.get(DataComponents.CHARGED_PROJECTILES);
        if ((pChargedProjectiles != null && !pChargedProjectiles.isEmpty()) || pItemstack.isEnchanted()) {
            pTooltipComponents.add(Component.empty());
        }
        super.appendHoverText(pItemstack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
