package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class RRArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, Wanderlust.MOD_ID);

    public static final Holder<ArmorMaterial> ELDER_ARMOR_MATERIAL =
            ARMOR_MATERIAL.register("elder", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                        map.put(ArmorItem.Type.BODY, 5);
                    }),
                    13,
                    SoundEvents.ARMOR_EQUIP_TURTLE,
                    () -> Ingredient.of(Items.PRISMARINE_CRYSTALS),
                    List.of(
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "elder")),
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "elder"), "_overlay", true)
                    ),
                    1.5f,
                    0f
            ));

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIAL.register(eventBus);
    }
}
