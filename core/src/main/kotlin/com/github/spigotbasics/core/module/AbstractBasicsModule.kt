package com.github.spigotbasics.core.module

import cloud.commandframework.bukkit.BukkitCommandManager
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.config.SavedConfig
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.util.logging.Logger

abstract class AbstractBasicsModule(
    final override val plugin: BasicsPlugin,
    final override val info: ModuleInfo
) :
    BasicsModule {

    /**
     * Logger for this module
     */
    override val logger: Logger = Logger.getLogger("basics.${info.name}")

    /**
     * Shared command manager
     */
    override val commandManager: BukkitCommandManager<CommandSender> = plugin.commandManager
    override val config = getConfig("config.yml");

    fun getResource(path: String): URL {
        val actualPath = toAbsoluteResourcePath(path)
        return javaClass.getResource(actualPath) ?: error("Resource $actualPath not found")
    }

    fun getResourceAsStream(path: String): InputStream {
        val actualPath = toAbsoluteResourcePath(path)
        return javaClass.getResourceAsStream(actualPath) ?: error("Resource $actualPath not found")
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

        // If a default config exists, set it as defaults
        getResourceAsStream(sourceName).use {
            configuration.setDefaults(YamlConfiguration.loadConfiguration(it.bufferedReader()))
        }

        // If the file does not exist, save the included default config
        if (!file.exists()) {
            file.createNewFile()
            getResourceAsStream(sourceName).copyTo(FileOutputStream(file))
        }

        // Load the config from disk
        configuration.load(file)
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

    override fun enable() {}

    override fun disable() {}

    override fun load() {}
}
