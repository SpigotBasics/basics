package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.StorageBackend
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.IOException
import java.sql.SQLException
import java.util.concurrent.CompletableFuture

abstract class HikariBackend(config: HikariConfig, private val ioDelay: Long) : StorageBackend {

    protected val dataSource = HikariDataSource(config)

    override fun setupNamespace(namespace: String) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $namespace (
                key_id VARCHAR(255) PRIMARY KEY,
                data TEXT
            )
        """.trimIndent()

        try {
            dataSource.connection.use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.execute(sql)
                }
            }
        } catch (e: SQLException) {
            throw(IOException(e))
        }
    }


    override fun getJsonObject(namespace: String, keyId: String): CompletableFuture<JsonObject?> {
        return CompletableFuture.supplyAsync {
            try {
                val sql = "SELECT data FROM $namespace WHERE key_id = ?"
                dataSource.connection.use { conn ->
                    conn.prepareStatement(sql).use { stmt ->
                        stmt.setString(1, keyId)
                        stmt.executeQuery().use { rs ->
                            if (rs.next()) {
                                val jsonData = rs.getString("data")
                                return@supplyAsync JsonParser.parseString(jsonData).asJsonObject
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            null
        }
    }


    // TODO: FOllowing code is for MySQL
//    override fun setJsonObject(namespace: String, keyId: String, value: JsonObject?): CompletableFuture<Void?> {
//        return CompletableFuture.runAsync {
//            try {
//                val sql = if (value == null) {
//                    "DELETE FROM $namespace WHERE key_id = ?"
//                } else {
//                    "INSERT INTO $namespace (key_id, data) VALUES (?, ?) ON DUPLICATE KEY UPDATE data = VALUES(data)"
//                }
//
//                dataSource.connection.use { conn ->
//                    conn.prepareStatement(sql).use { stmt ->
//                        stmt.setString(1, keyId)
//                        value?.let {
//                            stmt.setString(2, it.toString())
//                        }
//                        stmt.executeUpdate()
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

}