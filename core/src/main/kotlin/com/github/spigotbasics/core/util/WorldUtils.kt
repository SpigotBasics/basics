package com.github.spigotbasics.core.util

import org.bukkit.Bukkit
import org.bukkit.World

object WorldUtils {
    val defaultWorldName by lazy {
        Bukkit.getWorlds().firstOrNull { it.environment == World.Environment.NORMAL }?.name
    }

    val netherWorldName by lazy {
        Bukkit.getWorlds().firstOrNull { it.environment == World.Environment.NETHER }?.name
    }

    val endWorldName by lazy {
        Bukkit.getWorlds().firstOrNull { it.environment == World.Environment.THE_END }?.name
    }
}
