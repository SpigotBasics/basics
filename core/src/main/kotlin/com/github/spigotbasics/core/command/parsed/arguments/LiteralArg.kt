package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.command.parsed.CommandArgument

class LiteralArg(name: String) : CommandArgument<Unit>(name) {
    override fun parse(value: String): Unit? {
        return if(value.equals(name, ignoreCase = true))
            Unit
        else
            null
    }

    private val tabList = listOf(name)

    override fun tabComplete(typing: String): List<String> {
        return if(name.startsWith(typing, ignoreCase = true))
            tabList
        else
            emptyList()
    }
}