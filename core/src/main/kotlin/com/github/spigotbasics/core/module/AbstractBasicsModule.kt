package com.github.spigotbasics.core.module

import cloud.commandframework.bukkit.BukkitCommandManager
import com.github.spigotbasics.core.BasicsPlugin
import org.bukkit.command.CommandSender
import java.net.URL
import java.util.logging.Logger

abstract class AbstractBasicsModule(
    final override val plugin: BasicsPlugin, final override val info: ModuleInfo
) :
    BasicsModule {

    override val logger: Logger = Logger.getLogger("basics.${info.name}")

    override val commandManager: BukkitCommandManager<CommandSender> = plugin.commandManager

    // Loading the config should happen here

    fun getResource(path: String): URL {
        return javaClass.getResource(path) ?: error("Resource $path not found")
    }

    override fun enable() {}

    override fun disable() {}

    override fun load() {}


}
