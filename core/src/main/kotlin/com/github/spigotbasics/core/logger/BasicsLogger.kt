package com.github.spigotbasics.core.logger

import java.util.logging.Level
import java.util.logging.Logger

class BasicsLogger(
    private val logger: Logger,
    prefix: String,
) {
    private val prefix = "[$prefix] "

    companion object {
        val debugLogLevel = getDefaultDebugLogLevel()

        private fun getDefaultDebugLogLevel(): Int {
            val debugLevel = System.getenv("BASICS_DEBUG_LEVEL") ?: return 0
            return debugLevel.toIntOrNull() ?: 0
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
            logger.info("[DEBUG $level] " + prefix + message)
        }
    }
}
