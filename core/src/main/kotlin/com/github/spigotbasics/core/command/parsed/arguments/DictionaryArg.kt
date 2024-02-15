package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.Dictionary
import com.github.spigotbasics.core.extensions.partialMatches

class DictionaryArg<T>(name: String, private val dictionary: Dictionary<T>) : CommandArgument<T>(name) {
    override fun parse(value: String): T? {
        return dictionary[value]
    }

    override fun tabComplete(typing: String): List<String> {
        return dictionary.keys.partialMatches(typing)
    }
}
