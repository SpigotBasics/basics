package com.github.spigotbasics.modules.basicsmsg.command

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.modules.basicsmsg.LastMessagedStore
import com.github.spigotbasics.modules.basicsmsg.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandRespond(private val messages: Messages, private val lastMessagedStore: LastMessagedStore) :
    CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = sender as Player
        val message = context["message"] as String
        val targetUUID = lastMessagedStore.getLastMessagedUUID(player.uniqueId)
        if (targetUUID == null) {
            messages.noRecentMessages.sendToSender(player)
            return
        }

        val target = Bukkit.getPlayer(targetUUID)
        if (target == null) {
            messages.recentNotOnline.sendToSender(player)
            return
        }

        messages.formatSent(message).concerns(target).sendToPlayer(player)
        messages.formatReceived(message).concerns(player).sendToPlayer(target)
    }
}
