package com.github.spigotbasics.core.facade.event

import com.github.spigotbasics.core.facade.SimpleMethodAggregator
import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinEventSetMessageAccess : SimpleMethodAggregator<PlayerJoinEvent, Component, String, Unit, Unit>(
    PlayerJoinEvent::class,
    "joinMessage", Component::class.java,
    "setJoinMessage", String::class.java
) {
    // TODO: Move set method one interface / class up
    fun set(event: PlayerJoinEvent, component: Component, string: String) {
        apply(event, component, string)
    }
}