package com.github.spigotbasics.modules.basicsgamemode

import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.GameMode
import org.bukkit.command.CommandSender

class GameModeArgument(private val module: BasicsGamemodeModule, name: String) : CommandArgument<GameMode>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): GameMode? {
        return when (value) {
            "survival", "s", "0" -> GameMode.SURVIVAL
            "creative", "c", "1" -> GameMode.CREATIVE
            "adventure", "a", "2" -> GameMode.ADVENTURE
            "spectator", "sp", "3" -> GameMode.SPECTATOR
            else -> null
        }
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        val list = mutableListOf<String>()

        if (sender.hasPermission(module.permSurvival)) {
            list += "survival"
        }
        if (sender.hasPermission(module.permCreative)) {
            list += "creative"
        }
        if (sender.hasPermission(module.permAdventure)) {
            list += "adventure"
        }
        if (sender.hasPermission(module.permSpectator)) {
            list += "spectator"
        }

        return list.partialMatches(typing)
    }
}
