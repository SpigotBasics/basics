package com.github.spigotbasics.core.command.parsed2

abstract class CommandArgument<T> {
    abstract fun parse(value: String): T?
}