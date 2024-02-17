package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.util.TeleportUtils
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Level

class BasicsWorldCommand(private val module: BasicsWorldModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = sender as Player
        val force: Boolean = context["force"] != null
        val world: World = context["world"] as World

        val origin = player.location

        if (world == player.world) {
            module.msgAlreadyInWorld(world.name).sendToSender(player)
            return
        }

        val targetLocation = TeleportUtils.getScaledLocationInOtherWorld(origin, world)

        if (force) {
            Spiper.teleportAsync(player, targetLocation)
        }

        val future = TeleportUtils.findSafeLocationInSameChunkAsync(targetLocation, world.minHeight, world.maxHeight)
        future.thenAccept { safeLocation ->
            if (safeLocation == null) {
                module.coreMessages.noSafeLocationFound.sendToSender(player)
                return@thenAccept
            }

            module.scheduler.runTask {
                Spiper.teleportAsync(player, safeLocation).whenComplete { success, throwable ->
                    module.msgUnsuccessful(world.name).sendToSender(player)
                    if (throwable != null || !success) {
                        module.msgUnsuccessful(world.name).sendToSender(player)
                        throwable?.let {
                            module.logger.log(
                                Level.SEVERE,
                                "Could not teleport player to world ${world.name}",
                                it,
                            )
                        }
                    } else if (success) {
                        module.msgSuccess(world.name).sendToSender(player)
                    }
                }
            }
        }.exceptionally { throwable ->
            module.logger.log(Level.SEVERE, "Could not find safe location for player", throwable)
            module.msgUnsuccessful(world.name).sendToSender(player)
            null
        }

        module.msgStartingTeleport(world.name).sendToPlayerActionBar(player)
    }
}
