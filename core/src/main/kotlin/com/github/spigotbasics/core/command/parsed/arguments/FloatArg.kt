package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender

open class FloatArg(name: String) : CommandArgument<Float>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Float? = value.toFloatOrNull()?.takeIf { it.isFinite() }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return Basics.messages.invalidValueForArgumentMustBeNumber(name, value)
    }
}
