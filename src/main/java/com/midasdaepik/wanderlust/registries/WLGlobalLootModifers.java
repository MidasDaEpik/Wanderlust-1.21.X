package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.loot.AddLootModifer;
import com.midasdaepik.wanderlust.loot.AddLootTableModifier;
import com.midasdaepik.wanderlust.loot.AddMultipleLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class WLGlobalLootModifers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Wanderlust.MOD_ID);

    public static final Supplier<MapCodec<? extends  IGlobalLootModifier>> ADD_LOOT_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("add", AddLootModifer.MAP_CODEC);

    public static final Supplier<MapCodec<? extends  IGlobalLootModifier>> ADD_MULTIPLE_LOOT_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("add_multiple", AddMultipleLootModifier.MAP_CODEC);

    public static final Supplier<MapCodec<? extends  IGlobalLootModifier>> ADD_LOOT_TABLE_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("add_loot_table", AddLootTableModifier.MAP_CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
