package com.midasdaepik.wanderlust.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class Phantasmal extends MobEffect {
    public Phantasmal(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> pEffectCures, MobEffectInstance pEffect) {
        pEffectCures.clear();
    }
}
