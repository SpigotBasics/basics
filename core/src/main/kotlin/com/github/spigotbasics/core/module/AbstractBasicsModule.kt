package com.github.spigotbasics.core.module

import cloud.commandframework.SenderMapper
import cloud.commandframework.bukkit.BukkitCommandManager
import cloud.commandframework.execution.ExecutionCoordinator
import com.github.spigotbasics.core.BasicsPlugin
import java.net.URL
import java.util.logging.Logger

abstract class AbstractBasicsModule(protected val plugin: BasicsPlugin, final override val info: ModuleInfo) :
    BasicsModule {

    init {
        val commandManager = BukkitCommandManager(
            plugin,
            ExecutionCoordinator.simpleCoordinator(),
            SenderMapper.identity()
        );
    }

    override val logger: Logger = Logger.getLogger(info.mainClass)

    // Loading the config should happen here

    fun getResource(path: String): URL {
        return javaClass.getResource(path) ?: error("Resource $path not found")
    }

    override fun enable() {}

    override fun disable() {}

    override fun load() {}


}
