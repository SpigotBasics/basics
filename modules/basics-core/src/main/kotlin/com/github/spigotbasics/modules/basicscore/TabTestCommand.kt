package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import org.bukkit.command.CommandSender

class TabTestCommand :
    CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        sender.sendMessage("TabTestCommand executed: $context")
    }
}
