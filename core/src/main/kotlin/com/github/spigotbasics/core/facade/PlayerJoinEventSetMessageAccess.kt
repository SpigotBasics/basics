package com.github.spigotbasics.core.facade

import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinEventSetMessageAccess : SimpleMethodAggregator<PlayerJoinEvent, Component, String>(
    PlayerJoinEvent::class,
    "joinMessage", Component::class.java,
    "setJoinMessage", String::class.java
)