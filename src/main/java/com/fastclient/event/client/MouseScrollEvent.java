package com.fastclient.event.client;

import com.fastclient.event.Event;

public class MouseScrollEvent extends Event {

	private final double amount;

	public MouseScrollEvent(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}
}
