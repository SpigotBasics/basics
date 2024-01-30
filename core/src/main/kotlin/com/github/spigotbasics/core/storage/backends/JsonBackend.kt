package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.storage.StorageBackend
import com.github.spigotbasics.core.storage.StorageType
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.io.IOException
import java.util.concurrent.CompletableFuture

class JsonBackend(private val directory: File, private val ioDelay: Long = 0L) : StorageBackend {

    override val type = StorageType.JSON

    init {
        if (!directory.isDirectory) {
            val success = directory.mkdirs()
            if (!success) {
                throw IOException("Could not create storage directory $directory")
            }
        }
    }

    private fun ioDelay() {
        if(ioDelay > 0L) {
            println("Sleeping for $ioDelay ms")
            Thread.sleep(ioDelay)
            println("Sleep done.")
        }
    }

    override fun getJsonObject(namespace: String, user: String): CompletableFuture<JsonObject?> {
        println("JsonBackend.getJsonObject($namespace, $user)")
        return CompletableFuture.supplyAsync {
            val file = getFile(namespace, user)
            if (!file.parentFile.isDirectory) {
                file.parentFile.mkdirs()
            }
            ioDelay()
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
        println("JsonBackend.setJsonObject($namespace, $user, $value)")
        return CompletableFuture.runAsync {
            val file = getFile(namespace, user)
            ioDelay()
            if (value == null) {
                println("value == null, deleting file...")
                val deleted = file.delete()
                if(!deleted) {
                    throw IllegalStateException("Could not delete file $file")
                }
            } else {
                println("value != null, writing to file...")
                file.writeText(value.toString())
            }
        }
    }

    private fun getFile(key: String, user: String) = File(File(directory, key), "$user.json")
}