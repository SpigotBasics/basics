package com.github.spigotbasics.plugin

import co.aikar.commands.PaperCommandManager
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.MinecraftVersion
import com.github.spigotbasics.core.RUSTY_SPIGOT_THRESHOLD_FILE_NAME
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.config.CoreMessages
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.minimessage.TagResolverFactory
import com.github.spigotbasics.core.module.loader.ModuleJarFileFilter
import com.github.spigotbasics.core.module.manager.ModuleManager
import com.github.spigotbasics.plugin.commands.BasicsCommand
import com.github.spigotbasics.plugin.commands.BasicsDebugCommand
import com.github.spigotbasics.plugin.commands.CommandCompletions
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BasicsPluginImpl : JavaPlugin(), BasicsPlugin {

    private val rustySpigotThreshold =
        MinecraftVersion.fromBukkitVersion(getTextResource(RUSTY_SPIGOT_THRESHOLD_FILE_NAME)?.use { it.readText() }
            ?: error("Missing $RUSTY_SPIGOT_THRESHOLD_FILE_NAME resource file"))

    override val moduleFolder = File(dataFolder, "modules")
    override val moduleManager = ModuleManager(this, moduleFolder)
    override val audience by lazy { BukkitAudiences.create(this) }
    override val tagResolverFactory: TagResolverFactory = TagResolverFactory()
    override val coreConfigManager: CoreConfigManager = CoreConfigManager(this, tagResolverFactory)
    override val messages: CoreMessages =
        coreConfigManager.getConfig("messages.yml", "messages.yml", CoreMessages::class.java, CoreMessages::class.java)

    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)
    override fun getLogger() = logger

    private val commandManager: PaperCommandManager by lazy {
        PaperCommandManager(this)
    }

    /**
     * Checks if this server is running a rusty version of Spigot.
     * A rusty version of spigot is a version that's older than the version of spigot-api used by the core module.
     *
     * @return True if this server is running a rusty version of Spigot
     */
    private fun isRustySpigot(): Boolean {
        val myVersion = MinecraftVersion.current()
        return rustySpigotThreshold > myVersion;
    }

    override fun onLoad() {
        if (!moduleFolder.isDirectory) {
            logger.info("Creating modules folder at ${moduleFolder.absolutePath}")
            moduleFolder.mkdirs()
        }
    }

    override fun onEnable() {
        if (isRustySpigot()) {
            logger.severe("Your server version (${MinecraftVersion.current()}) is terminally rusty. Please update to at least Spigot $rustySpigotThreshold!")
            logger.severe("See here for more information:")
            logger.severe("- https://j3f.de/downloadspigotlatest")
            logger.severe("- https://j3f.de/howtoupdatespigot")
            server.pluginManager.disablePlugin(this)
            return
        }
        this::audience.get() // Force lazy init
        setupAcf()
        moduleManager.loadAndEnableAllModulesFromModulesFolder()

        reloadCustomTags()
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

        completions.registerAsyncCompletion(CommandCompletions.ENABLED_MODULES_AND_CORE) {
            listOf("core") + moduleManager.enabledModules.map { it.info.name }
        }

    }

    private fun registerCommands() {
        commandManager.registerCommand(BasicsCommand(this));
        commandManager.registerCommand(BasicsDebugCommand(this))
    }

    override fun createCommandManager(): BasicsCommandManager {
        return BasicsCommandManager(commandManager)
    }

    private fun reloadCustomTags() {
        tagResolverFactory.loadCustomTags(coreConfigManager.getConfig("custom-tags.yml", "custom-tags.yml", TagResolverFactory::class.java))
    }

    override fun reloadCoreConfig() {
        reloadCustomTags()
        messages.reload()
    }

}
