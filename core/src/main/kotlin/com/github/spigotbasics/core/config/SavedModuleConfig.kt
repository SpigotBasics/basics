package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.BasicsLoggerFactory
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.Reader
import java.util.logging.Level

/**
 * Represents a [YamlConfiguration] that is backed by a file. Instances of this class should only be obtained using
 * [com.github.spigotbasics.core.module.AbstractBasicsModule.getConfig].
 * It is fine to keep this object around and pass it around, it will automatically get updated
 * on module reload instead of replaced.
 *
 * @property file File backing this configuration
 * @constructor Create empty Saved config
 */
class SavedModuleConfig internal constructor(
    val module: BasicsModule,
    /**
     * File backing this configuration.
     */
    val file: File
) : YamlConfiguration() {

    companion object {
        private val logger = BasicsLoggerFactory.getCoreLogger(SavedModuleConfig::class)

        // TODO: Remove. SavedModuleConfigs should not be arbitrary created, as their purpose is to be backed
        // by a module-namespaced filename and to also include the module's default config for that file!
//        /**
//         * Creates a new [SavedModuleConfig] from the given file.
//         *
//         * @param file File to load from
//         * @return New [SavedModuleConfig] instance
//         */
//        fun fromFile(file: File): SavedModuleConfig {
//            val config = SavedModuleConfig(file)
//            config.load(file)
//            return config
//        }
    }

    /**
     * Saves this configuration to the file.
     */
    fun save() {
        save(file)
    }

    override fun load(file: String) {
        try {
            super.load(file)
        } catch (e: InvalidConfigurationException) {
            logger.log(Level.SEVERE, "Failed to load config file $file", e)
        }
    }

    override fun load(file: File) {
        try {
            super.load(file)
        } catch (e: InvalidConfigurationException) {
            logger.log(Level.SEVERE, "Failed to load config file $file", e)
        }
    }

    override fun load(reader: Reader) {
        try {
            super.load(reader)
        } catch (e: InvalidConfigurationException) {
            logger.log(Level.SEVERE, "Failed to load config from reader", e)
        }
    }

    override fun loadFromString(contents: String) {
        try {
            super.loadFromString(contents)
        } catch (e: InvalidConfigurationException) {
            logger.log(Level.SEVERE, "Failed to load config from string", e)
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
            return Message(tagResolverFactory = module.tagResolverFactory,
                lines = getStringList(path))
        } else if (isString(path)) {
            return Message(
                tagResolverFactory = module.tagResolverFactory,
                line = getString(path)!!)
        } else {
            return Message.DISABLED
        }
    }

}
