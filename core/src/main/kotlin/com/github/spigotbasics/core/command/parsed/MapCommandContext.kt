package com.github.spigotbasics.core.command.parsed

import org.bukkit.command.CommandSender

class MapCommandContext(map: Map<String, Any?>) : ParsedCommandContext, Map<String, Any?> by map {
    val sender: CommandSender by map
}
