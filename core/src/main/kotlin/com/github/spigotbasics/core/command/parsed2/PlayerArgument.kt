package com.github.spigotbasics.core.command.parsed2

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerArgument : CommandArgument<Player>() {
    override fun parse(value: String): Player? {
        return Bukkit.getPlayer(value)
    }
}