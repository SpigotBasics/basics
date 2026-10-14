package com.github.spigotbasics.modules.basicsjump

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.core.util.TeleportUtils
import org.bukkit.FluidCollisionMode
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause

class BasicsJumpModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.jump",
            "Allows the player to teleport to the block they are looking at",
        )

    private val msgJump
        get() = messages.getMessage("jump")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("jump", permission).mapContext {
            description("Teleports you to the block you are looking at")
            aliases(listOf("jumpto"))

            path {
                playerOnly()
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val player = sender as Player
                    val eye = player.eyeLocation
                    val target = player.world.rayTraceBlocks(eye, eye.direction, MAX_DISTANCE, FluidCollisionMode.NEVER, true)?.hitBlock
                    val standOn = target?.let(::findFreeSpaceAbove)
                    if (standOn == null) {
                        coreMessages.noSafeLocationFound.sendToPlayer(player)
                        return
                    }

                    val destination = standOn.location.add(0.5, 1.0, 0.5).setDirection(player.location.direction)
                    Spiper.teleportAsync(player, destination, TeleportCause.COMMAND)
                    msgJump.sendToPlayer(player)
                }
            },
        ).register()
    }

    /** First block at or above [block] that is safe to stand on with two free blocks above it */
    private fun findFreeSpaceAbove(block: Block): Block? =
        (block.y until block.world.maxHeight - 2).asSequence()
            .map { block.world.getBlockAt(block.x, it, block.z) }
            .firstOrNull {
                TeleportUtils.isSafeToStandOn(it.type) &&
                    TeleportUtils.isSafeToStandInside(it.getRelative(0, 1, 0).type) &&
                    TeleportUtils.isSafeToStandInside(it.getRelative(0, 2, 0).type)
            }

    companion object {
        private const val MAX_DISTANCE = 200.0
    }
}
