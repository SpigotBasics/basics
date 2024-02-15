package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import org.bukkit.command.CommandSender

class PrintPermissionsCommand : CommandContextExecutor<MapContext> {
    override fun execute(sender: CommandSender, context: MapContext) {
        for(permission in sender.server.pluginManager.permissions.filter { it.name.startsWith("basics.") }.sortedBy { it.name }) {
            sender.sendMessage(permission.name + " - " + permission.description + " - " + permission.default.toString())
        }
    }
}