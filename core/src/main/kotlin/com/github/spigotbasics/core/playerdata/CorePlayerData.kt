package com.github.spigotbasics.core.playerdata

import com.github.spigotbasics.core.storage.StorageManager
import com.google.gson.JsonPrimitive
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.CompletableFuture

class CorePlayerData(storageManager: StorageManager) {

    private val nameToUuid = storageManager.createStorage("player_name_to_uuid")
    private val uuidToName = storageManager.createStorage("player_uuid_to_name")


    fun getUuidForName(name: String): CompletableFuture<UUID?> {
        return nameToUuid.getJsonElement(name).thenApply {
            if (it != null) {
                UUID.fromString(it.asString)
            } else {
                null
            }
        }
    }

    fun getNameForUuid(uuid: UUID): CompletableFuture<String?>? {
        return uuidToName.getJsonElement(uuid.toString()).thenApply {
            it?.asString
        }
    }

    fun storeNameAndUuid(name: String, uuid: UUID) {
        val jsonName = JsonPrimitive(name)
        val jsonUuid = JsonPrimitive(uuid.toString())
        uuidToName.setJsonElement(uuid.toString(), jsonName)
        nameToUuid.setJsonElement(name, jsonUuid)
    }

}