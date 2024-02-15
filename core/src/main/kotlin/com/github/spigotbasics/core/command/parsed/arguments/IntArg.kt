package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender

open class IntArg(name: String) : CommandArgument<Int>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Int? = value.toIntOrNull()

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return Basics.messages.invalidValueForArgumentMustBeInteger(name, value)
    }
}
