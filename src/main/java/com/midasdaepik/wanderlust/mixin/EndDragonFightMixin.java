package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.registries.WLItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndDragonFight.class)
public class EndDragonFightMixin {
    @WrapMethod(method = "setDragonKilled")
    private void spawnBossLoot(EnderDragon pDragon, Operation<Void> pOriginal) {
        Level pLevel = pDragon.level();
        Vec3 pPodium = pLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(pDragon.getFightOrigin())).getCenter();

        if (pLevel instanceof ServerLevel pServerLevel) {
            pServerLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pPodium.x, pPodium.y + 4, pPodium.z, 32, 0.5, 0.5, 0.5, 0.5);
        }

        ItemEntity pDragonbone = new ItemEntity(pLevel, pPodium.x, pPodium.y + 4, pPodium.z, WLItems.DRAGONBONE.toStack(Mth.nextInt(RandomSource.create(), 1, 2)));
        pDragonbone.setDeltaMovement(0.0, 0.0, 0.0);
        pDragonbone.setGlowingTag(true);
        pDragonbone.setNoGravity(true);
        pDragonbone.setUnlimitedLifetime();
        pLevel.addFreshEntity(pDragonbone);

        if (RandomSource.create().nextFloat() < 0.33f) {
            ItemEntity pTyrantTrim = new ItemEntity(pLevel, pPodium.x, pPodium.y + 6, pPodium.z, WLItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.toStack(Mth.nextInt(RandomSource.create(), 1, 2)));
            pTyrantTrim.setDeltaMovement(0.0, 0.0, 0.0);
            pTyrantTrim.setGlowingTag(true);
            pTyrantTrim.setNoGravity(true);
            pTyrantTrim.setUnlimitedLifetime();
            pLevel.addFreshEntity(pTyrantTrim);
        }
        pOriginal.call(pDragon);
    }
}