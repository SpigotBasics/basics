package com.github.spigotbasics.core.command.parsed

class IntArgument : CommandArgument<Int>() {
    override fun parse(value: String): Int? = value.toIntOrNull()
}