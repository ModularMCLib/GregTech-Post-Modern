package com.modularmc.gtpm.api.placeholder;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public interface IPlaceholderInfoProviderCover {

    long getTicksSincePlaced();

    List<? extends Component> getComputerCraftTextBuffer();

    void setComputerCraftTextBufferLine(int line, MutableComponent component);
}
