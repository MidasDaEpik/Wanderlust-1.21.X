package com.midasdaepik.wanderlust.registries;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class RREnumExtensions {
    public static final EnumProxy<Rarity> RARITY_ELDER = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:elder", (UnaryOperator<Style>) style -> style.withColor(13550515)
    );

    public static final EnumProxy<Rarity> RARITY_WIND = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:wind", (UnaryOperator<Style>) style -> style.withColor(9801171)
    );

    public static final EnumProxy<Rarity> RARITY_BLAZE = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:blaze", (UnaryOperator<Style>) style -> style.withColor(16494865)
    );

    public static final EnumProxy<Rarity> RARITY_GOLD = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:gold", (UnaryOperator<Style>) style -> style.withColor(16633124)
    );

    public static final EnumProxy<Rarity> RARITY_WARPED_RAPIER = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:warped_rapier", (UnaryOperator<Style>) style -> style.withColor(2609073)
    );

    public static final EnumProxy<Rarity> RARITY_WITHERBLADE = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:witherblade", (UnaryOperator<Style>) style -> style.withColor(5458763)
    );

    public static final EnumProxy<Rarity> RARITY_PYROSWEEP = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:pyrosweep", (UnaryOperator<Style>) style -> style.withColor(14892816)
    );

    public static final EnumProxy<Rarity> RARITY_MYCORIS = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:mycoris", (UnaryOperator<Style>) style -> style.withColor(8000014)
    );

    public static final EnumProxy<Rarity> RARITY_SOULGORGE = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:soulgorge", (UnaryOperator<Style>) style -> style.withColor(435369)
    );

    public static final EnumProxy<Rarity> RARITY_WARPTHISTLE = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:warpthistle", (UnaryOperator<Style>) style -> style.withColor(2526073)
    );

    public static final EnumProxy<Rarity> RARITY_SCULK = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:sculk", (UnaryOperator<Style>) style -> style.withColor(356730)
    );

    public static final EnumProxy<Rarity> RARITY_DRAGON = new EnumProxy<>(
            Rarity.class, -1, "wanderlust:dragon", (UnaryOperator<Style>) style -> style.withColor(13719531)
    );
}
