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

public class WLArmorMaterials {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, MOD_ID);

    public static final Holder<ArmorMaterial> ELDER_ARMOR_MATERIAL =
            ARMOR_MATERIAL.register("elder", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                        map.put(ArmorItem.Type.BODY, 5);
                    }),
                    14,
                    SoundEvents.ARMOR_EQUIP_TURTLE,
                    () -> Ingredient.of(Items.PRISMARINE_CRYSTALS),
                    List.of(
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MOD_ID, "elder")),
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MOD_ID, "elder"), "_overlay", true)
                    ),
                    1.5f,
                    0f
            ));

    public static final Holder<ArmorMaterial> MASK_MATERIAL =
            ARMOR_MATERIAL.register("mask", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 1);
                        map.put(ArmorItem.Type.LEGGINGS, 2);
                        map.put(ArmorItem.Type.CHESTPLATE, 3);
                        map.put(ArmorItem.Type.HELMET, 1);
                        map.put(ArmorItem.Type.BODY, 3);
                    }),
                    15,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(Items.STRING),
                    List.of(
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MOD_ID, "mask"))
                    ),
                    0f,
                    0f
            ));

    public static final Holder<ArmorMaterial> PHANTOM_MATERIAL =
            ARMOR_MATERIAL.register("phantom", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                        map.put(ArmorItem.Type.BODY, 7);
                    }),
                    18,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(Items.PHANTOM_MEMBRANE),
                    List.of(
                            new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MOD_ID, "phantom"))
                    ),
                    1f,
                    0f
            ));

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIAL.register(eventBus);
    }
}
