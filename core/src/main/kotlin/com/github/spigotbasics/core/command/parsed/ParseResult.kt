package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.core.messages.Message

sealed class ParseResult<out T> {
    data class Success<T>(val context: T) : ParseResult<T>()

    data class Failure(val errors: List<Message>) : ParseResult<Nothing>()
}
