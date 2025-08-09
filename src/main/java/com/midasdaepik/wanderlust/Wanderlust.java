package com.midasdaepik.wanderlust;

import com.midasdaepik.wanderlust.config.WLAttributeConfig;
import com.midasdaepik.wanderlust.config.WLClientConfig;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import com.midasdaepik.wanderlust.registries.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Wanderlust.MOD_ID)
public class Wanderlust {
    public static final String MOD_ID = "wanderlust";
    public static final Logger LOGGER = LoggerFactory.getLogger(Wanderlust.class);

    public Wanderlust(IEventBus pEventBus, ModContainer pContainer) {
        pContainer.registerConfig(ModConfig.Type.STARTUP, WLAttributeConfig.CONFIG_SPEC, "wanderlust/attributes.toml");
        pContainer.registerConfig(ModConfig.Type.CLIENT, WLClientConfig.CONFIG_SPEC, "wanderlust/client.toml");
        pContainer.registerConfig(ModConfig.Type.COMMON, WLCommonConfig.CONFIG_SPEC, "wanderlust/common.toml");

        WLTags.initTags();

        pEventBus.addListener(WLPacketHandler::registerNetworking);

        WLArmorMaterials.register(pEventBus);
        WLAttachmentTypes.register(pEventBus);
        WLCreativeTabs.register(pEventBus);
        WLDataComponents.register(pEventBus);
        WLEffects.register(pEventBus);
        WLEntities.register(pEventBus);
        WLGlobalLootModifers.register(pEventBus);
        WLItems.register(pEventBus);
        WLParticles.register(pEventBus);
        WLRecipes.register(pEventBus);
        WLSounds.register(pEventBus);

        pEventBus.addListener(FMLClientSetupEvent.class, (fmlClientSetupEvent -> {
            fmlClientSetupEvent.enqueueWork(() -> {
                ModList.get().getModContainerById(MOD_ID).ifPresent(modContainer -> {
                    LOGGER.info("Running Wanderlust {}", modContainer.getModInfo().getVersion());
                });
            });
        }));
    }
}
