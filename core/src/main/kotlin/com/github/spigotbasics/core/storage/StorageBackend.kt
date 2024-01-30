package com.github.spigotbasics.core.storage

import com.google.gson.JsonObject
import java.util.concurrent.CompletableFuture

interface StorageBackend {

    val type: StorageType

    fun getJsonObject(namespace: String, keyId: String): CompletableFuture<JsonObject?>

    fun setJsonObject(namespace: String, keyId: String, value: JsonObject?): CompletableFuture<Void?>

    fun setupNamespace(namespace: String)

}