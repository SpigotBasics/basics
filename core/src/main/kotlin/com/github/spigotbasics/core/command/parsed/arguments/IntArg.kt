package com.github.spigotbasics.core.command.parsed.arguments

import org.bukkit.command.CommandSender

open class IntArg(name: String) : CommandArgument<Int>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Int? = value.toIntOrNull()
}
