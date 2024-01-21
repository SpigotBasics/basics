package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsLoggerFactory
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger

abstract class AbstractBasicsModule(context: ModuleInstantiationContext) : BasicsModule {

    /**
     * Module class loader
     */
    final override val moduleClassLoader = context.classLoader

    /**
     * Module file
     */
    final override val moduleFile = context.file

    /**
     * Module info
     */
    final override val info: ModuleInfo = context.info

    /**
     * Logger for this module
     */
    final override val logger: Logger = BasicsLoggerFactory.getModuleLogger(this)

    /**
     * Plugin instance
     */
    final override val plugin: BasicsPlugin = context.plugin

    /**
     * Commands Manager
     */
    final override val commandManager: BasicsCommandManager = context.plugin.createCommandManager()

    /**
     * Config
     */
    override var config = getConfig("config.yml")

    override fun reloadConfig() {
        config = getConfig("config.yml")
    }

    /**
     * Scheduler
     */
    override val scheduler = BasicsScheduler(plugin)

    /**
     * Adventure Audience
     */
    override val audience = plugin.audience

    fun getResource(path: String): URL? {
        val actualPath = toAbsoluteResourcePath(path)
        return javaClass.getResource(actualPath)
    }

    fun getResourceAsStream(path: String): InputStream? {
        val actualPath = toAbsoluteResourcePath(path)
        return javaClass.getResourceAsStream(actualPath)
    }

    private fun toAbsoluteResourcePath(path: String): String {
        return if (path.substring(0, 1) == "/") path else "/$path"
    }

    /**
     * Get a [SavedConfig] for a certain file name. This will load the default file from the plugin's resources
     * if it exists. If the file does not exist but the default config exists, the file will be created and the default
     * config will be copied to it.
     * If a default config does not exist, no file will be saved.
     *
     * @param sourceName Name of the source file
     * @return Configuration file
     */
    fun getConfig(sourceName: String): SavedConfig {

        // The file object will use the namespaced resource name
        val configName = getNamespacedResourceName(sourceName)
        val file = File(plugin.dataFolder, configName)

        val configuration = SavedConfig(file)

        try {
            // If a default config exists, set it as defaults
            getResourceAsStream(sourceName)?.use {
                configuration.setDefaults(YamlConfiguration.loadConfiguration(it.bufferedReader()))
            }
        } catch (e: InvalidConfigurationException) {
            throw InvalidModuleException("Module contains invalid default config: $sourceName", e)
        }

        // If the file does not exist, save the included default config if it exists
        if (!file.exists()) {
            getResourceAsStream(sourceName)?.copyTo(FileOutputStream(file))
        }

        // Load the config from disk if file exists
        try {
            if (file.exists()) {
                configuration.load(file)
            }
        } catch (e: InvalidConfigurationException) {
            logger.log(Level.WARNING, "Failed to load invalid config file $configName", e)
        }

        return configuration
    }

    /**
     * Get namespaced resource name. For `config.yml` this will simply be `<module-name>.yml`, for all other files it will be `<module-name>-<file-name>`.
     *
     * @param path Path to the resource
     * @return Namespaced resource name
     */
    private fun getNamespacedResourceName(path: String): String {
        var newPath = path

        // Remove leading slash
        if (path.startsWith("/")) {
            newPath = path.substring(1)
        }

        // config.yml is called <module-name>.yml
        if (newPath == "config.yml") {
            return "${info.name}.yml"
        }

        // All other files are called <module-name>-<file-name>
        return "${info.name}-$newPath"
    }

    private var isEnabled = false
    final override fun enable() {
        isEnabled = true
    }

    final override fun disable() {
        scheduler.killAll()
        commandManager.unregisterAll()
        isEnabled = false
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }

}
