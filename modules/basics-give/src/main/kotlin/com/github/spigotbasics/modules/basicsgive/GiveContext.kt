package com.github.spigotbasics.modules.basicsgive

import com.github.spigotbasics.core.command.parsed.ParsedCommandContext
import org.bukkit.Material
import org.bukkit.entity.Player

class GiveContext(val receiver: Player, val material: Material, val amount: Int = 1) : ParsedCommandContext
