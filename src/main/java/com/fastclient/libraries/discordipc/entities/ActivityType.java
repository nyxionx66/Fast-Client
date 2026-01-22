
package com.fastclient.libraries.discordipc.entities;

/**
 * Constants representing various Discord client activity types,
 * such as Playing or Listening
 */
public enum ActivityType {
    /**
     * Constant for the "Playing" Discord RPC Activity type.
     */
    Playing,

    /**
     * Constant for the "Streaming" Discord RPC Activity type.
     */
    Streaming,

    /**
     * Constant for the "Listening" Discord RPC Activity type.
     */
    Listening,

    /**
     * Constant for the "Watching" Discord RPC Activity type.
     */
    Watching,

    /**
     * Constant for the "Custom" Discord RPC Activity type.
     */
    Custom,

    /**
     * Constant for the "Competing" Discord RPC Activity type.
     */
    Competing;

    /**
     * Gets a {@link ActivityType} matching the specified index.
     * <p>
     * This is only internally implemented.
     *
     * @param index The index to get from.
     * @return The {@link ActivityType} corresponding to the parameters, or
     * {@link ActivityType#Playing} if none match.
     */
    public static ActivityType from(int index) {
        for (ActivityType value : values()) {
            if (value.ordinal() == index) {
                return value;
            }
        }
        return Playing;
    }
}
