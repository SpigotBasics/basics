package com.github.spigotbasics.core.command.parsed.arguments

open class IntArg(name: String) : CommandArgument<Int>(name) {
    override fun parse(value: String): Int? = value.toIntOrNull()
}
