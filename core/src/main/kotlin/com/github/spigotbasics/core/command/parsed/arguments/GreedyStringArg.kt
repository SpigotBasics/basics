package com.github.spigotbasics.core.command.parsed.arguments

import org.bukkit.command.CommandSender

class GreedyStringArg(name: String) : CommandArgument<String>(name) {
    override val greedy: Boolean = true

    override fun parse(sender: CommandSender, value: String): String {
        return value
    }

    override fun tabComplete(sender: CommandSender, typing: String): List<String> {
        return emptyList()
    }
}
