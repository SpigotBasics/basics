package com.github.spigotbasics.core.command.parsed

abstract class ParsedCommandExecutor<T : ParsedCommandContext> {
    abstract fun execute(context: T)
}
