package com.github.spigotbasics.modules.joinmessages

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinMessagesModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context), Listener {

    override fun onEnable() {
        val listener = JoinLeaveListener(config, audience)

        eventBus.subscribe(PlayerJoinEvent::class.java, listener::joinMessage)
        eventBus.subscribe(PlayerQuitEvent::class.java, listener::leaveMessage)
    }





}