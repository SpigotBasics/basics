package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SelectorMultiPlayerArg(name: String) : SelectorPlayerArgBase<List<Player>>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): List<Player>? {
        return get(sender, value, true).fold(
            { _ -> null },
            { it },
        )
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return errorMessage0(sender, value, true)
    }
}
