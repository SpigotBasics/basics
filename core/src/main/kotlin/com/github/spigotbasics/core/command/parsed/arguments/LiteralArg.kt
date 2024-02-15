package com.github.spigotbasics.core.command.parsed.arguments

import org.bukkit.command.CommandSender

class LiteralArg(name: String) : CommandArgument<String>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): String? {
        return if (value.equals(name, ignoreCase = true)) {
            value
        } else {
            null
        }
    }

    private val tabList = listOf(name)

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return if (name.startsWith(typing, ignoreCase = true)) {
            tabList
        } else {
            emptyList()
        }
    }
}
