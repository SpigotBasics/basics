package com.github.spigotbasics.modules.basicsweather

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.WeatherType

class WeatherCommand(val module: BasicsWeatherModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): CommandResult? {
        val player = requirePlayer(context.sender)
        val args = context.args
        if (args.isEmpty()) {
            return CommandResult.USAGE
        }
        val weatherType = module.weatherTypeFromString(args[0]) ?: throw failInvalidArgument(args[0]).asException()
        val world = player.world

        // TODO: Implement duration

//        val duration = if (args.size > 1) {
//            val dur = args[1].toIntOrNull() ?: throw failInvalidArgument(args[1]).asException()
//            if (dur < 0) {
//                throw failInvalidArgument(args[1]).asException()
//            }
//            dur
//        } else {
//            0
//        }

        val weatherName =
            when (weatherType) {
                WeatherType.CLEAR -> module.msgClear
                WeatherType.DOWNFALL -> module.msgStorm
            }

        val message = module.msgWeatherChanged.tagMessage("new-weather", weatherName)
        world.setStorm(weatherType == WeatherType.DOWNFALL)
        message.sendToSender(context.sender)
        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String> {
        return listOf("clear", "storm").partialMatches(context.args[0])
    }
}
