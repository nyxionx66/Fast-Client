package com.fastclient.management.presence;

import net.minecraft.util.Identifier;

/**
 * Custom payload channel for FastClient presence detection.
 * Used to identify other FastClient users on the same server.
 */
public class FastClientChannel {
    
    public static final Identifier PRESENCE_CHANNEL = Identifier.of("fastclient", "presence");
    
    private FastClientChannel() {
        // Utility class
    }
}
