package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandRepairAll(private val module: BasicsRepairModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val target = if (context["player"] != null) context["player"] as Player else sender as Player

        if (target == sender) {
            runAllSelf(target)
        } else {
            runAllOther(sender, target)
        }
    }

    private fun runAllSelf(player: Player) {
        repairAll(player)
        module.msgRepairAllSelf.concerns(player).sendToPlayer(player)
    }

    private fun runAllOther(
        sender: CommandSender,
        player: Player,
    ) {
        repairAll(player)
        module.msgRepairAllOther.concerns(player.player).sendToSender(sender)
    }
}
