package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.storage.backends.JsonBackend

class StorageManager(configManager: CoreConfigManager) {

    val config = configManager.getConfig(
        ConfigName.STORAGE.path,
        ConfigName.STORAGE.path,
        StorageManager::class.java,
        StorageConfig::class.java
    )

    private val backend: StorageBackend = when(config.storageType) {
        StorageType.JSON -> JsonBackend(config.jsonDirectory)
        StorageType.SQLITE -> TODO("SQLite Storage backend") //SqliteBackend(config.sqliteFile)
    }

    fun createStorage(namespace: String): NamespacedStorage {
        return NamespacedStorage(backend, namespace)
    }

}