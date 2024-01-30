package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.StorageBackend
import com.github.spigotbasics.core.storage.StorageType
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.io.IOException
import java.util.concurrent.CompletableFuture

class JsonBackend(private val directory: File) : StorageBackend {

    override val type = StorageType.JSON

    init {
        if (!directory.isDirectory) {
            val success = directory.mkdirs()
            if (!success) {
                throw IOException("Could not create storage directory $directory")
            }
        }
    }

    override fun getJsonObject(namespace: String, user: String): CompletableFuture<JsonObject?> {
        return CompletableFuture.supplyAsync {
            val file = getFile(namespace, user)
            if (!file.parentFile.isDirectory) {
                file.parentFile.mkdirs()
            }
            if (!file.exists()) {
                return@supplyAsync null
            } else {
                file.reader().use { reader ->
                    val json = JsonParser.parseReader(reader)
                    return@supplyAsync json.asJsonObject
                }
            }
        }
    }


    override fun setJsonObject(namespace: String, user: String, value: JsonObject?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            val file = getFile(namespace, user)
            if (value == null) {
                val deleted = file.delete()
                if(!deleted) {
                    throw IllegalStateException("Could not delete file $file")
                }
            } else {
                file.writeText(value.toString())
            }
        }
    }

    private fun getFile(key: String, user: String) = File(File(directory, key), "$user.json")
}