package com.github.spigotbasics.core.command.parsed.arguments

import org.bukkit.command.CommandSender

class AnyStringArg(name: String) : CommandArgument<String>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): String {
        return value
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return emptyList()
    }
}
