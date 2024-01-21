package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import com.github.spigotbasics.core.replace.mini
import com.github.spigotbasics.core.replace.papi
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinMessagesModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context), Listener {

    override fun onEnable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    private fun getJoinMessage(): String? = config.getString("join-message")
    private fun getQuitMessage(): String? = config.getString("quit-message")

    @EventHandler
    fun joinMessage(event: PlayerJoinEvent) {
        event.joinMessage = getJoinMessage()?.papi(event.player)?.mini() ?: return
    }

    @EventHandler
    fun leaveMessage(event: PlayerQuitEvent) {
        event.quitMessage = getQuitMessage()?.papi(event.player)?.mini() ?: return
    }
    
}