package com.github.spigotbasics.modules.basicsburn

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.IntRangeArg
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsBurnModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.burn",
            "Allows the player to set other players on fire",
        )

    private fun msgBurn(
        player: Player,
        seconds: Int,
    ) = messages.getMessage("burn").tagParsed("seconds", seconds.toString()).concerns(player)

    override fun onEnable() {
        val playerArg = SelectorSinglePlayerArg("player")
        val secondsArg = IntRangeArg("seconds", { 1 }, { MAX_SECONDS })

        commandFactory.parsedCommandBuilder("burn", permission).mapContext {
            usage = "<player> [seconds]"
            description("Sets a player on fire")

            path {
                arguments {
                    named("player", playerArg)
                }
            }

            path {
                arguments {
                    named("player", playerArg)
                    named("seconds", secondsArg)
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val target = context["player"] as Player
                    val seconds = context["seconds"] as Int? ?: DEFAULT_SECONDS
                    target.fireTicks = seconds * TICKS_PER_SECOND
                    msgBurn(target, seconds).sendToSender(sender)
                }
            },
        ).register()
    }

    companion object {
        private const val DEFAULT_SECONDS = 5
        private const val MAX_SECONDS = 3600
        private const val TICKS_PER_SECOND = 20
    }
}
