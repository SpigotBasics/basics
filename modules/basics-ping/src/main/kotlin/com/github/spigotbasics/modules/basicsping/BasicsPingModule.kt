package com.github.spigotbasics.modules.basicsping

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsPingModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.ping",
            "Allows the player to see their own ping",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.ping.others",
            "Allows the player to see the ping of other players",
        )

    private fun msgPing(ping: Int) = messages.getMessage("ping").tagParsed("ping", ping.toString())

    private fun msgPingOthers(
        player: Player,
        ping: Int,
    ) = messages.getMessage("ping-others").tagParsed("ping", ping.toString()).concerns(player)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("ping", permission).mapContext {
            usage = "[player]"
            description("Shows the ping of a player")

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
                    val message = if (sender == target) msgPing(target.ping) else msgPingOthers(target, target.ping)
                    message.sendToSender(sender)
                }
            },
        ).register()
    }
}
