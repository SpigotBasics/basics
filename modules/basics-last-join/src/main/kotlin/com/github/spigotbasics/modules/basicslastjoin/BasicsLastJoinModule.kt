package com.github.spigotbasics.modules.basicslastjoin

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
            storage?.getJsonObject(player.uniqueId.toString())?.whenComplete { json, u ->

                if (u != null) {
                    logger.warning("Failed to get last join for ${it.player.name}")
                    player.sendMessage("§cFailed to get last join. Please contact CMarco, he can fix this easily.")
                    return@whenComplete
                }

                if (json != null) {
                    val lastJoin = LastJoin.fromJson(json)
                    player.sendMessage("Welcome back, ${it.player.name}! Your last join was @ ${lastJoin.dateTime}")
                } else {
                    player.sendMessage("This is your first join, ${it.player.name}!")
                }
            }?.thenRun() {
                try {
                    storage?.setJsonObject(player.uniqueId.toString(), LastJoin().toJson())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

        }
    }

}