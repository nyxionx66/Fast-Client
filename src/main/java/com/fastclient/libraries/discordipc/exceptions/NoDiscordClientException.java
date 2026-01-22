
package com.fastclient.libraries.discordipc.exceptions;

import com.fastclient.libraries.discordipc.IPCClient;
import com.fastclient.libraries.discordipc.entities.DiscordBuild;

/**
 * An exception thrown when the {@link IPCClient IPCClient}
 * cannot find the proper application to use for RichPresence when
 * attempting to {@link IPCClient#connect(DiscordBuild...) connect}.
 * <p>
 * This purely and always means the IPCClient in question (specifically the client ID)
 * is <i>invalid</i> and features using this library cannot be accessed using the instance.
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class NoDiscordClientException extends Exception {
    /**
     * The serialized unique version identifier
     */
    private static final long serialVersionUID = 1L;

    public NoDiscordClientException() {
        super("No Valid Discord Client was found for this Instance");
    }
}
