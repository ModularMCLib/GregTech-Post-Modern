package com.modularmc.gtpm.data.lang;

import com.modularmc.gtpm.api.data.chemical.material.Material;

public class MaterialLangGenerator {

    /** Resolves the material name shared by the material and tag language generators. */
    public static String getEnglishName(Material material) {
        return material.getDefaultTranslation();
    }
}
