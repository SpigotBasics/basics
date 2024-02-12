package com.github.spigotbasics.core.command.parsed

import org.bukkit.command.CommandSender

interface ParsedCommandContextExecutor<T : ParsedCommandContext> {
    fun execute(
        sender: CommandSender,
        context: T,
    )
}
