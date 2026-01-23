package com.fastclient.management.presence;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.fastclient.Fast;
import com.fastclient.logger.FastLogger;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * Manages detection of other FastClient users on the same server.
 * Uses custom payload packets to broadcast and detect presence.
 */
public class PresenceManager {

    private static final Set<UUID> fastClientUsers = ConcurrentHashMap.newKeySet();
    private static boolean initialized = false;

    /**
     * Custom payload for FastClient presence detection
     */
    public record FastClientPresencePayload(String version, UUID senderUuid) implements CustomPayload {
        
        public static final CustomPayload.Id<FastClientPresencePayload> ID = 
            new CustomPayload.Id<>(FastClientChannel.PRESENCE_CHANNEL);
        
        public static final PacketCodec<PacketByteBuf, FastClientPresencePayload> CODEC = 
            PacketCodec.of(FastClientPresencePayload::write, FastClientPresencePayload::read);
        
        private static FastClientPresencePayload read(PacketByteBuf buf) {
            String version = buf.readString();
            UUID uuid = buf.readUuid();
            return new FastClientPresencePayload(version, uuid);
        }
        
        private void write(PacketByteBuf buf) {
            buf.writeString(version);
            buf.writeUuid(senderUuid);
        }
        
        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /**
     * Initialize the presence system - register payload types and handlers
     */
    public static void init() {
        if (initialized) return;
        
        try {
            // Register payload type for sending (C2S)
            PayloadTypeRegistry.playC2S().register(FastClientPresencePayload.ID, FastClientPresencePayload.CODEC);
            
            // Register payload type for receiving (S2C)
            PayloadTypeRegistry.playS2C().register(FastClientPresencePayload.ID, FastClientPresencePayload.CODEC);
            
            // Register receiver handler
            ClientPlayNetworking.registerGlobalReceiver(FastClientPresencePayload.ID, (payload, context) -> {
                context.client().execute(() -> {
                    handlePresencePacket(payload.senderUuid(), payload.version());
                });
            });

            // Register connection events
            ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
                // Small delay to ensure connection is fully established
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        broadcastPresence();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            });

            ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
                clear();
            });
            
            initialized = true;
            FastLogger.info("Presence", "FastClient presence system initialized");
        } catch (Exception e) {
            FastLogger.error("Presence", "Failed to initialize presence system: " + e.getMessage());
        }
    }

    /**
     * Broadcast our presence to the server (which forwards to other players)
     */
    public static void broadcastPresence() {
        try {
            if (!ClientPlayNetworking.canSend(FastClientPresencePayload.ID)) {
                return; // Server doesn't support our channel
            }
            
            UUID ourUuid = net.minecraft.client.MinecraftClient.getInstance().getSession().getUuidOrNull();
            if (ourUuid == null) return;
            
            FastClientPresencePayload payload = new FastClientPresencePayload(
                Fast.getInstance().getVersion(),
                ourUuid
            );
            
            ClientPlayNetworking.send(payload);
            FastLogger.info("Presence", "Broadcasted FastClient presence");
        } catch (Exception e) {
            // Silently fail - some servers block custom packets
        }
    }

    /**
     * Handle incoming presence packet from another FastClient user
     */
    private static void handlePresencePacket(UUID senderUuid, String version) {
        if (senderUuid == null) return;
        
        // Don't add ourselves
        UUID ourUuid = net.minecraft.client.MinecraftClient.getInstance().getSession().getUuidOrNull();
        if (ourUuid != null && ourUuid.equals(senderUuid)) return;
        
        if (fastClientUsers.add(senderUuid)) {
            FastLogger.info("Presence", "Detected FastClient user: " + senderUuid + " (v" + version + ")");
            
            // Send back our presence so they know about us too
            broadcastPresence();
        }
    }

    /**
     * Check if a player is using FastClient
     */
    public static boolean isFastClientUser(UUID uuid) {
        return uuid != null && fastClientUsers.contains(uuid);
    }

    /**
     * Clear all tracked users (call on disconnect)
     */
    public static void clear() {
        fastClientUsers.clear();
        FastLogger.info("Presence", "Cleared FastClient user list");
    }

    /**
     * Remove a specific user (call when player leaves)
     */
    public static void removeUser(UUID uuid) {
        if (uuid != null) {
            fastClientUsers.remove(uuid);
        }
    }

    /**
     * Get count of detected FastClient users
     */
    public static int getUserCount() {
        return fastClientUsers.size();
    }
}
