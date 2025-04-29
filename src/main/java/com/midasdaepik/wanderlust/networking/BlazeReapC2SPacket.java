package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.misc.WLUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

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
                    if (pRaycast.getType() == HitResult.Type.ENTITY) {
                        EntityHitResult pEntityRaycast = (EntityHitResult) pRaycast;
                        Entity pTarget = pEntityRaycast.getEntity();

                        pServerLevel.explode(pPlayer, pTarget.getX(), pTarget.getY() + pTarget.getBoundingBox().getYsize() * 0.5, pTarget.getZ(), 2f, true, Level.ExplosionInteraction.TNT);

                    } else if (pRaycast.getType() == HitResult.Type.BLOCK) {
                        BlockHitResult pBlockRaycast = (BlockHitResult) pRaycast;
                        Vec3 pLocation = pBlockRaycast.getLocation();

                        pServerLevel.explode(pPlayer, pLocation.x, pLocation.y, pLocation.z, 2f, true, Level.ExplosionInteraction.TNT);
                    }

                    pPlayer.setData(BLAZE_REAP_CHARGE, 0);
                    PacketDistributor.sendToPlayer(pServerPlayer, new BlazeReapChargeSyncS2CPacket(0));

                    pPlayer.getCooldowns().addCooldown(WLItems.BLAZE_REAP.get(), 80);
                }
            }
        });
        return true;
    }
}