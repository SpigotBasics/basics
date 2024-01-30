package com.github.spigotbasics.modules.basicslastjoin

import com.github.spigotbasics.core.Serialization
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import com.github.spigotbasics.core.storage.NamespacedStorage
import org.bukkit.event.player.PlayerJoinEvent

class BasicsLastJoinModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    var storage: NamespacedStorage? = null

    override fun onEnable() {
        storage = createStorage()
        eventBus.subscribe(PlayerJoinEvent::class.java) {

            val player = it.player
            storage?.getJsonElement(player.uniqueId.toString())?.whenComplete { json, u ->

                if (u != null) {
                    logger.warning("Failed to get last join for ${it.player.name}")
                    player.sendMessage("§cFailed to get last join. Please contact CMarco, he can fix this easily.")
                    return@whenComplete
                }

                if (json != null) {
                    val lastJoin = Serialization.fromJson(json, LastJoin::class.java)
                    player.sendMessage("Welcome back, ${it.player.name}! Your last join was @ ${lastJoin.dateTime}")
                } else {
                    player.sendMessage("This is your first join, ${it.player.name}!")
                }
            }?.thenRun() {
                try {
                    storage?.setJsonElement(player.uniqueId.toString(), Serialization.toJson(LastJoin()))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

        }
    }

}