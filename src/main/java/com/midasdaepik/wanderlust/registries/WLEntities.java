package com.midasdaepik.wanderlust.registries;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class WLEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Wanderlust.MOD_ID);

    public static final Supplier<EntityType<DragonsRageBreath>> DRAGONS_RAGE_BREATH = ENTITY_TYPES.register("dragons_rage_breath",
            () -> EntityType.Builder.<DragonsRageBreath>of(DragonsRageBreath::new, MobCategory.MISC)
                    .sized(1f,1f).clientTrackingRange(4).updateInterval(10).build("dragons_rage_breath"));

    public static final Supplier<EntityType<DragonsBreath>> DRAGONS_BREATH = ENTITY_TYPES.register("dragons_breath",
            () -> EntityType.Builder.<DragonsBreath>of(DragonsBreath::new, MobCategory.MISC)
                    .sized(5f,0.5f).clientTrackingRange(4).updateInterval(10).build("dragons_breath"));

    public static final Supplier<EntityType<Firestorm>> FIRESTORM = ENTITY_TYPES.register("firestorm",
            () -> EntityType.Builder.<Firestorm>of(Firestorm::new, MobCategory.MISC)
                    .sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build("firestorm"));
    public static final Supplier<EntityType<NoDamageFireball>> NO_DAMAGE_FIREBALL = ENTITY_TYPES.register("no_damage_fireball",
            () -> EntityType.Builder.<NoDamageFireball>of(NoDamageFireball::new, MobCategory.MISC)
                    .sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(10).build("no_damage_fireball"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
