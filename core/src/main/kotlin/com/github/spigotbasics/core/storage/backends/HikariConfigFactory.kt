package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.MySQLDatabaseInfo
import com.zaxxer.hikari.HikariConfig
import java.io.File

internal object HikariConfigFactory {
    fun createSqliteConfig(file: File): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:sqlite:${file.absolutePath}"
//        config.driverClassName = "org.sqlite.JDBC"
//        config.connectionTestQuery = "SELECT 1"
//        config.maximumPoolSize = 1
//        config.minimumIdle = 1
//        config.idleTimeout = 60000
//        config.maxLifetime = 60000
//        config.leakDetectionThreshold = 30000
//        config.isAutoCommit = false
//        config.transactionIsolation = "TRANSACTION_SERIALIZABLE"
//        config.addDataSourceProperty("cachePrepStmts", true)
//        config.addDataSourceProperty("prepStmtCacheSize", 250)
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048)
        return config
    }

    fun createMysqlConfig(dbInfo: MySQLDatabaseInfo): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://${dbInfo.host}:${dbInfo.port}/${dbInfo.database}"
        config.username = dbInfo.username
        config.password = dbInfo.password
        return config
    }
}
