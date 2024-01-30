package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.StorageType
import com.google.gson.JsonObject

import java.io.File
import java.util.concurrent.CompletableFuture

class SQLiteBackend (file: File, ioDelay: Long): HikariBackend(HikariConfigFactory.createSqliteConfig(file), ioDelay) {

    override val type = StorageType.SQLITE

    override fun setJsonObject(namespace: String, keyId: String, value: JsonObject?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            try {
                val sql = if (value == null) {
                    "DELETE FROM $namespace WHERE key_id = ?"
                } else {
                    // SQLite syntax for upsert (insert or replace)
                    "INSERT OR REPLACE INTO $namespace (key_id, data) VALUES (?, ?)"
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