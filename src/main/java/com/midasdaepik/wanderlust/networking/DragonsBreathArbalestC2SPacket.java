package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.entity.DragonsBreath;
import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLUtil;
import com.midasdaepik.wanderlust.registries.WLItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

public record DragonsBreathArbalestC2SPacket() implements CustomPacketPayload {
    public static final Type<DragonsBreathArbalestC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "dragons_breath_arbalest_c2s_packet"));

    public static final DragonsBreathArbalestC2SPacket INSTANCE = new DragonsBreathArbalestC2SPacket();
    public static final StreamCodec<ByteBuf, DragonsBreathArbalestC2SPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pServerPlayer = pContext.player();
            ServerLevel pLevel = (ServerLevel) pServerPlayer.level();
            ItemStack pMainhandItem = pServerPlayer.getMainHandItem();

            if (pMainhandItem.getItem() == WLItems.DRAGONS_BREATH_ARBALEST.get() && !pServerPlayer.getCooldowns().isOnCooldown(WLItems.DRAGONS_BREATH_ARBALEST.get())) {

                pLevel.playSeededSound(null, pServerPlayer.getEyePosition().x, pServerPlayer.getEyePosition().y, pServerPlayer.getEyePosition().z, SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1.5f, 1.2f,0);

                pLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pServerPlayer.getEyePosition().x, pServerPlayer.getEyePosition().y, pServerPlayer.getEyePosition().z, 24, 0.3, 0.3, 0.3, 0.03);

                final Vec3 AABBCenter = new Vec3(pServerPlayer.getX(), pServerPlayer.getY(), pServerPlayer.getZ());
                Set<DragonsBreath> pFoundTarget = new HashSet<>(pLevel.getEntitiesOfClass(DragonsBreath.class, new AABB(AABBCenter, AABBCenter).inflate(16d, 16d, 16d), e -> true));
                for (DragonsBreath pDragonsBreathEntityIterator : pFoundTarget) {
                    if (pDragonsBreathEntityIterator.getOwner() == pServerPlayer) {
                        final Vec3 EntityIteratorAABBCenter = new Vec3(pDragonsBreathEntityIterator.getX(), pDragonsBreathEntityIterator.getY(), pDragonsBreathEntityIterator.getZ());
                        Set<LivingEntity> pEntityIteratorFoundTarget = new HashSet<>(pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(EntityIteratorAABBCenter, EntityIteratorAABBCenter).inflate(2.5d, 2.5d, 2.5d), e -> true));
                        for (LivingEntity pEntityIterator : pEntityIteratorFoundTarget) {
                            pEntityIterator.hurt(new DamageSource(pLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "magic"))), pDragonsBreathEntityIterator.getOwner()), pDragonsBreathEntityIterator.getAttackDamage() * 1.5f);
                            pEntityIterator.setDeltaMovement(pEntityIterator.getDeltaMovement().x, getyVelocity(pDragonsBreathEntityIterator, pEntityIterator), pEntityIterator.getDeltaMovement().z);
                            pEntityIterator.addEffect(new MobEffectInstance(WLEffects.PLUNGING, 80, 0));
                        }

                        pLevel.playSeededSound(null, EntityIteratorAABBCenter.x, EntityIteratorAABBCenter.y, EntityIteratorAABBCenter.z, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.NEUTRAL, 0.8f, 1.2f,0);
                        pLevel.sendParticles(ParticleTypes.DRAGON_BREATH, EntityIteratorAABBCenter.x, EntityIteratorAABBCenter.y, EntityIteratorAABBCenter.z, 16, 0.1, 0.1, 0.1, 0.05);
                        WLUtil.particleSphere(pLevel, ParticleTypes.DRAGON_BREATH, EntityIteratorAABBCenter.x, EntityIteratorAABBCenter.y, EntityIteratorAABBCenter.z, 2.5);

                        pDragonsBreathEntityIterator.discard();
                    }
                }

                pMainhandItem.hurtAndBreak(2, pServerPlayer, EquipmentSlot.MAINHAND);

                pServerPlayer.getCooldowns().addCooldown(pMainhandItem.getItem(), 80);
                pServerPlayer.awardStat(Stats.ITEM_USED.get(pMainhandItem.getItem()));
            }
        });
        return true;
    }

    private static double getyVelocity(DragonsBreath pDragonsBreathEntityIterator, LivingEntity pEntityIterator) {
        double yVelocityModifier = pDragonsBreathEntityIterator.getAttackDamage() * 0.1 + 0.2;
        double yVelocity = pEntityIterator.getDeltaMovement().y;
        if (yVelocity < yVelocityModifier) {
            yVelocity += yVelocityModifier;
            if (yVelocity > yVelocityModifier * 1.5) {
                yVelocity = yVelocityModifier * 1.5;
            }
        }
        return yVelocity;
    }
}