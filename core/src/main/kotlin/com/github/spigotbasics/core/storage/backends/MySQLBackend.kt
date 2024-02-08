package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.exceptions.BasicsStorageAccessException
import com.github.spigotbasics.core.storage.MySQLDatabaseInfo
import com.github.spigotbasics.core.storage.StorageType
import com.google.gson.JsonElement
import java.util.concurrent.CompletableFuture

internal class MySQLBackend(dbInfo: MySQLDatabaseInfo, sqlSleep: Double) : HikariBackend(
    HikariConfigFactory.createMysqlConfig(dbInfo),
    dbInfo.tablePrefix,
    sqlSleep,
) {
    override val type = StorageType.MYSQL

    override fun setJsonElement(
        namespace: String,
        keyId: String,
        value: JsonElement?,
    ): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            try {
                selectSleep()
                val sql =
                    if (value == null) {
                        "DELETE FROM $tablePrefix$namespace WHERE key_id = ?"
                    } else {
                        "INSERT INTO $tablePrefix$namespace (key_id, data) VALUES (?, ?) ON DUPLICATE KEY UPDATE data = VALUES(data)"
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
                throw BasicsStorageAccessException("Could not INSERT into $tablePrefix$namespace", e)
            }
        }
    }
}
