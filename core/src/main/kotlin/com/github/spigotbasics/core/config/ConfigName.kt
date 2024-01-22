package com.github.spigotbasics.core.config

/**
 * Represents a config name
 *
 * @property path Path to the config
 */
class ConfigName(val path: String) {

    companion object {
        /**
         * Default config name
         */
        val CONFIG = ConfigName("config.yml")

        /**
         * Messages config name
         */
        val MESSAGES = ConfigName("messages.yml")
    }
}