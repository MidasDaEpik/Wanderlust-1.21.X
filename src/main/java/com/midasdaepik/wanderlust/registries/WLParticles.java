package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.particle.LargeOrientedCircleOptions;
import com.midasdaepik.wanderlust.particle.OrientedCircleOptions;
import com.midasdaepik.wanderlust.particle.PyroBarrierOptions;
import com.midasdaepik.wanderlust.particle.slash.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WLParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Wanderlust.MOD_ID);

    public static final Supplier<ParticleType<CritOptions>> CRIT =
            PARTICLE_TYPES.register("crit", () -> new ParticleType<CritOptions>(false) {
                @Override
                public MapCodec<CritOptions> codec() {
                    return CritOptions.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, CritOptions> streamCodec() {
                    return CritOptions.STREAM_CODEC;
                }
            });

    public static final Supplier<ParticleType<FastSweepOptions>> FAST_SWEEP =
            PARTICLE_TYPES.register("fast_sweep", () -> new ParticleType<FastSweepOptions>(false) {
                @Override
                public MapCodec<FastSweepOptions> codec() {
                    return FastSweepOptions.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, FastSweepOptions> streamCodec() {
                    return FastSweepOptions.STREAM_CODEC;
                }
            });

    public static final Supplier<ParticleType<MediumSweepOptions>> MEDIUM_SWEEP =
            PARTICLE_TYPES.register("medium_sweep", () -> new ParticleType<MediumSweepOptions>(false) {
                @Override
                public MapCodec<MediumSweepOptions> codec() {
                    return MediumSweepOptions.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, MediumSweepOptions> streamCodec() {
                    return MediumSweepOptions.STREAM_CODEC;
                }
            });

    public static final Supplier<ParticleType<SlowSweepOptions>> SLOW_SWEEP =
            PARTICLE_TYPES.register("slow_sweep", () -> new ParticleType<SlowSweepOptions>(false) {
                @Override
                public MapCodec<SlowSweepOptions> codec() {
                    return SlowSweepOptions.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, SlowSweepOptions> streamCodec() {
                    return SlowSweepOptions.STREAM_CODEC;
                }
            });

    public static final Supplier<ParticleType<ThrustOptions>> THRUST =
            PARTICLE_TYPES.register("thrust", () -> new ParticleType<ThrustOptions>(false) {
                @Override
                public MapCodec<ThrustOptions> codec() {
                    return ThrustOptions.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, ThrustOptions> streamCodec() {
                    return ThrustOptions.STREAM_CODEC;
                }
            });

    public static final Supplier<ParticleType<LargeOrientedCircleOptions>> LARGE_ORIENTED_CIRCLE =
            PARTICLE_TYPES.register("large_oriented_circle", () -> new ParticleType<LargeOrientedCircleOptions>(false) {
                @Override
                public MapCodec<LargeOrientedCircleOptions> codec() {
                    return LargeOrientedCircleOptions.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, LargeOrientedCircleOptions> streamCodec() {
                    return LargeOrientedCircleOptions.STREAM_CODEC;
                }
            });

    public static final Supplier<SimpleParticleType> MARK_EFFECT_PARTICLE =
            PARTICLE_TYPES.register("mark_effect_particle", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> MARK_PARTICLE =
            PARTICLE_TYPES.register("mark_particle", () -> new SimpleParticleType(false));

    public static final Supplier<ParticleType<OrientedCircleOptions>> ORIENTED_CIRCLE =
            PARTICLE_TYPES.register("oriented_circle", () -> new ParticleType<OrientedCircleOptions>(false) {
                @Override
                public MapCodec<OrientedCircleOptions> codec() {
                    return OrientedCircleOptions.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, OrientedCircleOptions> streamCodec() {
                    return OrientedCircleOptions.STREAM_CODEC;
                }
            });

    public static final Supplier<ParticleType<PyroBarrierOptions>> PYRO_BARRIER =
            PARTICLE_TYPES.register("pyro_barrier", () -> new ParticleType<PyroBarrierOptions>(false) {
                @Override
                public MapCodec<PyroBarrierOptions> codec() {
                    return PyroBarrierOptions.CODEC;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, PyroBarrierOptions> streamCodec() {
                    return PyroBarrierOptions.STREAM_CODEC;
                }
            });

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
