package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.messages.Message
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandSender
import java.util.stream.Collectors

class WorldArg(private val module: BasicsWorldModule, name: String) : CommandArgument<World>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): World? {
        return if (sender.hasPermission("basics.world.$value")) Bukkit.getWorld(value) else null
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return Basics.messages.getMessage("world-not-found").tagUnparsed("argument", value)
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return Bukkit.getWorlds().stream().map { it.name }.filter { sender.hasPermission("basics.world.$it") }
            .collect(Collectors.toList())
    }
}
