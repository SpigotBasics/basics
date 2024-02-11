package com.github.spigotbasics.core.command.parsed

import org.bukkit.Material
import org.bukkit.entity.Player

class GiveCommandContext(val receiver: Player, val material: Material, val amount: Int = 1) : CommandContext
