package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.storage.backends.JsonBackend
import com.github.spigotbasics.core.storage.backends.MySQLBackend
import com.github.spigotbasics.core.storage.backends.SQLiteBackend

// TODO: Storages saved in vals will be closed when a module is disabled and then enabled again.
// Possible fix: Prevent storages from being created before isEnabled is true.

// TODO: Plugins shall be notified about player joins instead of listening on their own, so they can fetch playerdata if needed
class StorageManager(configManager: CoreConfigManager) {

    val namespaceRegex = "^[_a-zA-Z][_a-zA-Z0-9]{0,63}\$".toRegex()

    val config = configManager.getConfig(
        ConfigName.STORAGE.path,
        ConfigName.STORAGE.path,
        StorageManager::class.java,
        StorageConfig::class.java
    )

    private val backend: StorageBackend = when (config.storageType) {
        StorageType.JSON -> JsonBackend(config.jsonDirectory, config.ioDelay)
        StorageType.SQLITE -> SQLiteBackend(config.sqliteFile, config.ioDelay)
        StorageType.MYSQL -> MySQLBackend(config.mysqlInfo, config.ioDelay)
    }

    internal fun createStorage(namespace: String): NamespacedStorage {
        if (!namespaceRegex.matches(namespace)) {
            throw IllegalArgumentException("Namespace '$namespace' does not match regex ${namespaceRegex.pattern}")
        }
        return NamespacedStorage(backend, namespace)
    }

}