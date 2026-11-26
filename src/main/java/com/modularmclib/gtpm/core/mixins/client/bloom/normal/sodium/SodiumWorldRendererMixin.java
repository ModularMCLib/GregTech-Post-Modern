package com.modularmclib.gtpm.core.mixins.client.bloom.normal.sodium;

import com.modularmclib.gtpm.client.bloom.BloomShaderManager;
import com.modularmclib.gtpm.client.renderer.GTRenderTypes;
import com.modularmclib.gtpm.integration.sodium.GTSodiumCompat;

import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.minecraft.client.renderer.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SodiumWorldRenderer.class, remap = false)
public class SodiumWorldRendererMixin {

    @Shadow
    private RenderSectionManager renderSectionManager;

    @Inject(method = "drawChunkLayer", at = @At("RETURN"))
    private void gtceu$drawBloomChunkLayer(RenderType renderLayer, ChunkRenderMatrices matrices, double x, double y,
                                           double z, CallbackInfo ci) {
        if (!BloomShaderManager.isBloomActive()) return;

        if (renderLayer == GTRenderTypes.bloom()) {
            this.renderSectionManager.renderLayer(matrices, GTSodiumCompat.BLOOM_RENDER_PASS, x, y, z);
        }
    }
}
