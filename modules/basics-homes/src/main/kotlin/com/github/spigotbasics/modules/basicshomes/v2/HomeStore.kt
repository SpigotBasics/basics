package com.github.spigotbasics.modules.basicshomes.v2

import com.github.spigotbasics.core.Serialization
import com.github.spigotbasics.modules.basicshomes.data.Home
import com.github.spigotbasics.modules.basicshomes.data.HomeList
import com.google.gson.reflect.TypeToken
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level

class HomeStore(private val module: BasicsHomesModule) {
    private val store = module.createStorage()
    private val homeCache = ConcurrentHashMap<UUID, HomeList>()

    fun getHomeList(uuid: UUID): HomeList {
        return homeCache[uuid] ?: error("HomeList for $uuid doesn't exist")
    }

    fun forgetHomeList(uuid: UUID) {
        homeCache.remove(uuid)
    }

    fun saveHomeListBlocking(uuid: UUID) {
        val homes = homeCache[uuid] ?: error("HomeList for $uuid doesn't exist")
        try {
            store.setJsonElement(uuid.toString(), Serialization.toJson(homes.toList()))
        } catch (exception: Exception) {
            module.logger.log(Level.SEVERE, "Failed to save home list for $uuid", exception)
        }
    }

    fun loadHomeListBlocking(uuid: UUID) {
        try {
            val json = store.getJsonElement(uuid.toString()).get()
            val list =
                if (json == null) {
                    HomeList()
                } else {
                    HomeList.fromList(Serialization.fromJson(json, object : TypeToken<MutableList<Home>>() {}))
                }
            homeCache[uuid] = list
        } catch (e: Exception) {
            module.logger.log(Level.SEVERE, "Failed to load home list for $uuid", e)
            homeCache[uuid] = HomeList()
        }
    }
}
