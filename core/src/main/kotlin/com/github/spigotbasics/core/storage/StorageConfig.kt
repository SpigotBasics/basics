package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.extensions.getDurationAsMillis
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

    val mysqlTablePrefix: String
        get() = getString("mysql.table-prefix") ?: "basics_"

    val ioDelay: Long
        get() = getDurationAsMillis("debug.artificial-io-delay", 0L)

    val sqlSleep: Double
        get() = getDurationAsMillis("debug.sql-sleep-delay", 0L) / 1_000.0

    val joinTimeOut: Long
        get() = getDurationAsMillis("load-player-data-on-join.timeout", 2_000)

    val joinCacheDuration: Long
        get() = getDurationAsMillis("load-player-data-on-join.cache-duration", 60_000)


    val mysqlInfo: MySQLDatabaseInfo
        get() {
            val host = mysqlHost ?: error("MySQL host not set")
            val port = mysqlPort; if(port == 0) error("MySQL port not set")
            val database = mysqlDatabase ?: error("MySQL database not set")
            val username = mysqlUsername ?: error("MySQL username not set")
            val password = mysqlPassword ?: error("MySQL password not set")
            val tablePrefix = mysqlTablePrefix
            return MySQLDatabaseInfo(host, port, database, username, password, tablePrefix)
        }

    private fun replaceDir(dir: String): String {
        return dir.replace("%plugindir%", plugin.dataFolder.absolutePath)
    }

}
