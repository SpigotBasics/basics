package com.github.spigotbasics.core.extensions

import org.bukkit.entity.Player

fun Player.genitiveSuffix(): String = when (name.lowercase().last()) {
    's', 'x', 'z' -> "'"
    else -> "'s"
}