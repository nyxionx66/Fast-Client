package com.fastclient.management.websocket.packet.impl;

import com.google.gson.JsonObject;
import com.fastclient.management.websocket.packet.FastPacket;

public class SC_FastUserPacket extends FastPacket {

	private final String uuid;
	
	public SC_FastUserPacket(String uuid) {
		super("sc-fast-user");
		this.uuid = uuid;
	}

	@Override
	public JsonObject toJson() {
		jsonObject.addProperty("uuid", uuid);
		return jsonObject;
	}
}
