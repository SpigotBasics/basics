package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.config.CoreConfigManager

class StorageManager(configManager: CoreConfigManager) {

    val config = configManager.getConfig(ConfigName.STORAGE.path, ConfigName.STORAGE.path, StorageManager::class.java, StorageConfig::class.java)

}