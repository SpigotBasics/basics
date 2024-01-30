package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.storage.backends.JsonBackend

// TODO: Storages saved in vals will be closed when a module is disabled and then enabled again.
// Possible fix: Prevent storages from being created before isEnabled is true.

// TODO: Plugins shall be notified about player joins instead of listening on their own, so they can fetch playerdata if needed
class StorageManager(configManager: CoreConfigManager) {

    val config = configManager.getConfig(
        ConfigName.STORAGE.path,
        ConfigName.STORAGE.path,
        StorageManager::class.java,
        StorageConfig::class.java
    )

    private val backend: StorageBackend = when (config.storageType) {
        StorageType.JSON -> JsonBackend(config.jsonDirectory)
        StorageType.SQLITE -> TODO("SQLite Storage backend") //SqliteBackend(config.sqliteFile)
    }

    internal fun createStorage(namespace: String): NamespacedStorage {
        return NamespacedStorage(backend, namespace)
    }

}