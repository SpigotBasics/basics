package com.github.spigotbasics.core.logger

import java.util.logging.Level
import java.util.logging.Logger

class BasicsLogger(private val logger: Logger, prefix: String) {

    private val prefix = "[$prefix] "
    fun info(message: String) {
        logger.info(prefix+message)
    }

    fun warning(message: String) {
        logger.warning(prefix+message)
    }

    fun severe(message: String) {
        logger.severe(prefix+message)
    }

    fun log(level: Level, message: String, throwable: Throwable) {
        logger.log(level, prefix+message, throwable)
    }
}