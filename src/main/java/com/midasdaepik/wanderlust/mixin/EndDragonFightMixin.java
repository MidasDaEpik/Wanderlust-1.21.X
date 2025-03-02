package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.registries.RRItems;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndDragonFight.class)
public class EndDragonFightMixin {
    @Inject(method = "setDragonKilled", at = @At("HEAD"))
    private void spawnBossLoot(EnderDragon pDragon, CallbackInfo pCallbackInfo) {
        Level pLevel = pDragon.level();
        Vec3 pPodium = pLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(pDragon.getFightOrigin())).getCenter();

        ItemEntity pDragonbone = new ItemEntity(pLevel, pPodium.x, pPodium.y + 4, pPodium.z, RRItems.DRAGONBONE.toStack());
        pDragonbone.setDeltaMovement(0.0, 0.0, 0.0);
        pDragonbone.setGlowingTag(true);
        pDragonbone.setNoGravity(true);
        pDragonbone.setUnlimitedLifetime();
        pLevel.addFreshEntity(pDragonbone);

        if (Mth.nextInt(RandomSource.create(), 1, 3) == 1) {
            ItemEntity pTyrantTrim = new ItemEntity(pLevel, pPodium.x, pPodium.y + 6, pPodium.z, RRItems.TYRANT_ARMOR_TRIM_SMITHING_TEMPLATE.toStack(Mth.nextInt(RandomSource.create(), 1, 2)));
            pTyrantTrim.setDeltaMovement(0.0, 0.0, 0.0);
            pTyrantTrim.setGlowingTag(true);
            pTyrantTrim.setNoGravity(true);
            pTyrantTrim.setUnlimitedLifetime();
            pLevel.addFreshEntity(pTyrantTrim);
        }
    }
}