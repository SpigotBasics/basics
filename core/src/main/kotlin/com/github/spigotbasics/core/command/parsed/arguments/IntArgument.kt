package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.command.parsed.CommandArgument

class IntArgument : CommandArgument<Int>() {
    override fun parse(value: String): Int? = value.toIntOrNull()
}
