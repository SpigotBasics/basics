package com.github.spigotbasics.modules.basicsextinguish

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsExtinguishExecutor(private val module: BasicsExtinguishModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = context["player"] as Player? ?: sender as Player?

        val msg =
            if (sender == player) {
                module.messageExtinguished
            } else {
                module.messageExtinguishedOther
            }

        player?.fireTicks = 0
        msg.concerns(player).sendToSender(sender)
    }
}
