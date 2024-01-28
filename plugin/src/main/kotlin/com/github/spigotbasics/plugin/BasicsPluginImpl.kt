package com.github.spigotbasics.plugin

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.Constants
import com.github.spigotbasics.core.MinecraftVersion
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.AudienceProvider
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.TagResolverFactory
import com.github.spigotbasics.core.module.manager.ModuleManager
import com.github.spigotbasics.libraries.io.papermc.lib.PaperLib
import com.github.spigotbasics.pipe.SpigotPaperFacade
import com.github.spigotbasics.pipe.paper.PaperFacade
import com.github.spigotbasics.pipe.spigot.SpigotFacade

import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BasicsPluginImpl : JavaPlugin(), BasicsPlugin {

    private val rustySpigotThreshold =
        MinecraftVersion.fromBukkitVersion(getTextResource(Constants.RUSTY_SPIGOT_THRESHOLD_FILE_NAME)?.use { it.readText() }
            ?: error("Missing ${Constants.RUSTY_SPIGOT_THRESHOLD_FILE_NAME} resource file"))

    override val audienceProvider: AudienceProvider = AudienceProvider(this)

    override val facade: SpigotPaperFacade = if (PaperLib.isPaper()) {
        PaperFacade()
    } else {
        SpigotFacade()
    }

    override val moduleFolder = File(dataFolder, "modules")
    override val moduleManager = ModuleManager(this, moduleFolder)
    override val tagResolverFactory: TagResolverFactory = TagResolverFactory(facade)
    override val messageFactory: MessageFactory = MessageFactory(audienceProvider, tagResolverFactory)
    override val coreConfigManager: CoreConfigManager = CoreConfigManager(this, messageFactory)
    override val messages: CoreMessages =
        coreConfigManager.getConfig("messages.yml", "messages.yml", CoreMessages::class.java, CoreMessages::class.java)

    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)
    override fun getLogger() = logger

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


        moduleManager.loadAndEnableAllModulesFromModulesFolder()
        reloadCustomTags()
    }

    private fun reloadCustomTags() {
        tagResolverFactory.loadCustomTags(
            coreConfigManager.getConfig(
                Constants.CUSTOM_TAGS_FILE_NAME,
                Constants.CUSTOM_TAGS_FILE_NAME,
                TagResolverFactory::class.java
            )
        )
    }

    override fun reloadCoreConfig() {
        reloadCustomTags()
        messages.reload()
    }

}
