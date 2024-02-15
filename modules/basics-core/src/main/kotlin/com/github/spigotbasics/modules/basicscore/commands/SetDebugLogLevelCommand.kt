package com.github.spigotbasics.modules.basicscore.commands

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.logger.BasicsLogger
import org.bukkit.command.CommandSender

class SetDebugLogLevelCommand : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        BasicsLogger.debugLogLevel = context["level"] as Int
        sender.sendMessage("Debug log level set to ${context["level"]}")
    }
}
