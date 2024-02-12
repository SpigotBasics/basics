package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.command.parsed.CommandArgument
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.messages.Message
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerArg(name: String) : CommandArgument<Player>(name) {
    override fun parse(value: String): Player? {
        return Bukkit.getPlayer(value)
    }

    override fun tabComplete(typing: String): List<String> {
        return Bukkit.getOnlinePlayers().map { it.name }.partialMatches(typing)
    }

    // TODO: We need an ArgumentFactory!
    override fun errorMessage(value: String?): Message {
        return Basics.messages.playerNotFound(value ?: "null")
    }
}
