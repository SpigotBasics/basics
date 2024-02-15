package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.messages.Message
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerArg(name: String) : CommandArgument<Player>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Player? {
        return Bukkit.getPlayer(value)
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return Bukkit.getOnlinePlayers().filter {
            if (sender is Player) {
                sender.canSee(it)
            } else {
                true
            }
        }.map { it.name }.partialMatches(typing)
    }

    // TODO: We need an ArgumentFactory! To get rid of the static access to Basics.messages
    override fun errorMessage(sender: CommandSender, value: String): Message {
        return Basics.messages.playerNotFound(value)
    }
}
