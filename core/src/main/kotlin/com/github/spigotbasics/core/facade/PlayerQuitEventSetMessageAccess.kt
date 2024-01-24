package com.github.spigotbasics.core.facade

import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitEventSetMessageAccess : SimpleMethodAggregator<PlayerQuitEvent, Component, String>(
    PlayerQuitEvent::class,
    "quitMessage", Component::class.java,
    "setQuitMessage", String::class.java
)