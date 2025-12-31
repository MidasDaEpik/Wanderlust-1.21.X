package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;

public class WLEnchantments {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static final ResourceKey<Enchantment> BOLSTERED = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "bolstered"));

    public static final ResourceKey<Enchantment> CONCEALMENT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "concealment"));

    public static final ResourceKey<Enchantment> NAMELESS = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "nameless"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        register(context, BOLSTERED, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(WLTags.ENCHANTABLE_MASK),
                        items.getOrThrow(WLTags.ENCHANTABLE_MASK),
                        4,
                        1,
                        Enchantment.dynamicCost(10, 10),
                        Enchantment.dynamicCost(40, 10),
                        4,
                        EquipmentSlotGroup.HEAD
                )).exclusiveWith(enchantments.getOrThrow(WLTags.MASK_EXCLUSIVE))
        );
    }

    public static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}
