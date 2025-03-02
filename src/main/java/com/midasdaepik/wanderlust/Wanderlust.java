package com.midasdaepik.wanderlust;

import com.midasdaepik.wanderlust.registries.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Wanderlust.MOD_ID)
public class Wanderlust {
    public static final String MOD_ID = "wanderlust";
    public static final Logger LOGGER = LoggerFactory.getLogger(Wanderlust.class);

    public Wanderlust(IEventBus eventBus) {
        RRTags.initTags();

        eventBus.addListener(RRPacketHandler::registerNetworking);

        RRArmorMaterials.register(eventBus);
        RRAttachmentTypes.register(eventBus);
        RRCreativeTabs.register(eventBus);
        RRDataComponents.register(eventBus);
        RREffects.register(eventBus);
        RREntities.register(eventBus);
        RRGlobalLootModifers.register(eventBus);
        RRItems.register(eventBus);
        RRSounds.register(eventBus);

        eventBus.addListener(FMLClientSetupEvent.class, (fmlClientSetupEvent -> {
            fmlClientSetupEvent.enqueueWork(() -> {
                ModList.get().getModContainerById(MOD_ID).ifPresent(modContainer -> {
                    LOGGER.info("Running Wanderlust {}", modContainer.getModInfo().getVersion());
                });
            });
        }));
    }
}
