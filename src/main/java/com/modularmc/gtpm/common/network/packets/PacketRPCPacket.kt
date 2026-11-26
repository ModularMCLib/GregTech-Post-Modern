package com.modularmc.gtpm.common.network.packets

import com.modularmc.gtpm.GTPM
import com.modularmc.gtpm.api.sync_system.rpc.RPCPacketDistributor
import com.modularmc.gtpm.api.sync_system.rpc.RPCSender

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

/** Bidirectional static-method transport matching LDLib2's packet ID and encoded arguments. */
class PacketRPCPacket(private val packetID: String, private val data: ByteArray) : CustomPacketPayload {
	private constructor(buffer: RegistryFriendlyByteBuf) : this(buffer.readUtf(), buffer.readByteArray())

	private fun encode(buffer: RegistryFriendlyByteBuf) {
		buffer.writeUtf(packetID)
		buffer.writeByteArray(data)
	}

	fun execute(context: IPayloadContext) {
		val player = context.player()
		RPCPacketDistributor.handle(packetID, if (player is ServerPlayer) RPCSender.ofRemote(player) else RPCSender.ofServer(), data)
	}

	override fun type(): Type<PacketRPCPacket> = TYPE

	companion object {
		@JvmField val TYPE = Type<PacketRPCPacket>(GTPM.id("rpc_packet"))

		@JvmField val CODEC: StreamCodec<RegistryFriendlyByteBuf, PacketRPCPacket> = StreamCodec.ofMember(PacketRPCPacket::encode, ::PacketRPCPacket)
	}
}
