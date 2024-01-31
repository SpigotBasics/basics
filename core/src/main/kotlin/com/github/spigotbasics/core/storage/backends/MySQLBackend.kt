package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.MySQLDatabaseInfo
import com.github.spigotbasics.core.storage.StorageType
import com.google.gson.JsonElement
import java.util.concurrent.CompletableFuture

class MySQLBackend(dbInfo: MySQLDatabaseInfo, ioDelay: Long): HikariBackend(HikariConfigFactory.createMysqlConfig(dbInfo), ioDelay) {

    override val type = StorageType.MYSQL

    override fun setJsonElement(namespace: String, keyId: String, value: JsonElement?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            try {
                val sql = if (value == null) {
                    "DELETE FROM $namespace WHERE key_id = ?"
                } else {
                    "INSERT INTO $namespace (key_id, data) VALUES (?, ?) ON DUPLICATE KEY UPDATE data = VALUES(data)"
                }

                dataSource.connection.use { conn ->
                    conn.prepareStatement(sql).use { stmt ->
                        stmt.setString(1, keyId)
                        value?.let {
                            stmt.setString(2, it.toString())
                        }
                        stmt.executeUpdate()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}