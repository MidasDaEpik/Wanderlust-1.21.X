package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WLItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Wanderlust.MOD_ID);

    public static final DeferredItem<Item> DAGGER = ITEMS.register("dagger",
            () -> new Dagger(new Item.Properties()));

    public static final DeferredItem<Item> CUTLASS = ITEMS.register("cutlass",
            () -> new Cutlass(new Item.Properties()));

    public static final DeferredItem<ArmorItem> PHANTOM_HOOD = ITEMS.register("phantom_hood",
            () -> new PhantomHood(new Item.Properties()));
    public static final DeferredItem<ArmorItem> PHANTOM_CLOAK = ITEMS.register("phantom_cloak",
            () -> new PhantomCloak(new Item.Properties()));

    public static final DeferredItem<Item> FANGS_OF_FROST = ITEMS.register("fangs_of_frost",
            () -> new FangsOfFrost(new Item.Properties()));

    public static final DeferredItem<Item> TAINTED_DAGGER = ITEMS.register("tainted_dagger",
            () -> new TaintedDagger(new Item.Properties()));

    public static final DeferredItem<Item> TOME_OF_EVOCATION = ITEMS.register("tome_of_evocation",
            () -> new TomeOfEvocation(new Item.Properties()));

    public static final DeferredItem<Item> ELDER_SPINE = ITEMS.register("elder_spine",
            () -> new Item(new Item.Properties().rarity(WLEnumExtensions.RARITY_ELDER.getValue())));

    public static final DeferredItem<Item> CHARYBDIS = ITEMS.register("charybdis",
            () -> new Charybdis(new Item.Properties()));
    public static final DeferredItem<ArmorItem> ELDER_CHESTPLATE = ITEMS.register("elder_chestplate",
            () -> new ElderChestplate(new Item.Properties()));

    public static final DeferredItem<Item> WHISPERWIND = ITEMS.register("whisperwind",
            () -> new Whisperwind(new Item.Properties()));

    public static final DeferredItem<Item> PIGLIN_WARAXE = ITEMS.register("piglin_waraxe",
            () -> new PiglinWaraxe(new Item.Properties()));

    public static final DeferredItem<Item> FIRESTORM_KATANA = ITEMS.register("firestorm_katana",
            () -> new FirestormKatana(new Item.Properties()));
    public static final DeferredItem<Item> SEARING_STAFF = ITEMS.register("searing_staff",
            () -> new SearingStaff(new Item.Properties()));
    public static final DeferredItem<Item> MOLTEN_PICKAXE = ITEMS.register("molten_pickaxe",
            () -> new MoltenPickaxe(new Item.Properties()));

    public static final DeferredItem<Item> BLAZE_REAP = ITEMS.register("blaze_reap",
            () -> new BlazeReap(new Item.Properties()));

    public static final DeferredItem<Item> OBSIDIAN_BULWARK = ITEMS.register("obsidian_bulwark",
            () -> new ObsidianBulwark(new Item.Properties()));

    public static final DeferredItem<Item> WARPED_RAPIER = ITEMS.register("warped_rapier",
            () -> new WarpedRapier(new Item.Properties()));

    public static final DeferredItem<Item> WITHERBLADE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("witherblade_upgrade_smithing_template",
            UpgradeTemplateItem::createWitherbladeUpgradeTemplate);

    public static final DeferredItem<Item> ANCIENT_TABLET_FUSION = ITEMS.register("ancient_tablet_fusion",
            AncientKnowledgeItem::createAncientTabletFusion);
    public static final DeferredItem<Item> ANCIENT_TABLET_IMBUEMENT = ITEMS.register("ancient_tablet_imbuement",
            AncientKnowledgeItem::createAncientTabletImbuement);
    public static final DeferredItem<Item> ANCIENT_TABLET_REINFORCEMENT = ITEMS.register("ancient_tablet_reinforcement",
            AncientKnowledgeItem::createAncientTabletReinforcement);

    public static final DeferredItem<Item> ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("atrophy_armor_trim_smithing_template",
            () -> SmithingTemplateItem.createArmorTrimTemplate(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "atrophy")));

    public static final DeferredItem<Item> WITHERBLADE = ITEMS.register("witherblade",
            () -> new Witherblade(new Item.Properties()));
    public static final DeferredItem<Item> REFINED_WITHERBLADE = ITEMS.register("refined_witherblade",
            () -> new RefinedWitherblade(new Item.Properties()));

    public static final DeferredItem<Item> MYCORIS = ITEMS.register("mycoris",
            () -> new Mycoris(new Item.Properties()));
    public static final DeferredItem<Item> PYROSWEEP = ITEMS.register("pyrosweep",
            () -> new Pyrosweep(new Item.Properties()));
    public static final DeferredItem<Item> SOULGORGE = ITEMS.register("soulgorge",
            () -> new Soulgorge(new Item.Properties()));
    public static final DeferredItem<Item> WARPTHISTLE = ITEMS.register("warpthistle",
            () -> new Warpthistle(new Item.Properties()));

    public static final DeferredItem<Item> ECHO_GEM = ITEMS.register("echo_gem",
            () -> new Item(new Item.Properties().rarity(WLEnumExtensions.RARITY_SCULK.getValue())));

    public static final DeferredItem<Item> CATALYST_CHALICE = ITEMS.register("catalyst_chalice",
            () -> new CatalystChalice(new Item.Properties()));
    public static final DeferredItem<Item> CATALYST_CRYSTAL = ITEMS.register("catalyst_crystal",
            () -> new CatalystCrystal(new Item.Properties()));
    public static final DeferredItem<Item> ECHO_TUNER = ITEMS.register("echo_tuner",
            () -> new EchoTuner(new Item.Properties()));
    public static final DeferredItem<Item> LYRE_OF_ECHOES = ITEMS.register("lyre_of_echoes",
            () -> new LyreOfEchoes(new Item.Properties()));

    public static final DeferredItem<Item> TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("tyrant_armor_trim_smithing_template",
            () -> SmithingTemplateItem.createArmorTrimTemplate(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "tyrant")));

    public static final DeferredItem<Item> DRAGONBONE = ITEMS.register("dragonbone",
            () -> new Item(new Item.Properties().rarity(WLEnumExtensions.RARITY_DRAGON.getValue()).fireResistant().component(WLDataComponents.NO_GRAVITY, true)));

    public static final DeferredItem<Item> DRAGONS_RAGE = ITEMS.register("dragons_rage",
            () -> new DragonsRage(new Item.Properties()));
    public static final DeferredItem<Item> DRAGONS_BREATH_ARBALEST = ITEMS.register("dragons_breath_arbalest",
            () -> new DragonsBreathArbalest(new Item.Properties()));

    public static final DeferredItem<Item> HALO_ARMOR_EFFECT_SMITHING_TEMPLATE = ITEMS.register("halo_armor_effect_smithing_template",
            CosmeticTemplateItem::createHaloTemplate);

    public static final DeferredItem<Item> MOD_ICON = ITEMS.register("mod_icon",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
