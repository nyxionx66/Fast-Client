package com.fastclient.management.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.fastclient.logger.FastLogger;
import com.fastclient.management.websocket.handler.WebSocketHandler;
import com.fastclient.management.websocket.handler.impl.HypixelStatsHandler;
import com.fastclient.management.websocket.handler.impl.FastUserHandler;
import com.fastclient.utils.JsonUtils;

public class FastWebSocketClient extends WebSocketClient {

	private final Map<String, WebSocketHandler> handlers = new HashMap<>();
	private final Gson gson = new Gson();
	private final Runnable closeTask;

	public FastWebSocketClient(Map<String, String> headers, Runnable closeTask) throws URISyntaxException {
		super(new URI("ws://localhost:8080/websocket"), headers);
		this.closeTask = closeTask;
		initializeHandlers();
	}

    private void initializeHandlers() {
    	register("sc-hypixel-stats", new HypixelStatsHandler());
    	register("sc-fast-user", new FastUserHandler());
    }
    
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		FastLogger.info("API", "WebSocket connection opened");
	}

	@Override
	public void onMessage(String message) {

		JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
		
		String type = JsonUtils.getStringProperty(jsonObject, "type", "");

		WebSocketHandler handler = handlers.get(type);

		if (handler != null) {
			handler.handle(jsonObject);
		} else {
			FastLogger.warn("API", "No handler found for message type: " + type);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		FastLogger.info("API", "WebSocket connection closed: " + reason);
		closeTask.run();
	}

	@Override
	public void onError(Exception ex) {
		FastLogger.error("API", "WebSocket error occurred", ex);
	}

	private void register(String type, WebSocketHandler handler) {
		handlers.put(type, handler);
	}
}