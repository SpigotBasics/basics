package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SelectorSinglePlayerArg(name: String) : SelectorEntityArgBase<Player>(name, allowMultiple = false, allowEntities = false) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Player? {
        return get(sender, value).rightOrNull()?.singleOrNull() as Player?
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return errorMessage0(sender, value)
    }
}
