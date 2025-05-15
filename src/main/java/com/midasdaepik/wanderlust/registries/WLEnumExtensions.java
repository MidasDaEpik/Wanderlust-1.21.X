package com.midasdaepik.wanderlust.registries;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class WLEnumExtensions {
    public static final EnumProxy<Rarity> RARITY_BLAZE = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:blaze", (UnaryOperator<Style>) style -> style.withColor(0xFBB111)
    );

    public static final EnumProxy<Rarity> RARITY_DRAGON = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:dragon", (UnaryOperator<Style>) style -> style.withColor(0xD157EB)
    );

    public static final EnumProxy<Rarity> RARITY_ELDER = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:elder", (UnaryOperator<Style>) style -> style.withColor(0xCEC3B3)
    );

    public static final EnumProxy<Rarity> RARITY_FROST = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:frost", (UnaryOperator<Style>) style -> style.withColor(0xA5F8FD)
    );

    public static final EnumProxy<Rarity> RARITY_GOLD = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:gold", (UnaryOperator<Style>) style -> style.withColor(0xFDCD24)
    );

    public static final EnumProxy<Rarity> RARITY_MYCORIS = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:mycoris", (UnaryOperator<Style>) style -> style.withColor(0x7A120E)
    );

    public static final EnumProxy<Rarity> RARITY_PHANTOM = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:phantom", (UnaryOperator<Style>) style -> style.withColor(0x5C73D0)
    );

    public static final EnumProxy<Rarity> RARITY_PYROSWEEP = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:pyrosweep", (UnaryOperator<Style>) style -> style.withColor(0xE33F10)
    );

    public static final EnumProxy<Rarity> RARITY_SCULK = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:sculk", (UnaryOperator<Style>) style -> style.withColor(0x05717A)
    );

    public static final EnumProxy<Rarity> RARITY_SOULGORGE = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:soulgorge", (UnaryOperator<Style>) style -> style.withColor(0x06A4A9)
    );

    public static final EnumProxy<Rarity> RARITY_WARPED_RAPIER = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:warped_rapier", (UnaryOperator<Style>) style -> style.withColor(0x27CFB1)
    );

    public static final EnumProxy<Rarity> RARITY_WARPTHISTLE = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:warpthistle", (UnaryOperator<Style>) style -> style.withColor(0x268B79)
    );

    public static final EnumProxy<Rarity> RARITY_WIND = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:wind", (UnaryOperator<Style>) style -> style.withColor(0x958DD3)
    );

    public static final EnumProxy<Rarity> RARITY_WITHERBLADE = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:witherblade", (UnaryOperator<Style>) style -> style.withColor(0x534B4B)
    );
}
