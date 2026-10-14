package com.github.spigotbasics.modules.basicsclearinventory

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsClearInventoryModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.clearinventory",
            "Allows the player to clear their own inventory",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.clearinventory.others",
            "Allows the player to clear the inventory of other players",
        )

    private val msgCleared
        get() = messages.getMessage("cleared")

    private fun msgClearedOthers(player: Player) = messages.getMessage("cleared-others").concerns(player)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("clearinventory", permission).mapContext {
            usage = "[player]"
            description("Clears a player's inventory")
            aliases(listOf("ci"))

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
                    target.inventory.clear()
                    val message = if (sender == target) msgCleared else msgClearedOthers(target)
                    message.sendToSender(sender)
                }
            },
        ).register()
    }
}
