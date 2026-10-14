package com.github.spigotbasics.modules.basicsnear

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.IntRangeArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.extensions.decimals
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsNearModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.near",
            "Allows the player to list nearby players",
        )

    private fun msgNear(
        radius: Int,
        players: String,
    ) = messages.getMessage("near").tagParsed("radius", radius.toString()).tagUnparsed("players", players)

    private fun msgNone(radius: Int) = messages.getMessage("near-none").tagParsed("radius", radius.toString())

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("near", permission).mapContext {
            usage = "[radius]"
            description("Lists nearby players")

            path {
                playerOnly()
            }

            path {
                playerOnly()
                arguments {
                    named("radius", IntRangeArg("radius", { 1 }, { MAX_RADIUS }))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val player = sender as Player
                    val radius = context["radius"] as Int? ?: DEFAULT_RADIUS
                    val nearby =
                        player.world.players
                            .filter { it != player && player.canSee(it) }
                            .map { it to it.location.distance(player.location) }
                            .filter { (_, distance) -> distance <= radius }
                            .sortedBy { (_, distance) -> distance }

                    if (nearby.isEmpty()) {
                        msgNone(radius).sendToPlayer(player)
                        return
                    }
                    val list = nearby.joinToString(", ") { (other, distance) -> "${other.name} (${distance.decimals()}m)" }
                    msgNear(radius, list).sendToPlayer(player)
                }
            },
        ).register()
    }

    companion object {
        private const val DEFAULT_RADIUS = 200
        private const val MAX_RADIUS = 1000
    }
}
