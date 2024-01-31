package com.github.spigotbasics.modules.basicshomes

import com.github.spigotbasics.core.Serialization
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import com.github.spigotbasics.core.storage.NamespacedStorage
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

class BasicsHomesModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    var storage: NamespacedStorage? = null
    val homes = mutableMapOf<UUID, HomeList>()

    val permission = permissionManager.createSimplePermission("basics.home")

    override fun onEnable() {
        storage = createStorage()

        createCommand().name("home").permission(permission).executor(HomeCommand(this)).register()
        createCommand().name("sethome").permission(permission).executor(SetHomeCommand(this)).register()
    }

    fun loadHomeListBlocking(uuid: UUID): HomeList {
        val storage = storage ?: error("Storage is null")
        try {
            val json = storage.getJsonElement(uuid.toString()).get()
            return if (json == null) {
                HomeList()
            } else {
                Serialization.fromJson(json, HomeList::class.java)
            }
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to load home list for $uuid", e)
            return HomeList()
        }
    }


    override fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            val homeList = loadHomeListBlocking(uuid)
            homes[uuid] = homeList
        }
    }

    override fun savePlayerData(uuid: UUID): CompletableFuture<Void?> {
        return CompletableFuture.runAsync {
            val homes = homes[uuid] ?: error("Homes is null")
            val storage = storage ?: error("Storage is null")
            try {
                storage.setJsonElement(uuid.toString(), Serialization.toJson(homes)).get()
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Failed to save home list for $uuid", e)
            }
        }
    }

    override fun forgetPlayerData(uuid: UUID) {
        homes.remove(uuid)
    }

}