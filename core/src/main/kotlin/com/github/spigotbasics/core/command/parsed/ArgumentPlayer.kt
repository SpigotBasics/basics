package com.github.spigotbasics.core.command.parsed

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ArgumentPlayer : Argument<Player> {
    override fun parse(value: String): Player? {
        return Bukkit.getPlayer(value)
    }
}