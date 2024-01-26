package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.logger.BasicsLoggerFactory

/**
 * Represents a config name
 *
 * @property path Path to the config
 */
class ConfigName private constructor(val path: String){

    companion object {

        private val logger = BasicsLoggerFactory.getCoreLogger(ConfigName::class)

        /**
         * Default config name
         */
        val CONFIG = ConfigName("config.yml")

        /**
         * Messages config name
         */
        val MESSAGES = ConfigName("core-messages.yml")


        fun fromName(name: String): ConfigName {
            when(name) {
                "config", "config.yml" -> {
                    logger.warning("Module tried to get default config using name $name instead of ConfigName.CONFIG")
                    CONFIG
                }
                "messages", "core-messages.yml" -> {
                    logger.warning("Module tried to get default config using name $name instead of ConfigName.MESSAGES")
                    MESSAGES
                }
            }
            return ConfigName(name)
        }
    }
}