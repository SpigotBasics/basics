package com.github.spigotbasics.modules.basicstime

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.IntArg
import com.github.spigotbasics.core.command.parsed.arguments.StringOptionArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsTimeModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.time",
            "Allows to change the time of the current world",
        )

    private val presets = mapOf("day" to 1000L, "noon" to 6000L, "night" to 13000L, "midnight" to 18000L)

    private fun msgTimeSet(
        world: String,
        ticks: Long,
    ) = messages.getMessage("time-set").tagUnparsed("world", world).tagParsed("ticks", ticks.toString())

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("time", permission).mapContext {
            usage = "<day|noon|night|midnight|ticks>"
            description("Changes the time of the current world")

            path {
                playerOnly()
                arguments {
                    named("preset", StringOptionArg("preset", presets.keys.toList()))
                }
            }

            path {
                playerOnly()
                arguments {
                    named("ticks", IntArg("ticks"))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val world = (sender as Player).world
                    val ticks = (context["preset"] as String?)?.let(presets::getValue) ?: (context["ticks"] as Int).toLong()
                    world.time = ticks
                    msgTimeSet(world.name, ticks).sendToSender(sender)
                }
            },
        ).register()
    }
}
