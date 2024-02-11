package com.github.spigotbasics.core.command.parsed

interface CommandExecutor<T : CommandContext> {
    fun execute(context: T)
}
