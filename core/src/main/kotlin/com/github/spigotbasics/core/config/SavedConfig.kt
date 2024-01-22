package com.github.spigotbasics.core.config

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * Represents a [YamlConfiguration] that is backed by a file.
 *
 * @property file File backing this configuration
 * @constructor Create empty Saved config
 */
class SavedConfig(

    /**
     * File backing this configuration.
     */
    val file: File
) : YamlConfiguration() {

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

    /**
     * Saves this configuration to the file.
     */
    fun save() {
        save(file)
    }

    fun getMessage(path: String): Message {
        if(isList(path)) {
            return Message(getStringList(path))
        } else if (isString(path)) {
            return Message(getString(path)!!)
        } else {
            return Message.EMPTY
        }
    }

}
