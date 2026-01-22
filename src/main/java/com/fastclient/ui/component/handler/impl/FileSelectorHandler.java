package com.fastclient.ui.component.handler.impl;

import java.io.File;

import com.fastclient.ui.component.handler.ComponentHandler;

public abstract class FileSelectorHandler extends ComponentHandler {
	public abstract void onSelect(File file);
}
