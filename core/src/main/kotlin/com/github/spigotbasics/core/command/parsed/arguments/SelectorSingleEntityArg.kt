package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.rightOrNull
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class SelectorSingleEntityArg(name: String) : SelectorEntityArgBase<Entity>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Entity? {
        return get(sender, value, allowMultiple = false, allowEntities = true).rightOrNull()?.singleOrNull()
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return errorMessage0(sender, value, allowMultiple = false, allowEntities = true)
    }
}
