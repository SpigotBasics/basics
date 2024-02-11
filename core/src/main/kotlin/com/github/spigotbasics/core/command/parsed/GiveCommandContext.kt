package com.github.spigotbasics.core.command.parsed

import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GiveCommandContext(
    sender: CommandSender,
    val receiver: Player,
    val item: Material,
) : ParsedCommandContext(sender)
