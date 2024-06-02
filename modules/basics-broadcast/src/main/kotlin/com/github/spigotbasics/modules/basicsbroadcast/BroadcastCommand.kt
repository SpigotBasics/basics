package com.github.spigotbasics.modules.basicsbroadcast

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BroadcastCommand(private val module: BasicsBroadcastModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val parsed = context["parsed"] != null
        val rawMessage = context["message"] as String

        val message =
            if (parsed) {
                module.messageFactory.createMessage(rawMessage).concerns(sender as? Player)
            } else {
                module.messageFactory.createPlainMessage(rawMessage)
            }

        message.sendToAllPlayers()
        message.sendToConsole()
    }
}
