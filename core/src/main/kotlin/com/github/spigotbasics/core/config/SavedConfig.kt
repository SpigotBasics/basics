package com.github.spigotbasics.core.config

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * Represents a [YamlConfiguration] that is backed by a file.
 *
 * @property file File backing this configuration
 * @constructor Create empty Saved config
 */
class SavedConfig(private val file: File) : YamlConfiguration() {

    companion object {
        /**
         * Creates a new [SavedConfig] from the given file.
         *
         * @param file File to load from
         * @return New [SavedConfig] instance
         */
        fun load(file: File): SavedConfig {
            val config = SavedConfig(file)
            config.load(file)
            return config
        }
    }

    fun save() {
        save(file)
    }

}
