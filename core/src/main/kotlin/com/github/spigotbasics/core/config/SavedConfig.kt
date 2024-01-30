package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Represents a [YamlConfiguration] that is backed by a file. Instances of this class should only be obtained using
 * [com.github.spigotbasics.core.module.AbstractBasicsModule.getConfig].
 * It is fine to keep this object around and pass it around, it will automatically get updated
 * on module reload instead of replaced.
 *
 * @property file File backing this configuration
 * @constructor Create empty Saved config
 */
open class SavedConfig internal constructor(

    /**
     * File backing this configuration.
     */
    private val plugin: BasicsPlugin,
    val file: File
    //val tagResolverFactory: TagResolverFactory
    //val messageFactory: MessageFactory
) : YamlConfiguration() {

    private val logger = BasicsLoggerFactory.getConfigLogger(file)
    private val messageFactory: MessageFactory = plugin.messageFactory

    /**
     * Saves this configuration to the file.
     */
    fun save() {
        save(file)
    }

    override fun load(file: File) {
        try {
            super.load(file)
        } catch (e: InvalidConfigurationException) {
            logger.log(Level.SEVERE, "Failed to load config file $file", e)
        }
    }

    fun reload() {
        if(file.isFile) {
            load(file)
        }
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
//            return Message(tagResolverFactory = tagResolverFactory,
//                lines = getStringList(path))
            return messageFactory.createMessage(getStringList(path))
        } else if (isString(path)) {
//            return Message(
//                tagResolverFactory = tagResolverFactory,
//                line = getString(path)!!)
            return messageFactory.createMessage(getString(path)!!)
        } else {
            return messageFactory.createMessage(emptyList())
        }
    }

}
