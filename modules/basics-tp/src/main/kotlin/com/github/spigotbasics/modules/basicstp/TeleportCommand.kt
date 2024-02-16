package com.github.spigotbasics.modules.basicstp

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.model.TripleContextCoordinates
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
                (context["destination"] as? TripleContextCoordinates?),
                (context["destination"] as? Entity?),
            )

        // First relative: Sender's location
        // Second position: Teleportee's location

        targets.forEach { teleportee ->

            val teleporteeLoc = teleportee.location
            val senderLoc = (sender as? Entity)?.location ?: teleporteeLoc

            val location =
                coordsOrEntity.fold(
                    { coords -> coords.toLocation(senderLoc, teleporteeLoc) },
                    { it.location },
                )

            teleportee.teleport(location)
            module.createMessage(teleportee, coordsOrEntity.mapLeft { location }).sendToSender(sender)
        }
    }
}
