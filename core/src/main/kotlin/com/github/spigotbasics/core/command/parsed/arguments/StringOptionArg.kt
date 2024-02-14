package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.command.parsed.CommandArgument
import com.github.spigotbasics.core.extensions.partialMatches

class StringOptionArg(name: String, private val validOptions: List<String>) : CommandArgument<String>(name) {
    override fun parse(value: String): String? {
        return if(validOptions.contains(value))
            value
        else
            null
    }

    override fun tabComplete(typing: String): List<String> {
        return validOptions.partialMatches(typing)
    }
}