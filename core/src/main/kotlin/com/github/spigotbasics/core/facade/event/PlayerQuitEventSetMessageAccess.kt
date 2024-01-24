package com.github.spigotbasics.core.facade.event

import com.github.spigotbasics.core.facade.SimpleMethodAggregator
import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitEventSetMessageAccess : SimpleMethodAggregator<PlayerQuitEvent, Component, String, Unit, Unit>(
    PlayerQuitEvent::class,
    "quitMessage", Component::class.java,
    "setQuitMessage", String::class.java
) {

    // TODO: Move set method one interface / class up
    fun set(event: PlayerQuitEvent, component: Component, string: String) {
        apply(event, component, string)
    }
}