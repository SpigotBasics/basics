package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity

class SelectorMultiEntityArg(name: String) : SelectorEntityArgBase<List<Entity>>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): List<Entity>? {
        return get(sender, value, allowMultiple = true, allowEntities = true).fold(
            { _ -> null },
            { it },
        )
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return errorMessage0(sender, value, allowMultiple = true, allowEntities = true)
    }
}
