package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.event.BasicsEventBus
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import java.util.logging.Logger

abstract class AbstractBasicsModule(context: ModuleInstantiationContext) : BasicsModule {

    /**
     * Module class loader
     */
    final override val moduleClassLoader = context.classLoader

    /**
     * Module file
     */
    final override val moduleFile = context.file

    /**
     * Module info
     */
    final override val info: ModuleInfo = context.info

    /**
     * Logger for this module
     */
    final override val logger: Logger = BasicsLoggerFactory.getModuleLogger(this)

    /**
     * Plugin instance
     */
    final override val plugin: BasicsPlugin = context.plugin

    /**
     * Commands Manager
     */
    final override val commandManager: BasicsCommandManager = context.plugin.createCommandManager()

    /**
     * Event bus for registering events
     */
    final override val eventBus: BasicsEventBus = BasicsEventBus(context.plugin)

    /**
     * Config
     */
    override var config = getConfig(ConfigName.CONFIG)

    override fun reloadConfig() {
        config.reload()
    }

    /**
     * Scheduler
     */
    override val scheduler = BasicsScheduler(plugin)

    /**
     * Adventure Audience
     */
    override val audience = plugin.audience

    override val tagResolverFactory
        get() = plugin.tagResolverFactory

    fun getConfig(configName: ConfigName, clazz: Class<out SavedConfig> = SavedConfig::class.java): SavedConfig = plugin.coreConfigManager.getConfig(configName.path, getNamespacedResourceName(configName.path), SavedConfig::class.java)

    /**
     * Get namespaced resource name. For `config.yml` this will simply be `<module-name>.yml`, for all other files it will be `<module-name>-<file-name>`.
     *
     * @param path Path to the resource
     * @return Namespaced resource name
     */
    private fun getNamespacedResourceName(path: String): String {
        var newPath = path

        // Remove leading slash
        if (path.startsWith("/")) {
            newPath = path.substring(1)
        }

        // All other files are called <module-name>-<file-name>
        return "${info.name}-$newPath"
    }

    private var isEnabled = false
    final override fun enable(reloadConfig: Boolean) {
        isEnabled = true
        if(reloadConfig) {
            config.reload()
        }
    }

    final override fun disable() {
        scheduler.killAll()
        commandManager.unregisterAll()
        eventBus.dispose()
        isEnabled = false
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }

}
