package com.github.spigotbasics.core.command.parsed

abstract class CommandArgument<T> {
    abstract fun parse(value: String): T?
    open fun tabComplete(): List<String> = emptyList()
    open fun errorMessage(value: String? = null): String = "Invalid value $value for argument ${this::class.simpleName}"
}
