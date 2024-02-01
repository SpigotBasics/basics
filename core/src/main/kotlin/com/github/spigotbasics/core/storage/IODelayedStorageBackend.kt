package com.github.spigotbasics.core.storage

import com.google.gson.JsonElement
import java.util.concurrent.CompletableFuture

// TODO: Get rid of this
// TODO: Properly propagate exceptions in the async methods
class IODelayedStorageBackend(private val backend: StorageBackend, private val ioDelay: Long): StorageBackend by backend {

    override fun getJsonElement(namespace: String, keyId: String): CompletableFuture<JsonElement?> {
        return CompletableFuture.supplyAsync {
            try {
                Thread.sleep(ioDelay)
                backend.getJsonElement(namespace, keyId).join()
            } catch (e: Exception) {
                throw BasicsStorageAccessException("Could not read file $namespace/$keyId.json", e)
            }
        }
    }

    override fun setJsonElement(namespace: String, keyId: String, value: JsonElement?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            try {
                Thread.sleep(ioDelay)
                backend.setJsonElement(namespace, keyId, value).join()
            } catch (e: Exception) {
                throw BasicsStorageAccessException("Could not write file $namespace/$keyId.json", e)
            }
        }
    }

}