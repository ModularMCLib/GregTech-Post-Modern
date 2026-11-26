package com.modularmc.gtpm.client.bloom.particle;

import com.modularmc.gtpm.client.bloom.BloomHandler;
import com.modularmc.gtpm.client.bloom.IBloomEffect;
import com.modularmc.gtpm.client.bloom.IRenderSetup;
import com.modularmc.gtpm.client.particle.GTParticle;

import org.jetbrains.annotations.Nullable;

public abstract class GTBloomParticle extends GTParticle implements IBloomEffect {

    public GTBloomParticle(double posX, double posY, double posZ) {
        super(posX, posY, posZ);
        BloomHandler.registerBloomRender(getBloomRenderSetup(), this, this);
    }

    protected abstract @Nullable IRenderSetup getBloomRenderSetup();
}
