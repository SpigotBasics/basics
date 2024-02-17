package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.util.TeleportUtils
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsWorldCommand(private val module: BasicsWorldModule) : CommandContextExecutor<MapContext> {

    override fun execute(sender: CommandSender, context: MapContext) {
        val player = sender as Player
        val force: Boolean = context["force"] != null
        val world: World = context["world"] as World

        val origin = player.location

        if (world == player.world) {
            module.msgAlreadyInWorld(world.name).sendToSender(player)
            return
        }

        val targetLocation = TeleportUtils.getScaledLocationInOtherWorld(origin, world)

    }
}
