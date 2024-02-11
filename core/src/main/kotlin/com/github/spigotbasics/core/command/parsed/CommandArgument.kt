package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message

abstract class CommandArgument<T>(val name: String) {
    abstract fun parse(value: String): T?

    open fun tabComplete(typing: String): List<String> = emptyList()

    // TODO: This is using the static Singleton :/
    open fun errorMessage(value: String? = null): Message = Basics.messages.invalidValueForArgument(name, value ?: "null")
}
