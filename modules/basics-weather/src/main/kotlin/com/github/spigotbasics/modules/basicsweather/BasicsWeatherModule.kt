package com.github.spigotbasics.modules.basicsweather

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.IntArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsWeatherModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission = permissionManager.createSimplePermission("basics.weather")

    private val weatherChangedMessage
        get() = messages.getMessage("weather-changed")
    private val clearMessage
        get() = messages.getMessage("clear")
    private val rainMessage
        get() = messages.getMessage("rain")
    private val thunderMessage
        get() = messages.getMessage("thunder")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("weather", permission).mapContext {
            usage = "<weather-type>"
            description("Sets the weather of the world")

            path {
                arguments {
                    playerOnly()
                    named("type", BasicsWeatherTypeArg("type"))
                }
            }

            path {
                arguments {
                    playerOnly()
                    named("type", BasicsWeatherTypeArg("type"))
                    named("duration", IntArg("duration"))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val player = sender as Player
                    val type = context["type"] as BasicsWeatherType
                    val duration = context["duration"] as Int?
                    type.activate(player.world, duration)
                    val statusMessage: Message =
                        when (type) {
                            BasicsWeatherType.CLEAR -> clearMessage
                            BasicsWeatherType.RAIN -> rainMessage
                            BasicsWeatherType.THUNDER -> thunderMessage
                        }

                    weatherChangedMessage.tagMessage("new-weather", statusMessage).concerns(player).sendToPlayer(player)
                }
            },
        ).register()
    }
}
