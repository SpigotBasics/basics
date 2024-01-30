package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.event.BasicsEventBus
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.permission.BasicsPermissionManager
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import com.github.spigotbasics.core.storage.NamespacedStorage
import java.util.concurrent.CompletableFuture
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

    override val commandManager = BasicsCommandManager(plugin.facade.getCommandMap(plugin.server.pluginManager))

    /**
     * Message Factory
     */
    override val messageFactory = plugin.messageFactory

    override val tagResolverFactory
        get() = plugin.tagResolverFactory

    override val permissionManager = BasicsPermissionManager(logger)

    private val storages: MutableMap<String, NamespacedStorage> = mutableMapOf()

    fun getConfig(configName: ConfigName/*, clazzToGetFrom: Class<*> = javaClass*/): SavedConfig =
        getConfig(configName/*, clazzToGetFrom*/, SavedConfig::class.java)

    fun <T : SavedConfig> getConfig(
        configName: ConfigName/*, clazzToGetFrom: Class<*>*/,
        configurationClass: Class<T>
    ): T = plugin.coreConfigManager.getConfig(
        configName.path,
        getNamespacedResourceName(configName.path),
        javaClass,
        configurationClass
    )

    override fun createStorage(name: String?): NamespacedStorage {
        // TODO: name must match regex [a-z0-9-_]+ (or something like that)
        val namespacedName = if (name == null) {
            "module.${info.name}"
        } else {
            "module.${info.name}.$name"
        }

        if(storages.containsKey(namespacedName)) {
            throw IllegalArgumentException("Storage with name '$namespacedName' already exists")
        }

        val storage: NamespacedStorage = plugin.storageManager.createStorage(namespacedName)
        storages[namespacedName] = storage
        return storage
    }

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
        if (reloadConfig) {
            config.reload()
        }
    }

    final override fun disable(): CompletableFuture<Void?> {
        val storageShutdownFuture =
            CompletableFuture.allOf(*storages.values.map { it.shutdown(10, java.util.concurrent.TimeUnit.SECONDS) }
                .toTypedArray()) // TODO: Configurable timeout
        scheduler.killAll()
        eventBus.dispose()
        commandManager.unregisterAll()
        permissionManager.unregisterAll()
        isEnabled = false
        return storageShutdownFuture
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }

}
