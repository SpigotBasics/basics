package com.github.spigotbasics.core.command.arguments

interface ArgumentType<T> {
    fun parse(value: String): T

    fun tabComplete(value: String): MutableList<String>? = null
}
