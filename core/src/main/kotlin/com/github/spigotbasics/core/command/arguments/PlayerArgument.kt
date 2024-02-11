package com.github.spigotbasics.core.command.arguments

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PlayerArgument : ArgumentType<Player> {
    override fun parse(value: String): Player {
        val player = Bukkit.getPlayer(value)
        if (player == null) {
            throw PlayerNotFoundException(value)
        }
        return player
    }
}
