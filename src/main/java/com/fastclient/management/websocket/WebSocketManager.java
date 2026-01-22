package com.fastclient.management.websocket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.fastclient.management.websocket.client.FastWebSocketClient;
import com.fastclient.management.websocket.packet.FastPacket;
import com.fastclient.utils.HttpUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;

public class WebSocketManager {

    private static final int MAX_RETRY = 3;
    
    private MinecraftClient client = MinecraftClient.getInstance();
    private GameProfile gameProfile;
    private FastWebSocketClient webSocket;
    private int retryCount = 0;
    
    public WebSocketManager() {
        new Thread("Websocket Thread") {
            @Override
            public void run() {
                while (true) {
                	
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                    }
                    
                    GameProfile currentProfile = client.getGameProfile();
                    
                    if (gameProfile != null && currentProfile != null && !gameProfile.equals(currentProfile)) {
                        retryCount = 0;
                    }
                    
                    if ((webSocket == null || !gameProfile.equals(currentProfile) || webSocket.isClosed()) 
                            && retryCount < MAX_RETRY) {
                        
                        gameProfile = currentProfile;
                        
                        if (webSocket != null) {
                            webSocket.close();
                        }
                        
                        JsonObject postObject = new JsonObject();
                        Session session = client.getSession();

                        postObject.addProperty("accessToken", session.getAccessToken());
                        postObject.addProperty("selectedProfile", session.getUuidOrNull().toString().replace("-", ""));
                        postObject.addProperty("serverId", "cbd2c3f65d7ba5cceba0cc9647ff9a85c371f4");
                        HttpUtils.postJson("https://sessionserver.mojang.com/session/minecraft/join", postObject);
                        
                        Map<String, String> headers = new HashMap<>();
                        headers.put("name", gameProfile.getName());
                        headers.put("uuid", gameProfile.getId().toString().replace("-", ""));
                        
                        try {
                            webSocket = new FastWebSocketClient(headers, () -> {
                            	retryCount++;
                            });
                            webSocket.connect();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }
    
    public void send(FastPacket packet) {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(packet.toJson().toString());
        }
    }
}
