package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.misc.LevelInterface;
import com.midasdaepik.wanderlust.misc.WLUtil;
import com.midasdaepik.wanderlust.registries.WLItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector4d;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.BLAZE_REAP_CHARGE;

public record BlazeReapC2SPacket() implements CustomPacketPayload {
    public static final Type<BlazeReapC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "blaze_reap_c2s_packet"));

    public static final BlazeReapC2SPacket INSTANCE = new BlazeReapC2SPacket();
    public static final StreamCodec<ByteBuf, BlazeReapC2SPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext pContext) {
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.player();
            ServerLevel pLevel = (ServerLevel) pPlayer.level();
            ItemStack pMainhandItem = pPlayer.getMainHandItem();

            int BlazeReapCharge = pPlayer.getData(BLAZE_REAP_CHARGE);
            if (pMainhandItem.getItem() == WLItems.BLAZE_REAP.get() && BlazeReapCharge > 0 && pPlayer.level() instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                HitResult pRaycast = WLUtil.raycast(pLevel, pPlayer, ClipContext.Fluid.NONE, pPlayer.blockInteractionRange(), pPlayer.entityInteractionRange());
                if (pRaycast.getType() != HitResult.Type.MISS) {
                    Vec3 pLocation;
                    float pExplosionRadius, pEntityExplosionRadius;
                    if (pRaycast.getType() == HitResult.Type.ENTITY) {
                        Entity pTarget = ((EntityHitResult) pRaycast).getEntity();
                        pLocation = new Vec3(pTarget.getX(), pTarget.getY() + pTarget.getBoundingBox().getYsize() * 0.5, pTarget.getZ());
                        pExplosionRadius = 1f;
                        pEntityExplosionRadius = 2.5f;
                    } else {
                        pLocation = pRaycast.getLocation();
                        pExplosionRadius = 3f;
                        pEntityExplosionRadius = 1.5f;
                    }

                    ((LevelInterface) pLevel).wanderlust_1_21_X$explode(pPlayer, Explosion.getDefaultDamageSource(pLevel, pPlayer), null, pLocation.x, pLocation.y, pLocation.z, pExplosionRadius, Mth.nextInt(RandomSource.create(), 1, 5) == 1, Level.ExplosionInteraction.TNT, true, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE,
                            pEntityExplosionRadius, 25, false,
                            BlockTags.MINEABLE_WITH_PICKAXE,
                            new Vector4d(1, 2, 3, 0.5),
                            new Vector4d(3, 6, 6, 2));

                    pPlayer.setData(BLAZE_REAP_CHARGE, 0);
                    PacketDistributor.sendToPlayer(pServerPlayer, new BlazeReapChargeSyncS2CPacket(0));

                    pMainhandItem.hurtAndBreak(5, pServerPlayer, EquipmentSlot.MAINHAND);

                    pServerPlayer.awardStat(Stats.ITEM_USED.get(pMainhandItem.getItem()));
                    pPlayer.getCooldowns().addCooldown(WLItems.BLAZE_REAP.get(), 280);
                }
            }
        });
        return true;
    }
}