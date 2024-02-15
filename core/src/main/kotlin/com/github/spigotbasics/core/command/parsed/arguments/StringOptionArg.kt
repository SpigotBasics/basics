package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.command.CommandSender

class StringOptionArg(name: String, private val validOptions: List<String>) : CommandArgument<String>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): String? {
        return if (validOptions.contains(value)) {
            value
        } else {
            null
        }
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return validOptions.partialMatches(typing)
    }
}
