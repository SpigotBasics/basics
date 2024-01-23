package com.github.spigotbasics.core.extensions

import org.bukkit.entity.Player

fun String.genitiveSuffix(): String = when (lowercase().last()) {
    's', 'x', 'z' -> "'"
    else -> "'s"
}