package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import java.io.File

class StorageConfig(private val plugin: BasicsPlugin, file: File) : SavedConfig(plugin, file) {

    private val logger = BasicsLoggerFactory.getConfigLogger(file)

    val storageType: StorageType
        get() {
            val type = getString("type") ?: return StorageType.JSON
            return try {
                StorageType.valueOf(type.uppercase())
            } catch (e: IllegalArgumentException) {
                logger.severe("Invalid storage backend type '$type', falling back to JSON")
                StorageType.JSON
            }
        }

    val jsonDirectory: File
        get() = File(replaceDir(getString("json-directory") ?: "%plugindir%/data-storage/"))

    val sqliteFile: File
        get() = File(replaceDir(getString("sqlite-file") ?: "%plugindir%/data-storage/database.db"))

    val mysqlHost: String?
        get() = getString("mysql.host")

    val mysqlPort: Int
        get() = getInt("mysql.port")

    val mysqlDatabase: String?
        get() = getString("mysql.database")

    val mysqlUsername: String?
        get() = getString("mysql.username")

    val mysqlPassword: String?
        get() = getString("mysql.password")

    private fun replaceDir(dir: String): String {
        return dir.replace("%plugindir%", plugin.dataFolder.absolutePath)
    }

}
