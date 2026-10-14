package com.github.spigotbasics.modules.basicsgetpos

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.messages.tags.providers.LocationTag
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsGetPosModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.getpos",
            "Allows the player to see their own coordinates",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.getpos.others",
            "Allows the player to see the coordinates of other players",
        )

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("getpos", permission).mapContext {
            usage = "[player]"
            description("Shows the coordinates of a player")
            aliases(listOf("coords"))

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
                    val message =
                        if (sender == target) {
                            messages.getMessage("getpos")
                        } else {
                            messages.getMessage("getpos-others").concerns(target)
                        }
                    message.tags(LocationTag(target.location, 1)).sendToSender(sender)
                }
            },
        ).register()
    }
}
