package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandSender

class WorldArg(name: String) : CommandArgument<World>(name) {
    override fun parse(sender: CommandSender, value: String): World? {
        return Bukkit.getWorld(value)
    }

    override fun errorMessage(sender: CommandSender, value: String): Message {
        return Basics.messages.getMessage("world-not-found")
    }
}
