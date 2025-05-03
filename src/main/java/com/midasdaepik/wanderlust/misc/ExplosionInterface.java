package com.midasdaepik.wanderlust.misc;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.joml.Vector4d;

public interface ExplosionInterface {

    void set_wanderlust_1_21_X$updateExplosion(float pEntityExplosionRadius, float pEntityDamageCap, boolean pDamageItems, TagKey<Block> pDamageableBlocks, Vector4d pDamageableBlockMinMaxLimitMultiplier, Vector4d pNonDamageableBlockMinMaxLimitMultiplier);

}