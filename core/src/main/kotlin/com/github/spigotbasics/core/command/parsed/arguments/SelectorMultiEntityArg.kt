package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity

class SelectorMultiEntityArg(name: String) : SelectorEntityArgBase<List<Entity>>(name, allowMultiple = true, allowEntities = true) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): List<Entity>? {
        return get(sender, value).fold(
            { _ -> null },
            { it },
        )
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return errorMessage0(sender, value)
    }
}
