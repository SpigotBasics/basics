package com.github.spigotbasics.core.command.parsed2

interface CommandExecutor<T : CommandContext> {
    fun execute(context: T)
}
