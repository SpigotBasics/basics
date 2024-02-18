package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.util.TeleportUtils
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Level

class BasicsWorldCommand(private val module: BasicsWorldModule) : CommandContextExecutor<MapContext> {
    private val logger = BasicsLoggerFactory.getModuleLogger(module, BasicsWorldCommand::class)

    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = sender as Player
        val force: Boolean = context["force"] != null
        val world: World = context["world"] as World

        val origin = player.location

        logger.debug(100, "Teleporting player ${player.name} to world ${world.name}")

        if (world == player.world) {
            module.msgAlreadyInWorld(world.name).sendToSender(player)
            logger.debug(100, "Abort: Player already is in target world")
            return
        }

        val targetLocation = TeleportUtils.getScaledLocationInOtherWorld(origin, world)
        logger.debug(100, "Scaled target location: $targetLocation")

        if (force) {
            logger.debug(100, "Force-Teleporting player ${player.name} to world ${world.name} now ...")
            Spiper.teleportAsync(player, targetLocation)
            return
        }

        module.msgStartingTeleport(world.name).sendToPlayerActionBar(player)

        val future = TeleportUtils.findSafeLocationInSameChunkAsync(targetLocation, world.minHeight, world.maxHeight)

        future.thenAccept { safeLocation ->
            if (safeLocation == null) {
                logger.debug(100, "No safe location found.")
                module.coreMessages.noSafeLocationFound.sendToSender(player)
                return@thenAccept
            }

            module.scheduler.runTask {
                logger.debug(100, "Safe location found: $safeLocation")
                Spiper.teleportAsync(player, safeLocation).whenComplete { success, throwable ->
                    if (throwable != null || !success) {
                        logger.debug(100, "Could not teleport player to world ${world.name} - success: $success, throwable: $throwable")
                        module.msgUnsuccessful(world.name).sendToSender(player)
                        throwable?.let {
                            module.logger.log(
                                Level.SEVERE,
                                "Could not teleport player to world ${world.name}",
                                it,
                            )
                        }
                    } else if (success) {
                        logger.debug(100, "Player ${player.name} teleported to world ${world.name} successfully")
                        module.msgSuccess(world.name).sendToSender(player)
                    }
                }
            }
        }.exceptionally { throwable ->
            logger.debug(100, "Exception while finding safe location for player: $throwable")
            module.logger.log(Level.SEVERE, "Could not find safe location for player", throwable)
            module.msgUnsuccessful(world.name).sendToSender(player)
            null
        }
    }
}
