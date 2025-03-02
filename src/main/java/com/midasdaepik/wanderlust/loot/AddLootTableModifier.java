package com.midasdaepik.wanderlust.loot;

import com.google.common.base.Suppliers;
import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.mixin.LootContextAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class AddLootTableModifier extends LootModifier {
    private final String lootTable;

    public static final Supplier<MapCodec<AddLootTableModifier>> MAP_CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(
                    inst -> LootModifier.codecStart(inst).and(
                            Codec.STRING.fieldOf("loot_table").forGetter(m -> m.lootTable)
                    ).apply(inst, AddLootTableModifier::new)
            )
    );

    public AddLootTableModifier(LootItemCondition[] conditionsIn, String lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> pGeneratedLoot, LootContext pContext) {
        injectLoot(pContext, pGeneratedLoot);

        return pGeneratedLoot;
    }

    public void injectLoot(LootContext pContext, List<ItemStack> pOriginalLoot) {
        Optional<Holder.Reference<LootTable>> pOptionalLootTable = pContext.getResolver().get(Registries.LOOT_TABLE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, this.lootTable)));
        if (pOptionalLootTable.isEmpty()) {
            return;
        }

        LootTable pLootTable = pOptionalLootTable.get().value();
        ObjectArrayList<ItemStack> newItems = new ObjectArrayList<>();
        pLootTable.getRandomItems(((LootContextAccessor) pContext).getParams(), newItems::add);
        pOriginalLoot.addAll(newItems);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return MAP_CODEC.get();
    }
}
