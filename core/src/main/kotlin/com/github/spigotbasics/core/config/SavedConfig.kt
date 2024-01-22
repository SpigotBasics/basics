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

    /**
     * Get a [Message] from the config. Messages can be declared as simple String (one line), a list of Strings (multiple lines) or an empty list (disabled message):
     *
     * <pre>
     * single-line: "<green>This is a single line message"
     * multi-line:
     *  - "<green>This is a multi-line message"
     *  - "<green>With multiple lines"
     *  - "<green>And even more lines"
     * disabled: [] # This message has been disabled
     * </pre>
     *
     * @param path Path to the message
     * @return Message
     */
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
