package com.fastclient.event.server.impl;

import com.fastclient.event.Event;

public class AttackEntityEvent extends Event {

	private final int entityId;

	public AttackEntityEvent(int entityId) {
		this.entityId = entityId;
	}

	public int getEntityId() {
		return entityId;
	}
}
