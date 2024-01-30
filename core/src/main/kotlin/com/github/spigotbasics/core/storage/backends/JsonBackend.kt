package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.StorageBackend
import com.github.spigotbasics.core.storage.StorageType
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File
import java.io.IOException
import java.util.concurrent.CompletableFuture

class JsonBackend(private val directory: File, private val ioDelay: Long = 0L) : StorageBackend {

    override val type = StorageType.JSON

    private fun ioDelay() {
        if(ioDelay > 0L) {
            Thread.sleep(ioDelay)
        }
    }

    override fun getJsonElement(namespace: String, keyId: String): CompletableFuture<JsonElement?> {
        return CompletableFuture.supplyAsync {
            val file = getFile(namespace, keyId)
            if (!file.parentFile.isDirectory) {
                file.parentFile.mkdirs()
            }
            ioDelay()
            if (!file.exists()) {
                return@supplyAsync null
            } else {
                file.reader().use { reader ->
                    val json = JsonParser.parseReader(reader)
                    return@supplyAsync json
                }
            }
        }
    }


    override fun setJsonElement(namespace: String, keyId: String, value: JsonElement?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            val file = getFile(namespace, keyId)
            ioDelay()
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

    override fun setupNamespace(namespace: String) {
        if (!directory.isDirectory) {
            val success = directory.mkdirs()
            if (!success) {
                throw IOException("Could not create storage directory $directory")
            }
        }
    }

    private fun getFile(key: String, user: String) = File(File(directory, key), "$user.json")
}