package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.event.BasicsEventBus
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.permission.BasicsPermissionManager
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import com.github.spigotbasics.core.storage.NamespacedStorage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.logging.Level

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
    final override val info = context.info

    /**
     * Logger for this module
     */
    final override val logger = BasicsLoggerFactory.getModuleLogger(this)

    /**
     * Plugin instance
     */
    final override val plugin = context.plugin

    /**
     * Event bus for registering events
     */
    final override val eventBus = BasicsEventBus(context.plugin)

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
        if (!isEnabled) {
            throw IllegalStateException("Cannot create storage while module is disabled")
        }
        val toReplaceRegex = "[^a-zA-Z0-9]".toRegex()
        var namespacedName = "m_${info.name.replace(toReplaceRegex, "_")}"
        if (name != null) {
            namespacedName += "__${name.replace(toReplaceRegex, "_")}"
        }

        if (storages.containsKey(namespacedName)) {
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
        val storageShutdownFuture = shutdownAllStorages() // TODO: Configurable timeout
        scheduler.killAll()
        eventBus.dispose()
        commandManager.unregisterAll()
        permissionManager.unregisterAll()
        isEnabled = false
        return storageShutdownFuture
    }

    private fun shutdownAllStorages(): CompletableFuture<Void?> =
        CompletableFuture.allOf(*storages.values.map {
            it.shutdown(10, TimeUnit.SECONDS).whenComplete { _, ex ->
                if (ex != null) logger.log(Level.SEVERE, "Failed to shutdown storage ${it.namespace}", ex)
                storages.remove(it.namespace)
            }
        }.toTypedArray())

    override fun isEnabled(): Boolean {
        return isEnabled
    }

}
