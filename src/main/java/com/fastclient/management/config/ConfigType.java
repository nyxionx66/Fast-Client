package com.fastclient.management.config;

public enum ConfigType {
	MOD("mod");

	private final String id;

	private ConfigType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public static ConfigType get(String id) {

		for (ConfigType t : ConfigType.values()) {
			if (t.getId().equals(id)) {
				return t;
			}
		}

		return null;
	}
}
