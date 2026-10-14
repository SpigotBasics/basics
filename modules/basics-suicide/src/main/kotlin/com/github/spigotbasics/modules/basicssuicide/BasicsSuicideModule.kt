package com.github.spigotbasics.modules.basicssuicide

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsSuicideModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.suicide",
            "Allows the player to kill themself",
        )

    private fun msgSuicide(player: Player) = messages.getMessage("suicide").concerns(player)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("suicide", permission).mapContext {
            description("Kills yourself")

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
                    player.health = 0.0
                    msgSuicide(player).sendToAllPlayers()
                }
            },
        ).register()
    }
}
