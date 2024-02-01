package com.github.spigotbasics.core.storage.backends

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.storage.BasicsStorageAccessException
import com.github.spigotbasics.core.storage.StorageBackend
import com.github.spigotbasics.core.storage.StorageType
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

internal class JsonBackend(private val directory: File) : StorageBackend {

    override val type = StorageType.JSON
    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)

    init {
        if(directory.exists() && !directory.isDirectory) {
            throw IOException("Storage directory $directory exists but is not a directory")
        }
        if(!directory.exists()) {
            val success = directory.mkdirs()
            if(!success) {
                throw IOException("Could not create storage directory $directory")
            }
        }
        val testFile = File(directory, "__test.json")
        testFile.writeText("{}")
        testFile.delete()
    }

    override fun getJsonElement(namespace: String, keyId: String): CompletableFuture<JsonElement?> {
        return CompletableFuture.supplyAsync {
            try {
                val file = getFile(namespace, keyId)
                if (!file.parentFile.isDirectory) {
                    file.parentFile.mkdirs()
                }
                if (!file.exists()) {
                    return@supplyAsync null
                } else {
                    file.reader().use { reader ->
                        val json = JsonParser.parseReader(reader)
                        return@supplyAsync json
                    }
                }
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Could not read file $namespace/$keyId.json", e)
                throw BasicsStorageAccessException("Could not read file $namespace/$keyId.json", e)
            }
        }
    }


    override fun setJsonElement(namespace: String, keyId: String, value: JsonElement?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            try {
                val file = getFile(namespace, keyId)
                if (value == null) {
                    val deleted = file.delete()
                    if (!deleted) {
                        throw BasicsStorageAccessException("Could not delete file $file")
                    }
                } else {
                    file.writeText(value.toString())
                }
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Could not write file $namespace/$keyId.json", e) // TODO: Properly propagate exceptions in the async methods
                throw BasicsStorageAccessException("Could not write file $namespace/$keyId.json", e)
            }
        }
    }

    override fun setupNamespace(namespace: String) {
        val subDir = File(directory, namespace)
        if (!subDir.isDirectory) {
            val success = subDir.mkdirs()
            if (!success) {
                throw IOException("Could not create storage directory ${subDir.absolutePath}")
            }
        }
    }

    private fun getFile(namespace: String, keyId: String) = File(File(directory, namespace), "$keyId.json")

    override fun shutdown(): CompletableFuture<Void?> = CompletableFuture.completedFuture(null)

}