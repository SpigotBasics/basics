package com.github.spigotbasics.core.logger

import java.util.logging.Level
import java.util.logging.Logger

class BasicsLogger(
    private val logger: Logger,
    prefix: String,
) {
    private val prefix = "[$prefix] "

    companion object {
        private val logger = BasicsLoggerFactory.getCoreLogger(BasicsLogger::class)
        var debugLogLevel = getDefaultDebugLogLevel()

        private fun getDefaultDebugLogLevel(): Int {
            val debugLevel = System.getenv("BASICS_DEBUG_LEVEL") ?: return -1
            val number = debugLevel.toIntOrNull()
            if (number == null) {
                logger.severe("Invalid debug level: $debugLevel (must be a positive integer) - disabling debug logging")
                return -1
            }
            return number
        }
    }

    fun info(message: String) {
        logger.info(prefix + message)
    }

    fun warning(message: String) {
        logger.warning(prefix + message)
    }

    fun severe(message: String) {
        logger.severe(prefix + message)
    }

    fun log(
        level: Level,
        message: String,
        throwable: Throwable,
    ) {
        logger.log(level, prefix + message, throwable)
    }

    fun debug(
        level: Int,
        message: String,
    ) {
        if (debugLogLevel >= level) {
            logger.warning("[DEBUG ${formattedDebugLogLevel(level)}] $prefix$message")
        }
    }

    private fun formattedDebugLogLevel(level: Int): String = if (level > 999) "***" else String.format("%3d", level)
}
