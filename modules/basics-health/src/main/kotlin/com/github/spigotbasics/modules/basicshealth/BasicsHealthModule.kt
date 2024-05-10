package com.github.spigotbasics.modules.basicshealth

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsHealthModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permHeal =
        permissionManager.createSimplePermission(
            "basics.heal",
            "Allows the player to heal themself",
        )
    val permHealOthers =
        permissionManager.createSimplePermission(
            "basics.heal.others",
            "Allows to heal other players",
        )
    val permFeed =
        permissionManager.createSimplePermission(
            "basics.feed",
            "Allows the player to feed themself",
        )
    val permFeedOthers =
        permissionManager.createSimplePermission(
            "basics.feed.others",
            "Allows to feed other players",
        )
    private val msgHealed
        get() = messages.getMessage("healed")
    private val msgFed
        get() = messages.getMessage("fed")

    private fun msgHealedOthers(player: Player) = messages.getMessage("healed-others").concerns(player)

    private fun msgFedOthers(player: Player) = messages.getMessage("fed-others").concerns(player)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("heal", permHeal).mapContext {
            description("Heals Players")
            usage = "[player]"

            path {
                arguments {
                    playerOnly()
                }
            }

            path {
                arguments {
                    permissions(permHealOthers)
                    named("player", SelectorSinglePlayerArg("player"))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val target = if (context["player"] != null) context["player"] as Player else sender as Player
                    target.health = target.healthScale
                    val message = if (sender == target) msgHealed else msgHealedOthers(target)
                    message.sendToSender(sender)
                }
            },
        ).register()
        commandFactory.parsedCommandBuilder("feed", permFeed).mapContext {
            description("Feeds Players")
            usage = "[player]"

            path {
                arguments {
                    playerOnly()
                }
            }

            path {
                permissions(permFeedOthers)
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
                    val target = if (context["player"] != null) context["player"] as Player else sender as Player
                    target.foodLevel = 20
                    val message = if (sender == target) msgFed else msgFedOthers(target)
                    message.sendToSender(sender)
                }
            },
        ).register()
    }
}
