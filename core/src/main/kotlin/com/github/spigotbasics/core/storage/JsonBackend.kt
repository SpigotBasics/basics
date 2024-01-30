package com.github.spigotbasics.core.storage

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture

class JsonBackend(private val diretory: File) : StorageBackend {

    private fun getFile(key: String, user: UUID) = File(File(diretory, key), "$user.json")

    override fun getJsonObject(key: String, user: UUID): CompletableFuture<JsonObject?> {
        return CompletableFuture.supplyAsync {
            val file = getFile(key, user)
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


    override fun setJsonObject(key: String, user: UUID, value: JsonObject?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            val file = getFile(key, user)
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
}