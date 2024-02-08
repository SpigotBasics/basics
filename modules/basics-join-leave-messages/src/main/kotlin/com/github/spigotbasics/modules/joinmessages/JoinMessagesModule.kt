package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinMessagesModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context), Listener {
    override fun onEnable() {
        val listener = JoinLeaveListener(plugin.facade, config)

        eventBus.subscribe(PlayerJoinEvent::class.java, listener::joinMessage)
        eventBus.subscribe(PlayerQuitEvent::class.java, listener::leaveMessage)
    }
}
