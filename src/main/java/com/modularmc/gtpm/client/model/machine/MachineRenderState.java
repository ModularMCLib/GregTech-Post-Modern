package com.modularmc.gtpm.client.model.machine;

import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sync_system.FieldCodecs;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;

public class MachineRenderState extends StateHolder<MachineDefinition, MachineRenderState> {

    public static final Codec<MachineRenderState> CODEC = codec(GTRegistries.MACHINES.byNameCodec(),
            MachineDefinition::defaultRenderState).stable();

    static {
        FieldCodecs.register(MachineRenderState.class, CODEC);
    }

    public MachineRenderState(MachineDefinition owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values,
                              MapCodec<MachineRenderState> propertiesCodec) {
        super(owner, values, propertiesCodec);
    }

    public MachineDefinition getDefinition() {
        return this.owner;
    }

    public boolean is(MetaMachine machine) {
        return this.is(machine.getDefinition());
    }

    public boolean is(MachineDefinition definition) {
        return this.getDefinition() == definition;
    }
}
