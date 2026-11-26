package com.modularmclib.gtpm.core.mixins.client;

import com.modularmclib.gtpm.core.util.extensions.VertexConsumerExt;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin extends VertexConsumerExt {}
