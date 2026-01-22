package com.fastclient.ui.component.handler.impl;

import com.fastclient.libraries.material3.hct.Hct;
import com.fastclient.ui.component.handler.ComponentHandler;

public abstract class HctColorPickerHandler extends ComponentHandler {
	public abstract void onPicking(Hct hct);
}