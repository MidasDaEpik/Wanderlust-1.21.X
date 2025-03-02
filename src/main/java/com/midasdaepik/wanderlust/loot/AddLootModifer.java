package com.midasdaepik.wanderlust.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddLootModifer extends LootModifier {
    private final Item item;

    public static final Supplier<MapCodec<AddLootModifer>> MAP_CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(
                    inst -> LootModifier.codecStart(inst).and(
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(m -> m.item)
                    ).apply(inst, AddLootModifer::new)
            )
    );

    public AddLootModifer(LootItemCondition[] pConditionsIn, Item pItem) {
        super(pConditionsIn);
        this.item = pItem;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> pGeneratedLoot, LootContext pContext) {
        pGeneratedLoot.add(new ItemStack(this.item));
        return pGeneratedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return MAP_CODEC.get();
    }
}
