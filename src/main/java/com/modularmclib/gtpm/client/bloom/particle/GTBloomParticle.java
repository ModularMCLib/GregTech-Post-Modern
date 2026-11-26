package com.modularmclib.gtpm.client.bloom.particle;

import com.modularmclib.gtpm.client.bloom.BloomHandler;
import com.modularmclib.gtpm.client.bloom.IBloomEffect;
import com.modularmclib.gtpm.client.bloom.IRenderSetup;
import com.modularmclib.gtpm.client.particle.GTParticle;

import org.jetbrains.annotations.Nullable;

public abstract class GTBloomParticle extends GTParticle implements IBloomEffect {

    public GTBloomParticle(double posX, double posY, double posZ) {
        super(posX, posY, posZ);
        BloomHandler.registerBloomRender(getBloomRenderSetup(), this, this);
    }

    protected abstract @Nullable IRenderSetup getBloomRenderSetup();
}
