package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class WLDamageSource {
    static String MOD_ID = Wanderlust.MOD_ID;

    public static final ResourceKey<DamageType> BURN =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "burn"));

    public static final ResourceKey<DamageType> BURN_NO_COOLDOWN =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "burn_no_cooldown"));

    public static final ResourceKey<DamageType> ECHO =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "echo"));

    public static final ResourceKey<DamageType> MAGIC =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "magic"));

    public static final ResourceKey<DamageType> SONIC_BOOM =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "sonic_boom"));

    public static final ResourceKey<DamageType> WHIRLPOOL =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "whirlpool"));

    public static DamageSource damageSource(Level pLevel, ResourceKey<DamageType> pDamageType) {
        return new DamageSource(pLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(pDamageType));
    }

    public static DamageSource damageSource(Level pLevel, Entity pSource, ResourceKey<DamageType> pDamageType) {
        return new DamageSource(pLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(pDamageType), pSource);
    }
}
