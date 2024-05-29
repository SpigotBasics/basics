package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandRepairHand(private val module: BasicsRepairModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val target = if (context["player"] != null) context["player"] as Player else sender as Player

        if (target == sender) {
            runHandSelf(target)
        } else {
            runHandOther(sender, target)
        }
    }

    private fun runHandSelf(player: Player) {
        repairHand(player)
        module.msgRepairHandSelf.concerns(player).sendToPlayer(player)
    }

    private fun runHandOther(
        sender: CommandSender,
        player: Player,
    ) {
        repairHand(player)
        module.msgRepairHandOther.concerns(player.player).sendToSender(sender)
    }
}
