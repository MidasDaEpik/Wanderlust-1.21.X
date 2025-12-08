package com.midasdaepik.wanderlust.effect;

import com.midasdaepik.wanderlust.registries.WLEffects;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class KatanaCombo extends MobEffect {
    public KatanaCombo(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Item pItem = pLivingEntity.getMainHandItem().getItem();
        if (!(pItem == WLItems.FIRESTORM_KATANA.asItem() || pItem == WLItems.MYCORIS.asItem())) {
            pLivingEntity.removeEffect(WLEffects.KATANA_COMBO);
        }
        return true;
    }
}
