package com.midasdaepik.wanderlust.event;

import com.midasdaepik.wanderlust.Wanderlust;
import com.midasdaepik.wanderlust.client.model.ElderChestplateModel;
import com.midasdaepik.wanderlust.client.model.ElderChestplateRetractedModel;
import com.midasdaepik.wanderlust.client.renderer.entity.DragonsBreathRenderer;
import com.midasdaepik.wanderlust.client.renderer.entity.DragonsRageBreathRenderer;
import com.midasdaepik.wanderlust.client.renderer.entity.FirestormRenderer;
import com.midasdaepik.wanderlust.client.renderer.hud.WeaponAbilityHudOverlay;
import com.midasdaepik.wanderlust.particle.PyroBarrier;
import com.midasdaepik.wanderlust.registries.WLEntities;
import com.midasdaepik.wanderlust.registries.WLItemProperties;
import com.midasdaepik.wanderlust.registries.WLItems;
import com.midasdaepik.wanderlust.registries.WLParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.Collections;
import java.util.Map;

@EventBusSubscriber(modid = Wanderlust.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    private static void clientSetup(FMLClientSetupEvent pEvent) {
        EntityRenderers.register(WLEntities.NO_DAMAGE_FIREBALL.get(), ThrownItemRenderer::new);

        WLItemProperties.addCustomItemProperties();
    }

    @SubscribeEvent
    private static void entityRenderers(EntityRenderersEvent.RegisterRenderers pEvent) {
        pEvent.registerEntityRenderer(WLEntities.DRAGONS_BREATH.get(), DragonsBreathRenderer::new);
        pEvent.registerEntityRenderer(WLEntities.DRAGONS_RAGE_BREATH.get(), DragonsRageBreathRenderer::new);
        pEvent.registerEntityRenderer(WLEntities.FIRESTORM.get(), FirestormRenderer::new);
    }

    @SubscribeEvent
    private static void particleProviders(RegisterParticleProvidersEvent pEvent) {
        pEvent.registerSpriteSet(WLParticles.PYRO_BARRIER.get(), PyroBarrier.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterGuiLayersEvent(RegisterGuiLayersEvent pEvent) {
        pEvent.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "weapon_ability_hud_overlay"), new WeaponAbilityHudOverlay(Minecraft.getInstance()));
    }

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions pEvent) {
        pEvent.registerLayerDefinition(ElderChestplateModel.LAYER_LOCATION, ElderChestplateModel::createBodyLayer);
        pEvent.registerLayerDefinition(ElderChestplateRetractedModel.LAYER_LOCATION, ElderChestplateRetractedModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterColorHandlersEventItem(RegisterColorHandlersEvent.Item pEvent) {
        pEvent.register(
                (pItemStack, pTintIndex) -> pTintIndex == 1 ? DyedItemColor.getOrDefault(pItemStack, 6448520) : -1,
                WLItems.ELDER_CHESTPLATE
        );
    }

    @SubscribeEvent
    public static void onRegisterClientExtensionsEvent(RegisterClientExtensionsEvent pEvent) {
        pEvent.registerItem(
                new IClientItemExtensions() {
                    public int getDefaultDyeColor(ItemStack pItemStack) {
                        return pItemStack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(pItemStack, 6448520)) : -1;
                    }

                    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity pLivingEntity, ItemStack pItemStack, EquipmentSlot pEquipmentSlot, HumanoidModel<?> pDefaultModel) {
                        if (pLivingEntity instanceof Player pPlayer && pPlayer.getCooldowns().isOnCooldown(pItemStack.getItem())) {
                            HumanoidModel pArmorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of(
                                    "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "body", new ElderChestplateRetractedModel(Minecraft.getInstance().getEntityModels().bakeLayer(ElderChestplateRetractedModel.LAYER_LOCATION)).Body,
                                    "right_arm", new ElderChestplateRetractedModel(Minecraft.getInstance().getEntityModels().bakeLayer(ElderChestplateRetractedModel.LAYER_LOCATION)).RightArm,
                                    "left_arm", new ElderChestplateRetractedModel(Minecraft.getInstance().getEntityModels().bakeLayer(ElderChestplateRetractedModel.LAYER_LOCATION)).LeftArm,
                                    "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap())
                            )));
                            pArmorModel.crouching = pDefaultModel.crouching;
                            pArmorModel.riding = pDefaultModel.riding;
                            pArmorModel.swimAmount = pDefaultModel.swimAmount;
                            pArmorModel.young = pDefaultModel.young;
                            return pArmorModel;
                        } else {
                            HumanoidModel pArmorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of(
                                    "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "body", new ElderChestplateModel(Minecraft.getInstance().getEntityModels().bakeLayer(ElderChestplateModel.LAYER_LOCATION)).Body,
                                    "right_arm", new ElderChestplateModel(Minecraft.getInstance().getEntityModels().bakeLayer(ElderChestplateModel.LAYER_LOCATION)).RightArm,
                                    "left_arm", new ElderChestplateModel(Minecraft.getInstance().getEntityModels().bakeLayer(ElderChestplateModel.LAYER_LOCATION)).LeftArm,
                                    "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap())
                            )));
                            pArmorModel.crouching = pDefaultModel.crouching;
                            pArmorModel.riding = pDefaultModel.riding;
                            pArmorModel.swimAmount = pDefaultModel.swimAmount;
                            pArmorModel.young = pDefaultModel.young;
                            return pArmorModel;
                        }
                    }
                },
                WLItems.ELDER_CHESTPLATE.get()
        );
    }
}
