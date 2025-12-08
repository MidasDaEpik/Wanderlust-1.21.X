package com.midasdaepik.wanderlust.datagen;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;

public class WLItemModelProvider extends ItemModelProvider {
    static String MOD_ID = Wanderlust.MOD_ID;

    public WLItemModelProvider(PackOutput pOutput, ExistingFileHelper pExistingFileHelper) {
        super(pOutput, MOD_ID, pExistingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(WLItems.ANCIENT_TABLET_FUSION.get());
        basicItem(WLItems.ANCIENT_TABLET_IMBUEMENT.get());
        basicItem(WLItems.ANCIENT_TABLET_REINFORCEMENT.get());
        basicItem(WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get());
        handheldItem(WLItems.CUTLASS.get());
        handheldItem(WLItems.DAGGER.get());
        basicItem(WLItems.DRAGONBONE.get());
        basicItem(WLItems.ECHO_GEM.get());
        handheldItem(WLItems.ECHO_TUNER.get());
        dyableTrimmedArmorItem(WLItems.ELDER_CHESTPLATE);
        basicItem(WLItems.ELDER_SPINE.get());
        handheldItem(WLItems.FANGS_OF_FROST.get());
        handheldItem(WLItems.FIRESTORM_KATANA.get());
        basicItem(WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE.get());
        basicItem(WLItems.MOD_ICON.get());
        handheldItem(WLItems.MOLTEN_PICKAXE.get());
        handheldItem(WLItems.MYCORIS.get());
        basicItem(WLItems.PHANTOM_CLOAK.get());
        basicItem(WLItems.PHANTOM_HOOD.get());
        handheldItem(WLItems.TOME_OF_EVOCATION.get());
        multiLayeredhandheldItem(WLItems.SEARING_STAFF.get(), "_orb");
        handheldItem(WLItems.KERIS.get());
        basicItem(WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());
        handheldItem(WLItems.WARPED_RAPIER.get());
        handheldItem(WLItems.WITHERBLADE.get());
        basicItem(WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE.get());
    }

    public void multiLayeredhandheldItem(Item pItem, String pLayerSuffix) {
        String pItemPath = pItem.toString();
        ResourceLocation pItemResLoc = ResourceLocation.parse(pItemPath);

        getBuilder(pItemPath)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(MOD_ID, "item/" + pItemResLoc.getPath()))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(MOD_ID, "item/" + pItemResLoc.getPath() + pLayerSuffix));
    }

    private static final LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    //Referenced from Kaupenjoe's Tutorials
    public void trimmedArmorItem(DeferredItem<ArmorItem> pItem) {
        if (pItem.get() instanceof ArmorItem pArmorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (pArmorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = pArmorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(pItem.getId().getPath(), mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace()  + ":item/" + trimNameResLoc.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0", ResourceLocation.fromNamespaceAndPath(MOD_ID, "item/" + pItem.getId().getPath()));
            });
        }
    }

    public void dyableTrimmedArmorItem(DeferredItem<ArmorItem> pItem) {
        if(pItem.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = armorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath() + "_overlay")
                        .texture("layer2", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(pItem.getId().getPath(), mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace()  + ":item/" + trimNameResLoc.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0", ResourceLocation.fromNamespaceAndPath(MOD_ID, "item/" + pItem.getId().getPath()))
                        .texture("layer1", ResourceLocation.fromNamespaceAndPath(MOD_ID, "item/" + pItem.getId().getPath() + "_overlay"));
            });
        }
    }
}
