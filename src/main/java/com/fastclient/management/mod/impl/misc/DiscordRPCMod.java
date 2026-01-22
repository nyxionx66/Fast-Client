package com.fastclient.management.mod.impl.misc;

import java.time.OffsetDateTime;

import com.google.gson.JsonObject;
import com.fastclient.Fast;
import com.fastclient.libraries.discordipc.IPCClient;
import com.fastclient.libraries.discordipc.IPCListener;
import com.fastclient.libraries.discordipc.entities.ActivityType;
import com.fastclient.libraries.discordipc.entities.Packet;
import com.fastclient.libraries.discordipc.entities.RichPresence;
import com.fastclient.libraries.discordipc.entities.User;
import com.fastclient.libraries.discordipc.entities.pipe.PipeStatus;
import com.fastclient.libraries.discordipc.exceptions.NoDiscordClientException;
import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.skia.font.Icon;

public class DiscordRPCMod extends Mod {

	private IPCClient client;

	public DiscordRPCMod() {
		super("mod.discordrpc.name", "mod.discordrpc.description", Icon.VERIFIED, ModCategory.MISC);
	}

	@Override
	public void onEnable() {
		super.onEnable();

		client = new IPCClient(1462797910554054767L);
		client.setListener(new IPCListener() {
			@Override
			public void onReady(IPCClient client) {

				RichPresence.Builder builder = new RichPresence.Builder();

				builder.setState("Playing Fast Client v" + Fast.getInstance().getVersion())
						.setStartTimestamp(OffsetDateTime.now().toEpochSecond()).setLargeImage("icon")
						.setActivityType(ActivityType.Playing);

				client.sendRichPresence(builder.build());
			}

			@Override
			public void onPacketSent(IPCClient client, Packet packet) {
			}

			@Override
			public void onPacketReceived(IPCClient client, Packet packet) {
			}

			@Override
			public void onActivityJoin(IPCClient client, String secret) {
			}

			@Override
			public void onActivitySpectate(IPCClient client, String secret) {
			}

			@Override
			public void onActivityJoinRequest(IPCClient client, String secret, User user) {
			}

			@Override
			public void onClose(IPCClient client, JsonObject json) {
			}

			@Override
			public void onDisconnect(IPCClient client, Throwable t) {
			}
		});

		try {
			client.connect();
		} catch (NoDiscordClientException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();

		if (client != null && client.getStatus() == PipeStatus.CONNECTED) {
			client.close();
			client = null;
		}
	}
}
