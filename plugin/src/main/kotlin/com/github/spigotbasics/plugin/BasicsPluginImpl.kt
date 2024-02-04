package com.github.spigotbasics.plugin

import com.github.spigotbasics.core.*
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.config.FixClassLoadingConfig
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.AudienceProvider
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.tags.AdventureTagResolverFactory
import com.github.spigotbasics.core.module.manager.ModuleManager
import com.github.spigotbasics.core.playerdata.CorePlayerData
import com.github.spigotbasics.core.playerdata.CorePlayerDataListener
import com.github.spigotbasics.core.playerdata.ModulePlayerDataLoader
import com.github.spigotbasics.core.storage.StorageManager
import com.github.spigotbasics.core.util.ClassLoaderFixer
import com.github.spigotbasics.pipe.SpigotPaperFacade
import com.github.spigotbasics.pipe.paper.PaperFacade
import com.github.spigotbasics.pipe.spigot.SpigotFacade
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.logging.Level

class BasicsPluginImpl : JavaPlugin(), BasicsPlugin {

    private val rustySpigotThreshold =
        MinecraftVersion.fromBukkitVersion(getTextResource(Constants.RUSTY_SPIGOT_THRESHOLD_FILE_NAME)?.use { it.readText() }
            ?: error("Missing ${Constants.RUSTY_SPIGOT_THRESHOLD_FILE_NAME} resource file"))

    override val audienceProvider: AudienceProvider = AudienceProvider(this)

    override val facade: SpigotPaperFacade = if (Spiper.isPaper) {
        PaperFacade()
    } else {
        SpigotFacade()
    }

    override val moduleFolder = File(dataFolder, "modules")
    override val moduleManager = ModuleManager(this, server, moduleFolder)
    override val tagResolverFactory: AdventureTagResolverFactory = AdventureTagResolverFactory(facade)
    override val messageFactory: MessageFactory = MessageFactory(audienceProvider, tagResolverFactory)
    override val coreConfigManager: CoreConfigManager = CoreConfigManager(messageFactory, dataFolder)
    override val messages: CoreMessages =
        coreConfigManager.getConfig("messages.yml", "messages.yml", CoreMessages::class.java, CoreMessages::class.java)
    override val storageManager: StorageManager by lazy { StorageManager(coreConfigManager) }

    override val corePlayerData: CorePlayerData by lazy { CorePlayerData(storageManager) }

    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)

    internal val modulePlayerDataLoader by lazy {
        ModulePlayerDataLoader(
            storageManager.config,
            moduleManager,
            messages
        )
    }

    private val classLoaderFixer = ClassLoaderFixer(
        coreConfigManager.getConfig(
            "fix-class-loading.yml",
            "fix-class-loading.yml",
            ClassLoaderFixer::class.java,
            FixClassLoadingConfig::class.java
        )
    )

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
        Basics.setPlugin(this)
        if (!moduleFolder.isDirectory) {
            logger.info("Creating modules folder at ${moduleFolder.absolutePath}")
            moduleFolder.mkdirs()
        }
    }

    override fun onEnable() {
        classLoaderFixer.trickOnEnable()

        if (isRustySpigot()) {
            logger.severe("Your server version (${MinecraftVersion.current()}) is terminally rusty.")
            logger.severe("Please update to at least Spigot $rustySpigotThreshold!")
            logger.severe("")
            logger.severe("See here for more information:")
            logger.severe("- https://j3f.de/downloadspigotlatest")
            logger.severe("- https://j3f.de/howtoupdatespigot")
            server.pluginManager.disablePlugin(this)
            return
        }

        // ::storageManager.get()
        server.pluginManager.registerEvents(CorePlayerDataListener(corePlayerData), this)

        moduleManager.loadAndEnableAllModulesFromModulesFolder()
        reloadCustomTags()
        server.pluginManager.registerEvents(tagResolverFactory.Listener(), this)
        server.pluginManager.registerEvents(modulePlayerDataLoader, this)

        getCommand("basicsdebug")?.setExecutor(BasicsDebugCommand(this))
    }


    private fun reloadCustomTags() {
        tagResolverFactory.loadAndCacheAllTagResolvers(
            coreConfigManager.getConfig(
                Constants.CUSTOM_TAGS_FILE_NAME,
                Constants.CUSTOM_TAGS_FILE_NAME,
                AdventureTagResolverFactory::class.java
            )
        )
    }

    override fun reloadCoreConfig() {
        reloadCustomTags()
        messages.reload()
    }

    override fun onDisable() {
        classLoaderFixer.setSuperEnabled(this, true)
        moduleManager.disableAndUnloadAllModules()
        storageManager.shutdown().whenComplete { _, e ->
            if (e != null) {
                logger.log(Level.SEVERE, "Failed to shutdown storage backend", e)
            } else {
                logger.info("Storage backend shutdown completed")
            }
        }.get()
        classLoaderFixer.setSuperEnabled(this, false)
        modulePlayerDataLoader.shutdownScheduler()
    }


}
