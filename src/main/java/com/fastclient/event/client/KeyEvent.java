package com.fastclient.event.client;

import com.fastclient.event.Event;

public class KeyEvent extends Event {

	private final int keyCode;

	public KeyEvent(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}
}
