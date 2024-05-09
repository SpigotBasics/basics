package com.github.spigotbasics.modules.basicsenderchest

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsEnderchestModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permission =
        permissionManager.createSimplePermission("basics.enderchest", "Allows opening your enderchest with /enderchest")

    val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.enderchest.others",
            "Allows opening other players enderchests with /enderchest <player>",
        )

    val inventoryTitle get() = messages.getMessage("inventory-title")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("enderchest", permission).mapContext {
            usage = "[player]"
            description("Opens your enderchest")

            path {
                playerOnly()
            }

            path {
                permissions(permissionOthers)
                playerOnly()
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
                    val playerSender = sender as Player
                    val player = if (context["player"] != null) context["player"] as Player else playerSender
                    val setTitle = player != sender

                    val view = playerSender.openInventory(player.enderChest)
                    if (setTitle) view?.title = inventoryTitle.concerns(player).toLegacyString()
                }
            },
        ).register()
    }
}
