package com.github.spigotbasics.modules.basicstp

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.model.XYZCoords
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity

class TeleportCommand(private val module: BasicsTpModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        @Suppress("UNCHECKED_CAST")
        val targets: List<Entity> = context["targets"] as List<Entity>? ?: listOf(sender as Entity)

        val coordsOrEntity =
            Either.of(
                (context["destination"] as? XYZCoords?)?.toLocation(targets.first().world),
                (context["destination"] as? Entity?),
            )

        val location =
            coordsOrEntity.fold(
                { it },
                { it.location },
            )

        targets.forEach {
            it.teleport(location)
            module.createMessage(it, coordsOrEntity).sendToSender(sender)
        }
    }
}
