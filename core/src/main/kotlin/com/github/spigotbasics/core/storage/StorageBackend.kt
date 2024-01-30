package com.github.spigotbasics.core.storage

import com.google.gson.JsonObject
import java.util.Optional
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface StorageBackend {

    fun getJsonObject(key: String, user: UUID): CompletableFuture<JsonObject?>

    fun setJsonObject(key: String, user: UUID, value: JsonObject?): CompletableFuture<Void?>

}