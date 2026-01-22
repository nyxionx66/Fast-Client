package com.fastclient.event.client;

import com.fastclient.event.Event;

import net.minecraft.network.packet.Packet;

public class SendPacketEvent extends Event {

	private final Packet<?> packet;

	public SendPacketEvent(Packet<?> packet) {
		this.packet = packet;
	}

	public Packet<?> getPacket() {
		return packet;
	}
}
