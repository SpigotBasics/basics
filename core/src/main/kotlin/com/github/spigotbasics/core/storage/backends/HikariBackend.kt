package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.StorageBackend
import com.google.gson.JsonElement
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


    override fun getJsonElement(namespace: String, keyId: String): CompletableFuture<JsonElement?> {
        return CompletableFuture.supplyAsync {
            try {
                val sql = "SELECT data FROM $namespace WHERE key_id = ?"
                dataSource.connection.use { conn ->
                    conn.prepareStatement(sql).use { stmt ->
                        stmt.setString(1, keyId)
                        stmt.executeQuery().use { rs ->
                            if (rs.next()) {
                                val jsonData = rs.getString("data")
                                return@supplyAsync JsonParser.parseString(jsonData)
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

}