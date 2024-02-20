package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity

class SelectorSingleEntityArg(name: String) : SelectorEntityArgBase<Entity>(name, allowMultiple = false, allowEntities = true) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Entity? {
        return get(sender, value).rightOrNull()?.singleOrNull()
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return errorMessage0(sender, value)
    }
}
