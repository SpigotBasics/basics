package com.github.spigotbasics.core.command.parsed

sealed class ParseResult<out T> {
    data class Success<T>(val context: T) : ParseResult<T>()

    data class Failure(val errors: List<String>) : ParseResult<Nothing>()
}
