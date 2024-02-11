package com.github.spigotbasics.core.command.parsed

import org.bukkit.command.CommandSender

interface CommandExecutor<T : CommandContext> {
    fun execute(
        sender: CommandSender,
        context: T,
    )
}
