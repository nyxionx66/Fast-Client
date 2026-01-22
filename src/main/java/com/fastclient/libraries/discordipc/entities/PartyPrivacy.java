
package com.fastclient.libraries.discordipc.entities;

/**
 * Constants representing various Discord client party privacy levels,
 * such as Public or Private
 */
public enum PartyPrivacy {
    /**
     * Constant for the "Private" Discord RPC Party privacy level.
     */
    Private,

    /**
     * Constant for the "Public" Discord RPC Party privacy level.
     */
    Public;

    /**
     * Gets a {@link PartyPrivacy} matching the specified index.
     * <p>
     * This is only internally implemented.
     *
     * @param index The index to get from.
     * @return The {@link PartyPrivacy} corresponding to the parameters, or
     * {@link PartyPrivacy#Public} if none match.
     */
    public static PartyPrivacy from(int index) {
        for (PartyPrivacy value : values()) {
            if (value.ordinal() == index) {
                return value;
            }
        }
        return Public;
    }
}
