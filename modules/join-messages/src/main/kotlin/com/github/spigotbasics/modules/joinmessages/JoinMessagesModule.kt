package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.config.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinMessagesModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context), Listener {

    override fun onEnable() {
        //plugin.server.pluginManager.registerEvents(this, plugin)
        eventBus.subscribe(this)
    }

    private fun getJoinMessage(): Message = config.getMessage("join")
    private fun getJoinSelfMessage(): Message = config.getMessage("join-self")
    private fun getQuitMessage(): Message = config.getMessage("quit")

    @EventHandler
    fun joinMessage(event: PlayerJoinEvent) {
        event.joinMessage = null
        getJoinMessage().papi(event.player).sendMiniTo(audience.filter { it != event.player })
        getJoinSelfMessage().papi(event.player).sendMiniTo(audience.player(event.player))

    }

    @EventHandler
    fun leaveMessage(event: PlayerQuitEvent) {
        event.quitMessage = null
        getQuitMessage().papi(event.player).sendMiniTo(audience.all())
    }


}