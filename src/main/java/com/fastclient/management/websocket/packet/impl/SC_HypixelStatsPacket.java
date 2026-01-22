package com.fastclient.management.websocket.packet.impl;

import com.google.gson.JsonObject;
import com.fastclient.management.websocket.packet.FastPacket;

public class SC_HypixelStatsPacket extends FastPacket {

	private final String uuid;
	
	public SC_HypixelStatsPacket(String uuid) {
		super("sc-hypixel-stats");
		this.uuid = uuid;
	}

	@Override
	public JsonObject toJson() {
		jsonObject.addProperty("uuid", uuid);
		return jsonObject;
	}
}
