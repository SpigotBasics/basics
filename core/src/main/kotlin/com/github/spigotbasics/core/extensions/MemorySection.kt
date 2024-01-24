package com.github.spigotbasics.core.extensions

import org.bukkit.configuration.MemorySection

fun MemorySection.getAsNewLineSeparatedString(key: String): String {
    val value = get(key) ?: return ""
    if(value is List<*>) {
        return value.joinToString("\n")
    }
    return value.toString()
}