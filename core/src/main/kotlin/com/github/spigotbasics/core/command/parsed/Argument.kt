package com.github.spigotbasics.core.command.parsed

interface Argument<T> {
    fun parse(value: String): T?
}
