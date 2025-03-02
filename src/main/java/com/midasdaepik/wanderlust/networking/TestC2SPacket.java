package com.midasdaepik.wanderlust.networking;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TestC2SPacket(ItemStack first, ItemStack second) implements CustomPacketPayload {
    public static final Type<TestC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "two_stacks"));

    // And we have a stream codec, here using RFBB (RegistryFriendlyByteBuf) because item stacks require it.
    public static final StreamCodec<RegistryFriendlyByteBuf, TestC2SPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,
            TestC2SPacket::first,

            ItemStack.OPTIONAL_STREAM_CODEC,
            TestC2SPacket::second,

            TestC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ServerLevel level = (ServerLevel) player.level();

            player.sendSystemMessage(Component.literal("Packet received"));
        });
        return true;
    }
}