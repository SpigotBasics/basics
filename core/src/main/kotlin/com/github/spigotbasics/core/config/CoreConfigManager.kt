package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.SafeResourceGetter
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.TagResolverFactory
import com.github.spigotbasics.core.module.InvalidModuleException
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.util.logging.Level

class CoreConfigManager(
    private val plugin: BasicsPlugin,
    private val messageFactory: MessageFactory
    ) {

    companion object {
        private val logger = BasicsLoggerFactory.getCoreLogger(CoreConfigManager::class)
    }

    private fun <T : SavedConfig> createInstance(clazz: Class<T>, plugin: BasicsPlugin, file: File): T {
        try {
            return clazz.getConstructor(BasicsPlugin::class.java, File::class.java).newInstance(plugin, file)
        } catch(e: NoSuchMethodException) {
            throw RuntimeException("Failed to create config instance of class ${clazz.name}, no visible constructor with (BasicsPlugin, File) found", e)
        } catch (e: Exception) {
            throw RuntimeException("Failed to create config instance of class ${clazz.name}", e)
        }
    }

    fun getConfig (resourceFileName: String, fileName: String, clazzToGetFrom: Class<*>): SavedConfig {
        return getConfig(resourceFileName, fileName, clazzToGetFrom, SavedConfig::class.java)
    }

    fun <T: SavedConfig> getConfig(resourceFileName: String, fileName: String, clazzToGetFrom: Class<*>, configurationClass: Class<T>): T {
        val configName = fileName
        val file = File(plugin.dataFolder, configName)

        val configuration = createInstance(configurationClass, plugin, file)

        try {
            // If a default config exists, set it as defaults
            SafeResourceGetter.getResourceAsStream(clazzToGetFrom, resourceFileName)?.use {
                configuration.setDefaults(YamlConfiguration.loadConfiguration(it.bufferedReader()))
            }
        } catch (e: InvalidConfigurationException) {
            throw InvalidModuleException("Module contains invalid default config: $resourceFileName", e)
        }

        // If the file does not exist, save the included default config if it exists
        if (!file.exists()) {
            //logger.info("Saving default config file $configName to ${file.absolutePath}")
            SafeResourceGetter.getResourceAsStream(clazzToGetFrom, resourceFileName)?.copyTo(FileOutputStream(file))
        }

        // Load the config from disk if file exists
        try {
            if (file.exists()) {
                configuration.load(file)
            }
        } catch (e: InvalidConfigurationException) {
            logger.log(Level.SEVERE, "Failed to load invalid config file $configName", e)
        }

        return configuration
    }
}