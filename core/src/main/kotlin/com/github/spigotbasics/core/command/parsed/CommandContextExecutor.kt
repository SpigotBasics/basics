package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.core.command.parsed.context.CommandContext
import org.bukkit.command.CommandSender

interface CommandContextExecutor<T : CommandContext> {
    fun execute(
        sender: CommandSender,
        context: T,
    )
}
