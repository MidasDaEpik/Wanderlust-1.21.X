package com.midasdaepik.wanderlust.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.midasdaepik.wanderlust.config.WLCommonConfig;
import com.midasdaepik.wanderlust.registries.WLEnchantmentEffects;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.neoforged.neoforge.common.extensions.IItemStackExtension.class)
public interface IItemStackExtension {
    @Shadow
    private ItemStack self() {
        return null;
    }

    @WrapMethod(method = "canWalkOnPowderedSnow")
    private boolean canWalkOnPowderedSnow(LivingEntity pLivingEntity, Operation<Boolean> pOriginal) {
        RegistryAccess pRegistry = pLivingEntity.level().registryAccess();
        return pOriginal.call(pLivingEntity) ||
                (pRegistry.holder(Enchantments.FROST_WALKER).map(pHolder -> self().getEnchantmentLevel(pHolder) > 0).orElse(false) && WLCommonConfig.CONFIG.EnchantmentFrostWalkerWalkOnPowderSnow.get()) ||
                EnchantmentHelper.has(self(), WLEnchantmentEffects.WALKABLE_POWDER_SNOW.get());
    }
}