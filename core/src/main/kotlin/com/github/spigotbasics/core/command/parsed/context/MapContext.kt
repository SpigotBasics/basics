package com.github.spigotbasics.core.command.parsed.context

import org.bukkit.command.CommandSender

class MapContext(map: Map<String, Any?>) : CommandContext, Map<String, Any?> by map {
    val sender: CommandSender by map
}
