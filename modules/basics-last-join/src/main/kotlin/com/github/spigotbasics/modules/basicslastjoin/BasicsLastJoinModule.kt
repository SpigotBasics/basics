package com.github.spigotbasics.modules.basicslastjoin

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.event.player.PlayerJoinEvent

class BasicsLastJoinModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val storage = createStorage() // TODO: This shall not be possible before isEnabled is true.
    //  Otherwise, the storage will be closed when the module is disabled and enabled again.

    override fun onEnable() {
        eventBus.subscribe(PlayerJoinEvent::class.java) {

            val player = it.player
            storage.getJsonObject(player.name) // Normaly use UUIDs. This is for testing purposes.
                .whenComplete { json, u ->

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
                }.thenRun() {
                    try {
                        player.sendMessage("We'll now set your new last join to now")
                        storage.setJsonObject(player.name, LastJoin().toJson())
                        player.sendMessage("Setter created")
                    } catch (e: Throwable) {
                        player.sendMessage("§cSetter failed: ${e.message}")
                        e.printStackTrace()
                    }
                }

        }
    }

}