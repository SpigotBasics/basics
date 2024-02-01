package com.github.spigotbasics.core.extensions

import com.github.spigotbasics.core.util.DurationParser
import org.bukkit.configuration.MemorySection

fun MemorySection.getAsNewLineSeparatedString(key: String): String {
    val value = get(key) ?: return ""
    if(value is List<*>) {
        return value.joinToString("\n")
    }
    return value.toString()
}

fun MemorySection.getDurationAsTicks(key: String, defaultValue: Long): Long {
    return DurationParser.parseDurationToTicks(getString(key) ?: return defaultValue)
}

fun MemorySection.getDurationAsMillis(key: String, defaultValue: Long): Long {
    return DurationParser.parseDurationToMillis(getString(key) ?: return defaultValue)
}