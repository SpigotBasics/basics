package com.github.spigotbasics.plugin

import co.aikar.commands.PaperCommandManager
import com.github.spigotbasics.core.BasicsLoggerFactory
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.manager.ModuleManager
import com.github.spigotbasics.plugin.commands.BasicsCommand
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BasicsPluginImpl : JavaPlugin(), BasicsPlugin {
    override val moduleFolder = File(dataFolder, "modules")
    override val moduleManager = ModuleManager(this, moduleFolder)

    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)
    override fun getLogger() = logger

    override val commandManager: PaperCommandManager by lazy {
        PaperCommandManager(this)
    }

    override fun onLoad() {
        if (!moduleFolder.isDirectory) {
            logger.info("Creating modules folder at ${moduleFolder.absolutePath}")
            moduleFolder.mkdirs()
        }
    }

    override fun onEnable() {
        setupAcf()
        moduleManager.loadModules()
    }

    private fun setupAcf() {
        commandManager.enableUnstableAPI("help"); // Allow using generateCommandHelp()
        registerCommandCompletions()
        registerCommands()
    }

    private fun registerCommandCompletions() {
        val completions = commandManager.commandCompletions
        completions.registerAsyncCompletion("allmodules") {
            moduleManager.modules.map { it.info.name }
        }

        completions.registerAsyncCompletion("enabledmodules") {
            moduleManager.enabledModules.map { it.info.name }
        }

        completions.registerAsyncCompletion("disabledmodules") {
            moduleManager.disabledModules.map { it.info.name }
        }

    }

    private fun registerCommands() {
        commandManager.registerCommand(BasicsCommand(this));
    }

}
