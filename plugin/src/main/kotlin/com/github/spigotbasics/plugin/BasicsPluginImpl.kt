package com.github.spigotbasics.plugin

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.ChunkTicketManager
import com.github.spigotbasics.core.Constants
import com.github.spigotbasics.core.MinecraftVersion
import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.arguments.ItemMaterialArg
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.config.FixClassLoadingConfig
import com.github.spigotbasics.core.listeners.PlayerCommandListSendListener
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.AudienceProvider
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.tags.TagResolverFactory
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
        MinecraftVersion.fromBukkitVersion(
            getTextResource(Constants.RUSTY_SPIGOT_THRESHOLD_FILE_NAME)?.use { it.readText() }
                ?: error("Missing ${Constants.RUSTY_SPIGOT_THRESHOLD_FILE_NAME} resource file"),
        )

    override val audienceProvider: AudienceProvider = AudienceProvider(this)

    override val facade: SpigotPaperFacade =
        if (Spiper.isPaper) {
            PaperFacade()
        } else {
            SpigotFacade()
        }

    override val moduleFolder = File(dataFolder, "modules")
    override val moduleManager = ModuleManager(this, server, moduleFolder)
    override val tagResolverFactory: TagResolverFactory = TagResolverFactory(facade)
    override val messageFactory: MessageFactory = MessageFactory(audienceProvider, tagResolverFactory)
    override val coreConfigManager: CoreConfigManager = CoreConfigManager(messageFactory, dataFolder)
    override val config: CoreConfig =
        coreConfigManager.getConfig(
            ConfigName.CONFIG.path,
            ConfigName.CONFIG.path,
            CoreConfig::class.java,
            CoreConfig::class.java,
        )
    override val messages: CoreMessages =
        coreConfigManager.getConfig(
            ConfigName.MESSAGES.path,
            ConfigName.MESSAGES.path,
            CoreMessages::class.java,
            CoreMessages::class.java,
        )
    override val storageManager: StorageManager by lazy { StorageManager(coreConfigManager) }

    override val corePlayerData: CorePlayerData by lazy { CorePlayerData(storageManager) }
    override val chunkTicketManager: ChunkTicketManager = ChunkTicketManager()

    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)

    internal val modulePlayerDataLoader by lazy {
        ModulePlayerDataLoader(
            storageManager.config,
            moduleManager,
            messages,
        )
    }

    private val classLoaderFixer =
        ClassLoaderFixer(
            file.absolutePath,
            coreConfigManager.getConfig(
                "fix-class-loading.yml",
                "fix-class-loading.yml",
                ClassLoaderFixer::class.java,
                FixClassLoadingConfig::class.java,
            ),
        )

    /**
     * Checks if this server is running a rusty version of Spigot.
     * A rusty version of spigot is a version that's older than the version of spigot-api used by the core module.
     *
     * @return True if this server is running a rusty version of Spigot
     */
    private fun isRustySpigot(): Boolean {
        val myVersion = MinecraftVersion.current()
        return rustySpigotThreshold > myVersion
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

        initializeHeavyClasses()

        // ::storageManager.get()
        server.pluginManager.registerEvents(CorePlayerDataListener(corePlayerData), this)

        moduleManager.loadAndEnableAllModulesFromModulesFolder()
        reloadCustomTags()
        server.pluginManager.registerEvents(tagResolverFactory.Listener(), this)
        server.pluginManager.registerEvents(modulePlayerDataLoader, this)
        server.pluginManager.registerEvents(PlayerCommandListSendListener(facade.getCommandMap(server.pluginManager)), this)

        getCommand("basicsdebug")?.setExecutor(BasicsDebugCommand(this))
    }

    private fun initializeHeavyClasses() {
        logger.info("Initializing heavy classes...")
        ItemMaterialArg.Companion
        logger.info("Heavy classes initialized.")
    }

    private fun reloadCustomTags() {
        tagResolverFactory.loadAndCacheAllTagResolvers(
            coreConfigManager.getConfig(
                Constants.CUSTOM_TAGS_FILE_NAME,
                Constants.CUSTOM_TAGS_FILE_NAME,
                TagResolverFactory::class.java,
            ),
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

//    fun findAllClassesUsingClassLoader(packageName: String): Set<Class<*>?> {
//        val stream: InputStream = this.classLoader
//            .getResourceAsStream(packageName.replace("[.]".toRegex(), "/"))
//        val reader = BufferedReader(InputStreamReader(stream))
//        return reader.lines()
//            .filter({ line: String -> line.endsWith(".class") })
//            .map({ line: String ->
//                getClass(
//                    line,
//                    packageName
//                )
//            })
//            .collect(Collectors.toSet())
//    }
//
//    private fun getClass(className: String, packageName: String): Class<*>? {
//        try {
//            return Class.forName(
//                packageName + "."
//                        + className.substring(0, className.lastIndexOf('.'))
//            )
//        } catch (e: ClassNotFoundException) {
//            // handle the exception
//        }
//        return null
//    }
}
