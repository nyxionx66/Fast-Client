package com.fastclient.management.websocket.packet;

import com.google.gson.JsonObject;

public abstract class FastPacket {

	protected final JsonObject jsonObject = new JsonObject();
	private final String type;

	public FastPacket(String type) {
		this.type = type;
		this.jsonObject.addProperty("type", type);
	}

	public String getType() {
		return type;
	}

	public abstract JsonObject toJson();
}
