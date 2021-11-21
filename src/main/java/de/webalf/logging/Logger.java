package de.webalf.logging;

import java.util.logging.Level;

/**
 * @author Tim Augustin
 * @version 1.0
 * @since 20.04.2020
 */
public class Logger {

	private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Logger.class.getName());

	public static void setLogLevel(Level level) {
		log.setLevel(level);
		Logger.logInfo("Logging level: " + level);
	}

	public static void logInfo(String info) {
		log.log(Level.INFO, info);
	}

	public static void logWarning(String warning) {
		log.log(Level.WARNING, warning);
	}

	public static void logSevere(String errorMessage, Exception e) {
		log.log(Level.SEVERE, errorMessage, e);
	}
}
