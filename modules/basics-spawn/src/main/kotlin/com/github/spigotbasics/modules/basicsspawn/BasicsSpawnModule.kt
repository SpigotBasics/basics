package com.github.spigotbasics.modules.basicsspawn

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.messages.tags.providers.LocationTag
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause

class BasicsSpawnModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.spawn",
            "Allows the player to teleport to the world spawn",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.spawn.others",
            "Allows the player to teleport other players to the world spawn",
        )
    private val permissionSetSpawn =
        permissionManager.createSimplePermission(
            "basics.setspawn",
            "Allows the player to set the world spawn",
        )

    private val msgSpawn
        get() = messages.getMessage("spawn")

    private fun msgSpawnOthers(player: Player) = messages.getMessage("spawn-others").concerns(player)

    private fun msgSpawnSet(location: Location) = messages.getMessage("spawn-set").tags(LocationTag(location))

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("spawn", permission).mapContext {
            usage = "[player]"
            description("Teleports to the world spawn")

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
                    val world = target.world.takeIf { it.environment == World.Environment.NORMAL } ?: server.worlds.first()
                    val spawn = world.spawnLocation.add(0.5, 0.0, 0.5)
                    Spiper.teleportAsync(target, spawn, TeleportCause.COMMAND)
                    (if (sender == target) msgSpawn else msgSpawnOthers(target)).sendToSender(sender)
                }
            },
        ).register()

        commandFactory.parsedCommandBuilder("setspawn", permissionSetSpawn).mapContext {
            description("Sets the world spawn to your location")

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
                    player.world.setSpawnLocation(player.location)
                    msgSpawnSet(player.world.spawnLocation).sendToPlayer(player)
                }
            },
        ).register()
    }
}
