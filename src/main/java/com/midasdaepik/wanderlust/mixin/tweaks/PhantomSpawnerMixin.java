package com.midasdaepik.wanderlust.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.config.WLTweaksConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static com.midasdaepik.wanderlust.registries.WLAttachmentTypes.INSOMNIA;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    @Shadow
    private int nextTick;

    @WrapMethod(method = "tick")
    private int tick(ServerLevel pServerLevel, boolean pSpawnEnemies, boolean pSpawnFriendlies, Operation<Integer> pOriginal) {
        if (WLTweaksConfig.CONFIG.PhantomSpawningReworkToggle.get()) {
            if (!pSpawnEnemies) {
                return 0;
            } else if (!pServerLevel.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)) {
                return 0;
            } else {
                RandomSource pRandomsource = pServerLevel.random;
                this.nextTick--;
                if (this.nextTick > 0) {
                    return 0;
                } else {
                    this.nextTick = this.nextTick + (60 + pRandomsource.nextInt(60)) * 20;
                    if (pServerLevel.getSkyDarken() < 5 && pServerLevel.dimensionType().hasSkyLight()) {
                        return 0;
                    } else {
                        int pPhantomSpawnCount = 0;
                        for (ServerPlayer pServerPlayer : pServerLevel.players()) {
                            if (!pServerPlayer.isSpectator()) {
                                BlockPos pBlockpos = pServerPlayer.blockPosition();
                                var pEvent = net.neoforged.neoforge.event.EventHooks.firePlayerSpawnPhantoms(pServerPlayer, pServerLevel, pBlockpos);
                                boolean pForceSpawn = pEvent.getResult() == net.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent.Result.ALLOW;

                                if (pEvent.shouldSpawnPhantoms(pServerLevel, pBlockpos)) {
                                    int pInsomnia = pServerPlayer.getData(INSOMNIA) / 24000 - 8;
                                    if (pForceSpawn || (pInsomnia > 0 && pRandomsource.nextInt(2) == 0)) {
                                        BlockPos pBlockPosSky = pBlockpos.above(20 + pRandomsource.nextInt(15))
                                                .east(-10 + pRandomsource.nextInt(21))
                                                .south(-10 + pRandomsource.nextInt(21));
                                        BlockState pBlockstate = pServerLevel.getBlockState(pBlockPosSky);
                                        FluidState pFluidstate = pServerLevel.getFluidState(pBlockPosSky);
                                        if (NaturalSpawner.isValidEmptySpawnBlock(pServerLevel, pBlockPosSky, pBlockstate, pFluidstate, EntityType.PHANTOM)) {
                                            SpawnGroupData pSpawngroupdata = null;
                                            int pPhantomCount = pEvent.getPhantomsToSpawn() + pInsomnia / 4;
                                            
                                            for (int i = 0; i < pPhantomCount; i++) {
                                                Phantom pPhantom = EntityType.PHANTOM.create(pServerLevel);
                                                if (pPhantom != null) {
                                                    pPhantom.moveTo(pBlockPosSky, 0.0F, 0.0F);
                                                    pSpawngroupdata = pPhantom.finalizeSpawn(pServerLevel, pServerLevel.getCurrentDifficultyAt(pBlockpos), MobSpawnType.NATURAL, pSpawngroupdata);
                                                    if (i + 1 <= pPhantomCount / 5.0) {
                                                        pPhantom.setPhantomSize(2);
                                                    } else if (i + 1 < pPhantomCount / 2.0) {
                                                        pPhantom.setPhantomSize(1);
                                                    }
                                                    pServerLevel.addFreshEntityWithPassengers(pPhantom);
                                                    pPhantomSpawnCount++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return pPhantomSpawnCount;
                    }
                }
            }
        } else {
            return pOriginal.call(pServerLevel, pSpawnEnemies, pSpawnFriendlies);
        }
    }
}