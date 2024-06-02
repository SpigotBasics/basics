package com.github.spigotbasics.modules.basicsmsg.command

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.modules.basicsmsg.LastMessagedStore
import com.github.spigotbasics.modules.basicsmsg.Messages
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class CommandMessage(private val messages: Messages, private val store: LastMessagedStore) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = context["player"] as Player
        val message = context["message"] as String

        when (sender) {
            is Player -> {
                if (sender == player) {
                    messages.formatSelf(message).concerns(sender).sendToPlayer(player)
                } else {
                    messages.formatSent(message).concerns(player).sendToPlayer(sender)
                    messages.formatReceived(message).concerns(sender).sendToPlayer(player)
                    store.setLastMessagedUUID(sender.uniqueId, player.uniqueId)
                }
            }

            is ConsoleCommandSender -> {
                messages.formatConsole(message).sendToPlayer(player)
            }

            else -> {
                messages.formatOther(message).sendToSender(player)
            }
        }
    }
}
