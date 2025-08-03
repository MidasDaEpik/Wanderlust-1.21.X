package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.particle.PyroBarrierOptions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WLParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Wanderlust.MOD_ID);

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
