package com.fastclient.management.user;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.fastclient.Fast;
import com.fastclient.management.websocket.packet.impl.SC_FastUserPacket;
import com.fastclient.utils.TimerUtils;
import com.fastclient.utils.server.ServerUtils;

import net.minecraft.client.MinecraftClient;

public class UserManager {

	private final Cache<String, Boolean> cache = Caffeine.newBuilder().maximumSize(1000).build();
	private final Set<String> requests = new HashSet<>();
	private final TimerUtils timer = new TimerUtils();

	public UserManager() {
	}

	public void update() {

		Iterator<String> iterator = requests.iterator();

		if (ServerUtils.isMultiplayer()) {
			if (timer.delay(100)) {
				if (iterator.hasNext()) {
					String request = iterator.next();
					Fast.getInstance().getWebSocketManager().send(new SC_FastUserPacket(request));
					requests.remove(request);
				}

				timer.reset();
			}
			
		} else {
			timer.reset();
		}
		
		// Auto-register local player as FastClient user
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null) {
			String localUuid = client.player.getUuid().toString().replace("-", "");
			if (cache.getIfPresent(localUuid) == null) {
				cache.put(localUuid, true);
			}
		}
	}

	public void add(String uuid, boolean isUser) {
		cache.put(uuid, isUser);
	}

	/**
	 * Check if a player is a FastClient user.
	 * If not cached, queues a request to the server.
	 * @param uuid Player UUID (without dashes)
	 * @return true if FastClient user, false if not or not yet known
	 */
	public boolean checkFastUser(String uuid) {
		Boolean cached = cache.getIfPresent(uuid);
		if (cached == null) {
			// Not in cache - queue a request and return false for now
			if (!requests.contains(uuid)) {
				requests.add(uuid);
			}
			return false;
		}
		return cached;
	}

	public boolean isFastUser(String uuid) {
		Boolean result = cache.getIfPresent(uuid);
		return result != null && result;
	}

	public void clear() {
		requests.clear();
	}
}
