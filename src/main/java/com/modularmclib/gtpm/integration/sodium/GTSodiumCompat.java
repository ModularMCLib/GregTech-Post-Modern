package com.modularmclib.gtpm.integration.sodium;

import com.modularmclib.gtpm.client.renderer.GTRenderTypes;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.parameters.AlphaCutoffParameter;

public class GTSodiumCompat {

    public static final TerrainRenderPass BLOOM_RENDER_PASS = new TerrainRenderPass(GTRenderTypes.bloom(), false, true);
    public static final Material BLOOM_MATERIAL = new Material(BLOOM_RENDER_PASS, AlphaCutoffParameter.ONE_TENTH, true);
}
