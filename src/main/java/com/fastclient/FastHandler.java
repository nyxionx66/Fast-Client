package com.fastclient;

import java.util.List;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.ClientTickEvent;
import com.fastclient.event.client.ServerJoinEvent;
import com.fastclient.event.server.impl.GameJoinEvent;
import com.fastclient.management.profile.Profile;

public class FastHandler {

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		Fast.getInstance().getColorManager().onTick();
		Fast.getInstance().getHypixelManager().update();
		Fast.getInstance().getUserManager().update();
	};

	public final EventBus.EventListener<GameJoinEvent> onGameJoin = event -> {
		Fast.getInstance().getHypixelManager().clear();
		Fast.getInstance().getUserManager().clear();
	};

	public final EventBus.EventListener<ServerJoinEvent> onServerJoin = event -> {

		List<Profile> profiles = Fast.getInstance().getProfileManager().getProfiles();

		for (Profile p : profiles) {

			String address = p.getServerIp();

			if (event.getAddress().contains(address)) {
				Fast.getInstance().getProfileManager().load(p);
				break;
			}
		}
	};
}
