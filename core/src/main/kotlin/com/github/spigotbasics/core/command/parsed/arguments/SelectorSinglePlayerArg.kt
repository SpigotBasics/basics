package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.rightOrNull
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SelectorSinglePlayerArg(name: String) : SelectorEntityArgBase<Player>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Player? {
        return get(sender, value, allowMultiple = false, allowEntities = false).rightOrNull()?.singleOrNull() as Player?
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return errorMessage0(sender, value, allowMultiple = false, allowEntities = false)
    }
}
