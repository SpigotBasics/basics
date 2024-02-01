package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.BasicsStorageAccessException
import com.github.spigotbasics.core.storage.StorageType
import com.google.gson.JsonElement

import java.io.File
import java.util.concurrent.CompletableFuture

internal class SQLiteBackend (file: File, sqlSleep: Double): HikariBackend(HikariConfigFactory.createSqliteConfig(file), sqlSleep) {

    override val type = StorageType.SQLITE

    override fun setJsonElement(namespace: String, keyId: String, value: JsonElement?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            try {
                selectSleep()
                val sql = if (value == null) {
                    "DELETE FROM $namespace WHERE key_id = ?"
                } else {
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
                throw BasicsStorageAccessException("Could not INSERT into $namespace", e)
            }
        }
    }

}