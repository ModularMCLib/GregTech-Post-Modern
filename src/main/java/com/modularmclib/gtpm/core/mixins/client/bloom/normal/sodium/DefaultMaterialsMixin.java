package com.modularmclib.gtpm.core.mixins.client.bloom.normal.sodium;

import com.modularmclib.gtpm.client.bloom.BloomShaderManager;
import com.modularmclib.gtpm.client.renderer.GTRenderTypes;
import com.modularmclib.gtpm.integration.sodium.GTSodiumCompat;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.renderer.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DefaultMaterials.class, remap = false)
public class DefaultMaterialsMixin {

    @Inject(method = "forRenderLayer", at = @At("HEAD"), cancellable = true)
    private static void gtceu$mapBloomLayer(RenderType renderType, CallbackInfoReturnable<Material> cir) {
        if (!BloomShaderManager.isBloomAvailable()) return;

        if (renderType == GTRenderTypes.bloom()) {
            cir.setReturnValue(GTSodiumCompat.BLOOM_MATERIAL);
        }
    }
}
