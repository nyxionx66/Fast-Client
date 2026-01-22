package com.fastclient.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FastLogger {

	private static final Logger logger = LogManager.getLogger("Fast Client");

	public static void info(String prefix, String message) {
		logger.info("[SC/INFO] [" + prefix + "] " + message);
	}

	public static void warn(String prefix, String message) {
		logger.warn("[SC/WARN] [" + prefix + "] " + message);
	}

	public static void error(String prefix, String message) {
		logger.error("[SC/ERROR] [" + prefix + "] " + message);
	}

	public static void error(String prefix, String message, Exception e) {
		logger.error("[SC/ERROR] [" + prefix + "] " + message, e);
	}

	public static Logger getLogger() {
		return logger;
	}
}