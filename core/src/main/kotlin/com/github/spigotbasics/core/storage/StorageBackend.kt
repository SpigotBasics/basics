package com.github.spigotbasics.core.storage

import com.google.gson.JsonObject
import java.util.concurrent.CompletableFuture

interface StorageBackend {

    val type: StorageType

    fun getJsonObject(namespace: String, user: String): CompletableFuture<JsonObject?>

    fun setJsonObject(namespace: String, user: String, value: JsonObject?): CompletableFuture<Void?>

}