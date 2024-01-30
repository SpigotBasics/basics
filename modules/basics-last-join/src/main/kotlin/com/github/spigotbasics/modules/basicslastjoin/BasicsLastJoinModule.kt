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
            val lastJoin = storage.getJsonObject(player.name) // Normaly use UUIDs. This is for testing purposes.

            lastJoin.whenComplete { lastJoin, u ->

                if(u != null) {
                    logger.warning("Failed to get last join for ${it.player.name}")
                    player.sendMessage("§cFailed to get last join. Please contact CMarco, he can fix this easily.")
                    return@whenComplete
                }

                if (lastJoin != null) {
                    player.sendMessage("Welcome back, ${it.player.name}! Your last join was [idk yet]")
                } else {
                    player.sendMessage("This is your first join, ${it.player.name}!")
                }
            }.whenComplete { _, _ ->
                //player.sendMessage("We'll now set your new last join to now")
            }

        }
    }

}