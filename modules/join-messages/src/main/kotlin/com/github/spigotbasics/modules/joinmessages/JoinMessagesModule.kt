package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.config.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinMessagesModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context), Listener {

    companion object {
        const val JOIN = "join"
        const val JOIN_SELF = "join-self"
        const val JOIN_CONSOLE = "join-console"
        const val QUIT = "quit"
        const val QUIT_CONSOLE = "quit-console"
    }

    override fun onEnable() {
        eventBus.subscribe(this)
    }


    @EventHandler
    fun joinMessage(event: PlayerJoinEvent) {
        event.joinMessage = null

        // All players
        config.getMessage(JOIN).papi(event.player).sendMiniTo(audience.players().filterAudience { it != event.player })

        // Self player
        config.getMessage(JOIN_SELF).papi(event.player).sendMiniTo(audience.player(event.player))

        // Console
        config.getMessage(JOIN_CONSOLE).papi(event.player).sendMiniTo(audience.console())

    }

    @EventHandler
    fun leaveMessage(event: PlayerQuitEvent) {
        event.quitMessage = null

        // All players
        config.getMessage(QUIT).papi(event.player).sendMiniTo(audience.players())

        // Console
        config.getMessage(QUIT_CONSOLE).papi(event.player).sendMiniTo(audience.console())
    }


}