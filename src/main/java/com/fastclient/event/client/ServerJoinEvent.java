package com.fastclient.event.client;

import com.fastclient.event.Event;

public class ServerJoinEvent extends Event {

    private final String address;

    public ServerJoinEvent(final String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }
}
