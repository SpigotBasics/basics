package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.exceptions.BasicsStorageAccessException
import com.github.spigotbasics.core.storage.StorageBackend
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.IOException
import java.sql.SQLException
import java.util.concurrent.*
import java.util.logging.Level

internal abstract class HikariBackend(config: HikariConfig, protected val tablePrefix: String, sqlSleep: Double) : StorageBackend {

    private val logger = BasicsLoggerFactory.getCoreLogger(HikariBackend::class)

    protected val selectSleepFunction = if(sqlSleep > 0) "SELECT SLEEP($sqlSleep);" else null // TODO: Get rid of this

    protected val dataSource = HikariDataSource(config)

    override fun setupNamespace(namespace: String) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $tablePrefix$namespace (
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
            logger.log(Level.SEVERE, "Could not execute create statement for namespace $tablePrefix$namespace", e)
            throw (IOException(e))
        }
    }


    override fun getJsonElement(namespace: String, keyId: String): CompletableFuture<JsonElement?> {
        return CompletableFuture.supplyAsync {
            try {
                selectSleep()
                val sql = "SELECT data FROM $tablePrefix$namespace WHERE key_id = ?"
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
                logger.log(Level.SEVERE, "Could not SELECT from $tablePrefix$namespace", e)
                throw BasicsStorageAccessException("Could not SELECT from $tablePrefix$namespace", e)
            }
            null
        }
    }

    fun selectSleep() {
        if (selectSleepFunction != null) {
            dataSource.connection.use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.execute(selectSleepFunction)
                }
            }
        }
    }

    override fun shutdown(): CompletableFuture<Void?> {
        logger.info("Waiting up to 10 seconds for Hikari DataSource to close ...")
        val future = CompletableFuture.runAsync {
            dataSource.close()
        }

        val scheduler = Executors.newScheduledThreadPool(1)
        scheduler.schedule({
            if (!future.isDone) {
                future.completeExceptionally(
                    TimeoutException("Couldn't close Hikari DataSource in time.")
                )
            }
        }, 10, TimeUnit.SECONDS)

        scheduler.shutdown()
        return future
    }

}