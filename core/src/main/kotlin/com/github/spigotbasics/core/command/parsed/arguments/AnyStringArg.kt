package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.command.parsed.CommandArgument

class AnyStringArg(name: String) : CommandArgument<String>(name) {
    override fun parse(value: String): String {
        return value
    }

    override fun tabComplete(typing: String): List<String> {
        return emptyList()
    }
}