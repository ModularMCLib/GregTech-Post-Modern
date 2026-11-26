package com.modularmclib.gtpm.api.data.chemical.material.properties;

@FunctionalInterface
public interface IMaterialProperty {

    void verifyProperty(MaterialProperties properties);
}
