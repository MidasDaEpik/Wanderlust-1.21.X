package com.midasdaepik.wanderlust.event;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = Wanderlust.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onSetup(FMLCommonSetupEvent pEvent) {
        if (WLTweaksConfig.CONFIG.HasteMiningFatigueTweak.get()) {
            MobEffects.DIG_SPEED.value().addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.withDefaultNamespace("effect.haste"), 0.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            MobEffects.DIG_SLOWDOWN.value().addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.withDefaultNamespace("effect.mining_fatigue"), 0.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }

        if (WLTweaksConfig.CONFIG.StrengthWeaknessRebalance.get()) {
            MobEffects.DAMAGE_BOOST.value().addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.withDefaultNamespace("effect.strength"), WLTweaksConfig.CONFIG.StrengthPercentModifier.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            MobEffects.WEAKNESS.value().addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.withDefaultNamespace("effect.weakness"), WLTweaksConfig.CONFIG.WeaknessPercentModifier.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }

        CauldronInteraction.WATER.map().put(WLItems.ELDER_CHESTPLATE.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.map().put(WLItems.PHANTOM_BOOTS.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.map().put(WLItems.PHANTOM_CLOAK.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.map().put(WLItems.PHANTOM_HOOD.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.map().put(WLItems.PHANTOM_LEGGINGS.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.map().put(WLItems.PHANTOM_TUNIC.get(), CauldronInteraction.DYED_ITEM);
    }

    @SubscribeEvent
    public static void onBuildCreativeModeTabContentsEvent(BuildCreativeModeTabContentsEvent pEvent) {
        ResourceKey<CreativeModeTab> pTab = pEvent.getTabKey();
        if (pTab == CreativeModeTabs.INGREDIENTS) {
            registerTabAdditionAfter(pEvent, WLItems.ELDER_SPINE.get(), Items.PRISMARINE_CRYSTALS);
            registerTabAdditionAfter(pEvent, WLItems.DRAGONBONE.get(), Items.ENDER_EYE);

            registerTabAdditionAfter(pEvent, WLItems.ECHO_GEM.get(), Items.ECHO_SHARD);

            registerTabAdditionAfter(pEvent, WLItems.WITHERBLADE_UPGRADE_SMITHING_TEMPLATE.get(), Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);

            registerTabAdditionAfter(pEvent, WLItems.ATROPHY_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE);
            registerTabAdditionAfter(pEvent, WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);

            //registerTabAdditionAfter(pEvent, WLItems.HALO_ARMOR_EFFECT_SMITHING_TEMPLATE.get(), Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE);

            registerTabAdditionBefore(pEvent, WLItems.ANCIENT_TABLET_IMBUEMENT.get(), Items.EXPERIENCE_BOTTLE);
            registerTabAdditionAfter(pEvent, WLItems.ANCIENT_TABLET_REINFORCEMENT.get(), WLItems.ANCIENT_TABLET_IMBUEMENT);
        }
    }

    public static void registerTabAdditionAfter(BuildCreativeModeTabContentsEvent pEvent, ItemLike pAddition, ItemLike pAfter) {
        pEvent.insertAfter(new ItemStack(pAfter), new ItemStack(pAddition), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
    }

    public static void registerTabAdditionBefore(BuildCreativeModeTabContentsEvent pEvent, ItemLike pAddition, ItemLike pBefore) {
        pEvent.insertBefore(new ItemStack(pBefore), new ItemStack(pAddition), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
    }
}
