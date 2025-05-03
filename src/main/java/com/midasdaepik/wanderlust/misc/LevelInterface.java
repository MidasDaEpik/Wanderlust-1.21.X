package com.midasdaepik.wanderlust.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.joml.Vector4d;

import javax.annotation.Nullable;

public interface LevelInterface {

    Explosion wanderlust_1_21_X$explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction explosionInteraction, boolean spawnParticles, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound, float pEntityExplosionRadius, float pEntityDamageCap, boolean pDamageItems);

    Explosion wanderlust_1_21_X$explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction explosionInteraction, boolean spawnParticles, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound, TagKey<Block> pDamageableBlocks, Vector4d pDamageableBlockMinMaxLimit, Vector4d pNonDamageableBlockMinMaxLimit);

    Explosion wanderlust_1_21_X$explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction explosionInteraction, boolean spawnParticles, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound, float pEntityExplosionRadius, float pEntityDamageCap, boolean pDamageItems, TagKey<Block> pDamageableBlocks, Vector4d pDamageableBlockMinMaxLimit, Vector4d pNonDamageableBlockMinMaxLimit);

}