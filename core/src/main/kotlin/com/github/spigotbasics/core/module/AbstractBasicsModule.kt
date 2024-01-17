package com.github.spigotbasics.core.module

import cloud.commandframework.bukkit.BukkitCommandManager
import com.github.spigotbasics.core.BasicsPlugin
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStream
import java.net.URL
import java.util.logging.Logger

abstract class AbstractBasicsModule(
    final override val plugin: BasicsPlugin, final override val info: ModuleInfo
) :
    BasicsModule {

    override val logger: Logger = Logger.getLogger("basics.${info.name}")

    override val commandManager: BukkitCommandManager<CommandSender> = plugin.commandManager

    override val config = getOrCreateConfig("config.yml");

    fun getResource(path: String): URL {
        val actualPath = if (path.substring(0, 1) == "/") path else "/$path"
        return javaClass.getResource(actualPath) ?: error("Resource $path not found")
    }

    fun getResourceAsStream(path: String): InputStream {
        val actualPath = if (path.substring(0, 1) == "/") path else "/$path"
        return javaClass.getResourceAsStream(actualPath) ?: error("Resource $path not found")
    }

    fun getOrCreateConfig(sourceName: String): FileConfiguration {
        val configName = if (sourceName == "config.yml") info.name + ".yml" else sourceName;
        val file = File(plugin.dataFolder, configName);
        val exists = file.exists();

        if (!exists) {
            file.createNewFile();
            getResourceAsStream(sourceName).copyTo(FileOutputStream(file))
        }

        val configuration: FileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (!exists) {
            configuration.save(file);
        }

        return configuration;
    }

    override fun enable() {}

    override fun disable() {}

    override fun load() {}
}
