package com.modularmclib.gtpm.api.data.chemical.material.properties;

public class WoodProperty implements IMaterialProperty {

    @Override
    public void verifyProperty(MaterialProperties properties) {
        properties.ensureSet(PropertyKey.DUST, true);
    }
}
