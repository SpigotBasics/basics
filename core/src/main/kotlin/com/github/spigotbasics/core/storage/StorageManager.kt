package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.exceptions.BasicsStorageInitializeException
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.storage.backends.JsonBackend
import com.github.spigotbasics.core.storage.backends.MySQLBackend
import com.github.spigotbasics.core.storage.backends.SQLiteBackend
import java.util.concurrent.CompletableFuture

class StorageManager(configManager: CoreConfigManager) {
    private val logger = BasicsLoggerFactory.getCoreLogger(StorageManager::class)

    private val namespaceRegex = "^[_a-zA-Z][_a-zA-Z0-9]{0,63}\$".toRegex()

    val config =
        configManager.getConfig(
            ConfigName.STORAGE.path,
            ConfigName.STORAGE.path,
            StorageManager::class.java,
            StorageConfig::class.java,
        )

    private val backend: StorageBackend = IODelayedStorageBackend(createBackend(), config.ioDelay)

    private fun createBackend(): StorageBackend {
        try {
            logger.info("Initializing ${config.storageType} storage backend ...")
            return when (config.storageType) {
                StorageType.JSON -> JsonBackend(config.jsonDirectory)
                StorageType.SQLITE -> SQLiteBackend(config.sqliteFile, config.sqlSleep)
                StorageType.MYSQL -> MySQLBackend(config.mysqlInfo, config.sqlSleep)
            }
        } catch (e: Exception) {
            throw BasicsStorageInitializeException(e)
        }
    }

    internal fun createStorage(namespace: String): NamespacedStorage {
        require(namespaceRegex.matches(namespace)) { "Namespace '$namespace' does not match regex ${namespaceRegex.pattern}" }
        return NamespacedStorage(backend, namespace)
    }

    internal fun createCoreStorage(namespace: String): NamespacedStorage {
        return createStorage("_$namespace")
    }

    fun shutdown(): CompletableFuture<Void?> = backend.shutdown()
}
