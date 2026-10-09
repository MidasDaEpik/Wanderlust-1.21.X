package com.midasdaepik.wanderlust.item;

import com.midasdaepik.wanderlust.registries.WLEnumExtensions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AncientKnowledgeItem extends Item {
    private final Integer KnowledgeTypeKey;

    public AncientKnowledgeItem(Properties pProperties, Integer pKnowledgeTypeKey) {
        super(pProperties.fireResistant().stacksTo(1));
        this.KnowledgeTypeKey = pKnowledgeTypeKey;
    }

    public static AncientKnowledgeItem createAncientTabletImbuement() {
        return new AncientKnowledgeItem(new Properties().fireResistant().rarity(WLEnumExtensions.RARITY_BLAZE.getValue()), 1);
    }

    public static AncientKnowledgeItem createAncientTabletReinforcement() {
        return new AncientKnowledgeItem(new Properties().fireResistant().rarity(WLEnumExtensions.RARITY_GOLD.getValue()), 2);
    }

    public static AncientKnowledgeItem createAncientTabletFusion() {
        return new AncientKnowledgeItem(new Properties().fireResistant().rarity(WLEnumExtensions.RARITY_WITHERBLADE.getValue()), 3);
    }
    @Override
    @SuppressWarnings("deprecation")
    public boolean hasCraftingRemainingItem() {
        return true;
    }
    
    @Override
    public void appendHoverText(ItemStack pItemStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.wanderlust.non_consumable_crafting_material"));
        pTooltipComponents.add(Component.empty());
        if (this.KnowledgeTypeKey == 1) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_imbuement.desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_imbuement.desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_imbuement.desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_imbuement.desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_imbuement.desc_5"));

        } else if (this.KnowledgeTypeKey == 2) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_reinforcement.desc_1"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_reinforcement.desc_2"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_reinforcement.desc_3"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_reinforcement.desc_4"));
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_reinforcement.desc_5"));

        } else if (this.KnowledgeTypeKey == 3) {
            pTooltipComponents.add(Component.translatable("item.wanderlust.ancient_tablet_fusion.desc_1"));
        }
        super.appendHoverText(pItemStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
