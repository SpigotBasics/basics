package com.github.spigotbasics.modules.basicssmite

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsSmiteModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.smite",
            "Allows the player to strike lightning where they are looking",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.smite.others",
            "Allows the player to strike other players with lightning",
        )

    private val msgSmite
        get() = messages.getMessage("smite")
    private val msgSmiteBlock
        get() = messages.getMessage("smite-block")

    private fun msgSmiteOthers(player: Player) = messages.getMessage("smite-others").concerns(player)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("smite", permission).mapContext {
            usage = "[player]"
            description("Strikes lightning")
            aliases(listOf("lightning"))

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
                    val target = context["player"] as Player?
                    if (target != null) {
                        target.world.strikeLightning(target.location)
                        msgSmite.sendToPlayer(target)
                        if (sender != target) msgSmiteOthers(target).sendToSender(sender)
                        return
                    }

                    val player = sender as Player
                    val location = player.getTargetBlock(null, MAX_DISTANCE).location
                    player.world.strikeLightning(location)
                    msgSmiteBlock.sendToPlayer(player)
                }
            },
        ).register()
    }

    companion object {
        private const val MAX_DISTANCE = 120
    }
}
