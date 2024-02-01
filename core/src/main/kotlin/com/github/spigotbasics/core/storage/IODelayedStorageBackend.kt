package com.github.spigotbasics.core.storage

import com.google.gson.JsonElement
import java.util.concurrent.CompletableFuture

class IODelayedStorageBackend(private val backend: StorageBackend, private val ioDelay: Long): StorageBackend by backend {

    override fun getJsonElement(namespace: String, keyId: String): CompletableFuture<JsonElement?> {
        return CompletableFuture.supplyAsync {
            Thread.sleep(ioDelay)
            backend.getJsonElement(namespace, keyId).join()
        }
    }

    override fun setJsonElement(namespace: String, keyId: String, value: JsonElement?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            Thread.sleep(ioDelay)
            backend.setJsonElement(namespace, keyId, value).join()
        }
    }

}