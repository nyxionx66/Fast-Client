package com.fastclient.management.websocket.handler.impl;

import com.google.gson.JsonObject;
import com.fastclient.Fast;
import com.fastclient.management.user.UserManager;
import com.fastclient.management.websocket.handler.WebSocketHandler;
import com.fastclient.utils.JsonUtils;

public class FastUserHandler extends WebSocketHandler {

	@Override
	public void handle(JsonObject jsonObject) {
		
		UserManager userManager = Fast.getInstance().getUserManager();
		
		String uuid = JsonUtils.getStringProperty(jsonObject, "uuid", "null");
		boolean isUser = JsonUtils.getBooleanProperty(jsonObject, "fastUser", false);
		
		if(!uuid.equals("null")) {
			userManager.add(uuid, isUser);
		}
	}
}
