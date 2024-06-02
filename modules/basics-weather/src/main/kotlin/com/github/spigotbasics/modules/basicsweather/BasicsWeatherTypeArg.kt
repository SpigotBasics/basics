package com.github.spigotbasics.modules.basicsweather

import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.command.CommandSender

class BasicsWeatherTypeArg(name: String) : CommandArgument<BasicsWeatherType>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): BasicsWeatherType? {
        return BasicsWeatherType.fromString(value)
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return BasicsWeatherType.entries.map { it.name.lowercase() }.partialMatches(typing)
    }
}
