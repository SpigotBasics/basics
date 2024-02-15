package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.Dictionary
import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.command.CommandSender

class DictionaryArg<T>(name: String, private val dictionary: Dictionary<T>) : CommandArgument<T>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): T? {
        return dictionary[value]
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return dictionary.keys.partialMatches(typing)
    }
}
