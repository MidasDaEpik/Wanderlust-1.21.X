package com.midasdaepik.wanderlust.mixin;

import com.midasdaepik.wanderlust.Wanderlust;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeCraftingHolder.class)
public interface RecipeCraftingHolderMixin {
    @Inject(method = "setRecipeUsed(Lnet/minecraft/world/level/Level;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/crafting/RecipeHolder;)Z", at = @At("HEAD"), cancellable = true)
    private void restrictCustomRecipe(Level pLevel, ServerPlayer pPlayer, RecipeHolder<?> pRecipe, CallbackInfoReturnable<Boolean> pReturn) {
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "firestorm_katana"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_imbuement"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "searing_staff"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_imbuement"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "witherblade_upgrade_smithing_template"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_imbuement"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }



        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "obsidian_bulwark"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_reinforcement"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "refined_witherblade"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_reinforcement"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }



        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "mycoris"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_fusion"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "mycoris_reverse"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_fusion"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "soulgorge"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_fusion"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "soulgorge_reverse"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_fusion"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "warpthistle"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_fusion"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
        if (pRecipe.id().equals(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "warpthistle_reverse"))) {
            ServerAdvancementManager pAdvancementManager = pPlayer.server.getAdvancements();
            AdvancementHolder pAdvancementHolder = pAdvancementManager.get(ResourceLocation.fromNamespaceAndPath(Wanderlust.MOD_ID, "nether/ancient_tablet_fusion"));
            if (pAdvancementHolder != null && !pPlayer.getAdvancements().getOrStartProgress(pAdvancementHolder).isDone()) {
                pReturn.setReturnValue(false);
            }
        }
    }
}