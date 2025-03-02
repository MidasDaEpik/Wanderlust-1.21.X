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
        WLTags.initTags();

        eventBus.addListener(WLPacketHandler::registerNetworking);

        WLArmorMaterials.register(eventBus);
        WLAttachmentTypes.register(eventBus);
        WLCreativeTabs.register(eventBus);
        WLDataComponents.register(eventBus);
        WLEffects.register(eventBus);
        WLEntities.register(eventBus);
        WLGlobalLootModifers.register(eventBus);
        WLItems.register(eventBus);
        WLSounds.register(eventBus);

        eventBus.addListener(FMLClientSetupEvent.class, (fmlClientSetupEvent -> {
            fmlClientSetupEvent.enqueueWork(() -> {
                ModList.get().getModContainerById(MOD_ID).ifPresent(modContainer -> {
                    LOGGER.info("Running Wanderlust {}", modContainer.getModInfo().getVersion());
                });
            });
        }));
    }
}
