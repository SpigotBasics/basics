package com.github.spigotbasics.modules.basicstp

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.model.XYZCoords
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeleportCommand(module: BasicsTpModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = context.getOrDefault("player", sender) as Player
        val coords = context["coords"] as XYZCoords
        val location = coords.toLocation(player.world)
        player.teleport(location)
    }
}
