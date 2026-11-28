package com.modularmc.gtpm.api.data.chemical;

import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmc.gtpm.api.registry.registrate.GTRegistryEntryBuilder;
import com.modularmc.gtpm.utils.FormattingUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jspecify.annotations.Nullable;

/**
 * This is some kind of Periodic Table, which can be used to determine "Properties" of the Materials.
 */
@Accessors(fluent = true, chain = false)
public class Element {

    public static final class Builder {

        private final GTRegistrate registrate;
        private final String name;
        private long protons;
        private long neutrons;
        private long halfLifeSeconds = -1;
        private @Nullable String decayTo;
        private boolean isotope;

        public Builder(GTRegistrate registrate, String name) {
            this.registrate = registrate;
            this.name = name;
        }

        public Builder nucleus(long protons, long neutrons) {
            this.protons = protons;
            this.neutrons = neutrons;
            return this;
        }

        public Builder decay(long halfLifeSeconds, @Nullable String target) {
            this.halfLifeSeconds = halfLifeSeconds;
            this.decayTo = target;
            return this;
        }

        public Builder isotope() {
            this.isotope = true;
            return this;
        }

        public GTRegistryEntryBuilder<Element> symbol(String symbol) {
            String path = FormattingUtil.hasUpperCase(name) ? FormattingUtil.toLowerCaseUnderscore(name) : name;
            return registrate.registryEntry(path, GTRegistries.ELEMENTS)
                    .definition(new Element(protons, neutrons, halfLifeSeconds, decayTo, name, symbol, isotope));
        }
    }

    /**
     * Amount of Protons
     */
    @Getter
    @Setter
    private long protons;
    /**
     * Amount of Neutrons
     */
    @Getter
    @Setter
    private long neutrons;
    /**
     * Amount of Half Life this Material has in Seconds. -1 for stable Materials
     */
    @Getter
    @Setter
    private long halfLifeSeconds;
    /**
     * String representing the Elements this element decays to. Separated by an '&' Character
     */
    @Getter
    @Setter
    private @Nullable String decayTo;
    /**
     * Name of the Element
     */
    @Getter
    @Setter
    private String name;
    /**
     * Symbol of the Element
     */
    @Getter
    @Setter
    private String symbol;
    /**
     * Is this element an isotope?
     */
    @Getter
    @Setter
    private boolean isIsotope;

    public long mass() {
        return protons + neutrons;
    }

    public Element(long protons, long neutrons, long halfLifeSeconds, @Nullable String decayTo, String name,
                   String symbol,
                   boolean isIsotope) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.halfLifeSeconds = halfLifeSeconds;
        this.decayTo = decayTo;
        this.name = name;
        this.symbol = symbol;
        this.isIsotope = isIsotope;
    }
}
