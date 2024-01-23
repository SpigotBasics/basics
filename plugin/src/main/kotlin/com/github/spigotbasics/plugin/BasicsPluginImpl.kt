package com.github.spigotbasics.plugin

import co.aikar.commands.PaperCommandManager
import com.github.spigotbasics.core.BasicsLoggerFactory
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.minimessage.TagResolverFactory
import com.github.spigotbasics.core.module.loader.ModuleJarFileFilter
import com.github.spigotbasics.core.module.manager.ModuleManager
import com.github.spigotbasics.plugin.commands.BasicsCommand
import com.github.spigotbasics.plugin.commands.CommandCompletions
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.logging.Level

class BasicsPluginImpl : JavaPlugin(), BasicsPlugin {
    override val moduleFolder = File(dataFolder, "modules")
    override val moduleManager = ModuleManager(this, moduleFolder)
    override val audience by lazy { BukkitAudiences.create(this) }
    override val tagResolverFactory: TagResolverFactory = loadCustomTags()

    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)
    override fun getLogger() = logger

    private val commandManager: PaperCommandManager by lazy {
        PaperCommandManager(this)
    }

    override fun onLoad() {
        if (!moduleFolder.isDirectory) {
            logger.info("Creating modules folder at ${moduleFolder.absolutePath}")
            moduleFolder.mkdirs()
        }
    }

    override fun onEnable() {
        this::audience.get() // Force lazy init
        setupAcf()
        moduleManager.loadAndEnableAllModulesFromModulesFolder()
    }

    private fun setupAcf() {
        commandManager.enableUnstableAPI("help"); // Allow using generateCommandHelp()
        registerCommandCompletions()
        registerCommands()
    }

    private fun registerCommandCompletions() {
        val completions = commandManager.commandCompletions
        completions.registerAsyncCompletion(CommandCompletions.LOADED_MODULES) {
            moduleManager.loadedModules.map { it.info.name }
        }

        completions.registerAsyncCompletion(CommandCompletions.ENABLED_MODULES) {
            moduleManager.enabledModules.map { it.info.name }
        }

        completions.registerAsyncCompletion(CommandCompletions.DISABLED_MODULES) {
            moduleManager.disabledModules.map { it.info.name }
        }

        completions.registerAsyncCompletion(CommandCompletions.ALL_MODULE_FILES) {
            moduleFolder.listFiles(ModuleJarFileFilter)?.map { it.name } ?: listOf()
        }

    }

    private fun registerCommands() {
        commandManager.registerCommand(BasicsCommand(this));
    }

    override fun createCommandManager(): BasicsCommandManager {
        return BasicsCommandManager(commandManager)
    }

    private fun loadCustomTags(): TagResolverFactory {
        try {
            val yaml = getTextResource("custom-tags.yml")?.use { reader ->
                YamlConfiguration.loadConfiguration(reader)
            } ?: error("custom-tags.yml not found")

            val map = yaml.getValues(true).mapValues {
                val value: String = it.value?.toString() ?: ""
                println("tag <${it.key}> = $value")

                return@mapValues value
            }
            return TagResolverFactory(map)
        } catch (e: Exception) {
            logger.log(Level.WARNING, "Cannot load custom-tags.yml, disabling all custom tags", e)
            return TagResolverFactory(emptyMap())
        }
    }

}
