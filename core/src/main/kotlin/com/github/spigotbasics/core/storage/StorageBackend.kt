package com.github.spigotbasics.core.storage

import com.google.gson.JsonElement
import java.util.concurrent.CompletableFuture

interface StorageBackend {

    val type: StorageType

    fun getJsonElement(namespace: String, keyId: String): CompletableFuture<JsonElement?>

    fun setJsonElement(namespace: String, keyId: String, value: JsonElement?): CompletableFuture<Void?>

    fun setupNamespace(namespace: String)

}