package com.github.spigotbasics.modules.basicsfly

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsFlyModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.fly",
            "Allows to toggle fly using /fly",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.fly.others",
            "Allows to toggle fly for other players using /fly",
        )
    private val msgEnabled
        get() = messages.getMessage("fly-enabled")
    private val msgDisabled
        get() = messages.getMessage("fly-disabled")

    private fun msgEnabledOthers(player: Player) = messages.getMessage("fly-enabled-others").concerns(player)

    private fun msgDisabledOthers(player: Player) = messages.getMessage("fly-disabled-others").concerns(player)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("fly", permission).mapContext {
            usage = "[player]"
            description("Toggles fly mode")

            path {
                arguments {
                    playerOnly()
                }
            }

            path {
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
                    target.allowFlight = !target.allowFlight
                    val message =
                        when {
                            target.allowFlight && context.sender == target -> msgEnabled
                            !target.allowFlight && context.sender == target -> msgDisabled
                            target.allowFlight -> msgEnabledOthers(target)
                            else -> msgDisabledOthers(target)
                        }

                    message.sendToSender(context.sender)
                }
            },
        ).register()
    }
}
