package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.command.parsed.CommandArgument
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerArgument : CommandArgument<Player>() {
    override fun parse(value: String): Player? {
        return Bukkit.getPlayer(value)
    }

    override fun tabComplete(): MutableList<String> {
        return Bukkit.getOnlinePlayers().map { it.name }.toMutableList()
    }

    override fun errorMessage(value: String?): String {
        return "Player $value not found"
    }
}
