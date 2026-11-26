package com.modularmclib.gtpm.integration.map.layer;

import com.modularmclib.gtpm.integration.map.GenericMapRenderer;

public abstract class MapRenderLayer {

    protected final String key;
    protected final GenericMapRenderer renderer;

    public MapRenderLayer(String key, GenericMapRenderer renderer) {
        this.key = key;
        this.renderer = renderer;
    }
}
