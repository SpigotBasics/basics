package com.github.spigotbasics.modules.basicstop

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.core.util.TeleportUtils
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause

class BasicsTopModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.top",
            "Allows the player to teleport to the highest block above them",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.top.others",
            "Allows the player to teleport other players to the highest block above them",
        )

    private val msgTop
        get() = messages.getMessage("top")

    private fun msgTopOthers(player: Player) = messages.getMessage("top-others").concerns(player)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("top", permission).mapContext {
            usage = "[player]"
            description("Teleports to the highest block at your position")

            path {
                playerOnly()
            }

            path {
                permissions(permissionOthers)
                arguments {
                    named("player", SelectorSinglePlayerArg("player"))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val target = context["player"] as Player? ?: sender as Player
                    val location = target.location
                    val block = target.world.getHighestBlockAt(location)
                    // The highest block in the nether is the bedrock roof
                    if (target.world.environment == World.Environment.NETHER || !TeleportUtils.isSafeToStandOn(block.type)) {
                        coreMessages.noSafeLocationFound.sendToSender(sender)
                        return
                    }

                    val destination = block.location.add(0.5, 1.0, 0.5).setDirection(location.direction)
                    Spiper.teleportAsync(target, destination, TeleportCause.COMMAND)
                    (if (sender == target) msgTop else msgTopOthers(target)).sendToSender(sender)
                }
            },
        ).register()
    }
}
