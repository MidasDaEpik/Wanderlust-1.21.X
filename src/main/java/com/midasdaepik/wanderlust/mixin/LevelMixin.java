package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.misc.ExplosionInterface;
import com.midasdaepik.wanderlust.misc.LevelInterface;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Vector4d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(Level.class)
public class LevelMixin implements LevelInterface {

    @Shadow
    private Explosion.BlockInteraction getDestroyType(GameRules.Key<GameRules.BooleanValue> gameRule) {
        return null;
    }

    @Override
    public Explosion wanderlust_1_21_X$explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction explosionInteraction, boolean spawnParticles, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound, float pEntityExplosionRadius, float pEntityDamageCap, boolean pDamageItems) {
        return wanderlust_1_21_X$explode(source, damageSource, damageCalculator, x, y, z, radius, fire, explosionInteraction, spawnParticles, smallExplosionParticles, largeExplosionParticles, explosionSound, pEntityExplosionRadius, pEntityDamageCap, pDamageItems, null, null, null);
    }

    @Override
    public Explosion wanderlust_1_21_X$explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction explosionInteraction, boolean spawnParticles, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound, TagKey<Block> pDamageableBlocks, Vector4d pDamageableBlockMinMaxLimitMultiplier, Vector4d pNonDamageableBlockMinMaxLimitMultiplier) {
        return wanderlust_1_21_X$explode(source, damageSource, damageCalculator, x, y, z, radius, fire, explosionInteraction, spawnParticles, smallExplosionParticles, largeExplosionParticles, explosionSound, -1f, -1f, true, pDamageableBlocks, pDamageableBlockMinMaxLimitMultiplier, pNonDamageableBlockMinMaxLimitMultiplier);
    }

    @Override
    public Explosion wanderlust_1_21_X$explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction explosionInteraction, boolean spawnParticles, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound, float pEntityExplosionRadius, float pEntityDamageCap, boolean pDamageItems, TagKey<Block> pDamageableBlocks, Vector4d pDamageableBlockMinMaxLimitMultiplier, Vector4d pNonDamageableBlockMinMaxLimitMultiplier) {
        Level pThis = (Level) (Object) this;

        Explosion.BlockInteraction var10000;
        switch (explosionInteraction.ordinal()) {
            case 0 -> var10000 = Explosion.BlockInteraction.KEEP;
            case 1 -> var10000 = this.getDestroyType(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
            case 2 -> var10000 = EventHooks.canEntityGrief(pThis, source) ? this.getDestroyType(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
            case 3 -> var10000 = this.getDestroyType(GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
            case 4 -> var10000 = Explosion.BlockInteraction.TRIGGER_BLOCK;
            default -> throw new MatchException(null, null);
        }

        Explosion.BlockInteraction explosion$blockinteraction = var10000;
        Explosion explosion = new Explosion(pThis, source, damageSource, damageCalculator, x, y, z, radius, fire, explosion$blockinteraction, smallExplosionParticles, largeExplosionParticles, explosionSound);

        if (explosion != null) {
            ((ExplosionInterface) explosion).set_wanderlust_1_21_X$updateExplosion(pEntityExplosionRadius, pEntityDamageCap, pDamageItems, pDamageableBlocks, pDamageableBlockMinMaxLimitMultiplier, pNonDamageableBlockMinMaxLimitMultiplier);
        }

        if (!EventHooks.onExplosionStart(pThis, explosion)) {
            explosion.explode();
            explosion.finalizeExplosion(spawnParticles);
        }

        if (pThis instanceof ServerLevel pServerLevel) {
            if (!explosion.interactsWithBlocks()) {
                explosion.clearToBlow();
            }

            for (ServerPlayer serverplayer : pServerLevel.players()) {
                if (serverplayer.distanceToSqr(x, y, z) < 4096.0) {
                    serverplayer.connection.send(
                            new ClientboundExplodePacket(
                                    x,
                                    y,
                                    z,
                                    radius,
                                    explosion.getToBlow(),
                                    explosion.getHitPlayers().get(serverplayer),
                                    explosion.getBlockInteraction(),
                                    explosion.getSmallExplosionParticles(),
                                    explosion.getLargeExplosionParticles(),
                                    explosion.getExplosionSound()
                            )
                    );
                }
            }
        }

        return explosion;
    }
}